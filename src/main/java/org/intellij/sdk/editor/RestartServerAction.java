package org.intellij.sdk.editor;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import icons.SdkIcons;
import org.jetbrains.annotations.NotNull;

public class RestartServerAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        Server.connectToServer(project);
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
