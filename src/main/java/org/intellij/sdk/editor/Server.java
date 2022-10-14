package org.intellij.sdk.editor;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        connectToServer();
    }

    public static void connectToServer() {
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

                    //Have the server take input from the client and echo it back
                    //This should be placed in a loop that listens for a terminator text e.g. bye

                    while(scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        serverPrintOut.println("Echo from <Your Name Here> Server: " + line);
                        Notifications.Bus.notify(new Notification("Custom Notification Group", line, NotificationType.INFORMATION));
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
}