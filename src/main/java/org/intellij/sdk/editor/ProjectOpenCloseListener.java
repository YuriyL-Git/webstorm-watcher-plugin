package org.intellij.sdk.editor;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;

public class ProjectOpenCloseListener implements ProjectManagerListener {
    @Override
  public void projectOpened(@NotNull Project project) {
     // Notifications.Bus.notify(new Notification("Custom Notification Group","Project OPENED!", NotificationType.INFORMATION));

        MessageBus messageBus = project.getMessageBus();
        messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
            @Override
            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                Editor editor = source.getSelectedTextEditor();
            }

            @Override
            public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
            }

            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                Editor editor = event.getManager().getSelectedTextEditor();
                editor.addEditorMouseListener(new MyCustomListener());

            }
        });
    }
}

class MyCustomListener implements EditorMouseListener {
    @NotNull
    @Override
    public void mouseClicked(EditorMouseEvent event) {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile file = manager.getSelectedFiles()[0];

        Notifications.Bus.notify(new Notification("Custom Notification Group","Clicked " + file.getName(), NotificationType.INFORMATION));
    }
}
