// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.intellij.sdk.editor;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerAdapter;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.psi.PsiFile;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This is a custom {@link TypedHandlerDelegate} that handles actions activated keystrokes in the editor.
 * The execute method inserts a fixed string at Offset 0 of the document.
 * Document changes are made in the context of a write action.
 */
class MyTypedHandler extends TypedHandlerDelegate {
  public void mousePressed(EditorMouseEvent e) {
  }

  @NotNull
  @Override
  public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
    // Get the document and project
    final Document document = editor.getDocument();
    @NotNull DocumentListener tess;
   // editor.addEditorMouseListener(new CustomListener());
    @NotNull BulkFileListener listener;
    @NotNull Disposable disposable = null;

    project.getMessageBus().connect().subscribe(VirtualFileManager.VFS_CHANGES,
            new BulkFileListener() {
              @Override
              public void after(@NotNull List<? extends VFileEvent> events) {
                Notifications.Bus.notify(new Notification("Custom Notification Group","File changed!", NotificationType.INFORMATION));

              }
            });
    // Construct the runnable to substitute the string at offset 0 in the document
    Runnable runnable = () -> document.insertString(0, "editor_basics\n");
    // Make the document change in the context of a write action.
    WriteCommandAction.runWriteCommandAction(project, runnable);

    MessageBus messageBus = project.getMessageBus();
    messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
      @Override
      public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        Editor editor = source.getSelectedTextEditor();
        Notifications.Bus.notify(new Notification("Custom Notification Group","File opened!" + file.getName(), NotificationType.INFORMATION));


      }

      @Override
      public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
      }

      @Override
      public void selectionChanged(@NotNull FileEditorManagerEvent event) {
      }
    });

    return Result.STOP;
  }
}

class CustomListener implements EditorMouseListener {
  @NotNull
  @Override
  public void mouseClicked(EditorMouseEvent event) {
    Notifications.Bus.notify(new Notification("Custom Notification Group","Clicked", NotificationType.INFORMATION));
  }
}
