package org.intellij.sdk.editor;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import icons.SdkIcons;
import org.jetbrains.annotations.NotNull;



public class MoveToCssClassAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        MainHandler handler = new MainHandler();
        handler.httpCommand("move-to-class");
    }


    @Override
    public void update(AnActionEvent event) {
        // Enable/disable depending on whether user is editing
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabled(editor != null);
        // Take this opportunity to set an icon for the group.
        event.getPresentation().setIcon(SdkIcons.Sdk_default_icon);
    }
}
