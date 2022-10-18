package org.intellij.sdk.editor;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


class MainHandler {
    public void onProjectOpened(Project project) throws IOException {
        Server.connectToServer(project);
    }

    public void onFileChanged(Project project) throws IOException {
        Server.connectToServer(project);
    }

    public void onEditorMouseClick(Editor editor, VirtualFile file) {
      //  Notifications.Bus.notify(new Notification("Custom Notification Group", "Clicked2 " + file.getName(), NotificationType.INFORMATION));

    }


}
