package org.intellij.sdk.editor;

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

import java.io.IOException;

public class ProjectOpenCloseListener implements ProjectManagerListener {
    static boolean isProjectOpen;

    @Override
  public void projectOpened(@NotNull Project project) {
        MainHandler handler = new MainHandler();

        if (!isProjectOpen) {
            try {
                handler.onProjectOpened(project);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            isProjectOpen = true;
        }


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
           /*     Editor editor = event.getManager().getSelectedTextEditor();
                editor.addEditorMouseListener(new EditorMouseListener(){
                    @Override
                    public void mouseClicked(EditorMouseEvent event) {
                        editorMouseClickHandler(event);
                    }
                });

                try {
                    handler.onFileChanged(project);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/

            }
        });
    }

    public void editorMouseClickHandler(EditorMouseEvent event) {
    /*    Project project = ProjectManager.getInstance().getOpenProjects()[0];
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile file = manager.getSelectedFiles()[0];
        MainHandler handler = new MainHandler();
        handler.onEditorMouseClick(event.getEditor(), file);*/
    }

}

