package org.intellij.sdk.editor;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static Project editor;

    public static void main(String[] args) {
    }

    public static void createServer(int portNumber, Project project) {
        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(portNumber);
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
                    }
                } catch (IOException e) {
                    System.err.println("Unable to process client request");
                    e.printStackTrace();
                }
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();

        Notifications.Bus.notify(new Notification("Custom Notification Group", "Server started on port: " + portNumber, NotificationType.INFORMATION));
    }

    public static void connectToServer(Project project) {
        int PORT_NUMBER_SERVER1 = 9991;
        int PORT_NUMBER_SERVER2 = 9992;
        createServer(PORT_NUMBER_SERVER1, project);
        createServer(PORT_NUMBER_SERVER2, project);
    }

    public static @NotNull
    @NlsSafe String getCommandResponce(String command, Project project) {
        FileEditorManager manager = FileEditorManager.getInstance(project);
        Editor editor = manager.getSelectedTextEditor();
        String result = "";

        if (command.startsWith("code::")) {
            String code = command.replace("code::", "").replaceAll("\\*&\\*", "\n");
            int textLength = editor.getDocument().getTextLength();

            ApplicationManager.getApplication().invokeLater(() -> {
                WriteCommandAction.runWriteCommandAction(project, () -> {
                    editor.getDocument().deleteString(0, textLength);
                    editor.getDocument().insertString(0, code);
                });
            });
            result = "code:: success";
        }

        if (command.startsWith("moveCaret::")) {
            String caretPosition = command.replace("moveCaret::", "").replaceAll("\\*&\\*", "\n");
            String[] coords = caretPosition.split("\\s+");
            int line = Integer.parseInt(coords[0]);
            int column = Integer.parseInt(coords[1]);
            LogicalPosition position = new LogicalPosition(line, column);

            ApplicationManager.getApplication().invokeLater(() -> {
                WriteCommandAction.runWriteCommandAction(project, () -> {
                    editor.getCaretModel().moveToLogicalPosition(position);
                   editor.getScrollingModel().scrollTo(position, ScrollType.CENTER);
                });
            });
            result = "moveCaret:: success";
        }

        if (command.startsWith("openFile::")) {
            String filepath = command.replace("openFile::", "");
            Notifications.Bus.notify(new Notification("Custom Notification Group", filepath, NotificationType.INFORMATION));

            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(filepath);

            ApplicationManager.getApplication().invokeLater(() -> {
                WriteCommandAction.runWriteCommandAction(project, () -> {
                    FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(
                            project,file
                    ), true);

                });
            });
            result = "openFile:: success";
        }

        if (result.length() > 0) {
            return result;
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