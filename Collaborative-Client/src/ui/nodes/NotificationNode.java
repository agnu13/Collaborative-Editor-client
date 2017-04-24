package ui.nodes;

import ui.util.MyNotification;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import notificationService.Notification;
import org.controlsfx.control.Notifications;
import projectManager.Collections;
import ui.controllers.IDEController;

public class NotificationNode extends AnchorPane {

    public NotificationNode(Notification notification, IDEController ideController) {

        Collections project = notification.getProject();

        Label label = new Label(project.getName());
        label.getStyleClass().add("recent-project-label");
        label.setCursor(Cursor.HAND);

        label.setOnMouseClicked((MouseEvent evt) -> {
            ideController.loadProject(project);
            ideController.closeHamburgerMenu();
        });

        Platform.runLater(() -> {
            /*Notifications not = Notifications.create()
                    .title(project.getName())
                    .text(notification.getNotificationMessage())
                    .hideAfter(Duration.seconds(7));
            
            not.show();*/
            MyNotification.show(project.getName(), notification.getNotificationMessage());
        });

        Label l2 = new Label(notification.getNotificationMessage());
        l2.getStyleClass().add("shared-by");
        AnchorPane.setTopAnchor(l2, 20d);

        this.getChildren().addAll(label, l2);
    }
}
