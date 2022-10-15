package org.intellij.sdk.editor;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
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

                    serverPrintOut.println("Hello World! Enter Peace to exit.");


                    Notifications.Bus.notify(new Notification("Custom Notification Group","Custom messsage: ", NotificationType.INFORMATION));


                    while(scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        ApplicationManager.getApplication().invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                serverPrintOut.println("Echo from <Your Name Here> Server: " + Server.getCommandResponce(line, project));
                            }
                        });
                        Notifications.Bus.notify(new Notification("Custom Notification Group","Responce: " + line, NotificationType.INFORMATION));
                    }
                } catch (IOException e) {
                    System.err.println("Unable to process client request");
                    e.printStackTrace();
                }
            }
        };
       Thread serverThread = new Thread(serverTask);


        serverThread.start();

        Notifications.Bus.notify(new Notification("Custom Notification Group", "Server started on port: "+PORT_NUMBER, NotificationType.INFORMATION));
    }

    public static @NotNull @NlsSafe String getCommandResponce(String command, Project project) {
        FileEditorManager manager = FileEditorManager.getInstance(project);
        Editor editor = manager.getSelectedTextEditor();
        Notifications.Bus.notify(new Notification("Custom Notification Group","Command: " + command, NotificationType.INFORMATION));

        if (Objects.equals(command, "editor")) {
            return editor.getDocument().getText();
        } else {
            return "Wrong command";
        }
    }
}