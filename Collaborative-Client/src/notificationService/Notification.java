package notificationService;

import com.google.gson.Gson;
import projectManager.Collections;

public class Notification {
    private Collections project;
    private String notificationMessage;
            
    String serialize() {
       return new Gson().toJson(this);
    }
    
    void deserialize(String content) {
        Notification notification = new Gson().fromJson(content, Notification.class);
        this.notificationMessage = notification.getNotificationMessage();
        project = new Collections(notification.getProject().getName(), notification.getProject().getPath());
    }

    public Collections getProject() {
        return project;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }
}
