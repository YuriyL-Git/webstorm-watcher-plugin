package org.intellij.sdk.editor;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Server {
    private static Project editor;

    public static void main(String[] args) {
    }

    public static void connectToServer(Project project) {
        int PORT_NUMBER = 9991;

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(9991);
                    Socket connectionSocket = serverSocket.accept();

                    InputStream inputToServer = connectionSocket.getInputStream();
                    OutputStream outputFromServer = connectionSocket.getOutputStream();

                    Scanner scanner = new Scanner(inputToServer, "UTF-8");
                    PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

                    serverPrintOut.println("Start");//dont delete!

                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        ApplicationManager.getApplication().invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                serverPrintOut.println("" + Server.getCommandResponce(line, project));
                            }
                        });
                        // Notifications.Bus.notify(new Notification("Custom Notification Group", "Request: " + line, NotificationType.INFORMATION));
                    }
                } catch (IOException e) {
                    System.err.println("Unable to process client request");
                    e.printStackTrace();
                }
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();

        Notifications.Bus.notify(new Notification("Custom Notification Group", "Server started on port: " + PORT_NUMBER, NotificationType.INFORMATION));
    }

    public static @NotNull
    @NlsSafe String getCommandResponce(String command, Project project) {
        FileEditorManager manager = FileEditorManager.getInstance(project);
        Editor editor = manager.getSelectedTextEditor();
        String result;

        if (command.startsWith("code::")) {

            String code = command.replace("code::", "").replaceAll("\\*&\\*", "\n");
            int textLength = editor.getDocument().getTextLength();

            ApplicationManager.getApplication().invokeLater(() -> {
                WriteCommandAction.runWriteCommandAction(project, () -> {
                    editor.getDocument().deleteString(0, textLength);
                    editor.getDocument().insertString(0, code);
                });
            });
            result = "Code is accepted";
        }


        switch (command) {
            case "editorText":
                result = editor.getDocument().getText();
                break;
            case "caretLine":
                result = editor.getCaretModel().getLogicalPosition().line + "";
                break;
            case "caretColumn":
                result = editor.getCaretModel().getLogicalPosition().column + "";
                break;
            case "filePath":
                result = manager.getSelectedFiles()[0].getPath();
                break;
            case "fileName":
                result = manager.getSelectedFiles()[0].getName();
                break;

            default:
                result = "Wrong command";
                break;
        }


        return result;
    }
}