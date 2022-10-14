package org.intellij.sdk.editor;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


class MainHandler {
    public void onProjectOpened() throws IOException {
        Server.connectToServer();
    }

    public void onFileChanged() throws IOException {
    }

    public void onEditorMouseClick(Editor editor, VirtualFile file) {
        Notifications.Bus.notify(new Notification("Custom Notification Group", "Clicked2 " + file.getName(), NotificationType.INFORMATION));

    }


}
