package org.intellij.sdk.editor;

import com.esotericsoftware.kryo.NotNull;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;

class MainHandler {
    public void start(Editor editor, VirtualFile file) {
        Notifications.Bus.notify(new Notification("Custom Notification Group","Clickeddd " + file.getName(), NotificationType.INFORMATION));
    }
}
