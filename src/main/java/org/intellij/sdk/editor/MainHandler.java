package org.intellij.sdk.editor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


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

    public void httpCommand(String command) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                        URI.create("http://localhost:3131/" + command))
                .build();
        var response = client.sendAsync(request, (HttpResponse.ResponseInfo res) -> {
            return null;
        });
    }


}
