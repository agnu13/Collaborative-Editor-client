/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controllers;

import authenticate.entities.User;
import exception.ConnectivityFailureException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import notificationService.Notification;
import notificationService.NotificationManager;
import notificationService.notifyObservers.NotificationObserver;
import static ui.controllers.IDEController.WEB_EASE;
import ui.util.Message;
import ui.nodes.NotificationNode;

/**
 * FXML Controller class
 *
 * @author Ashish
 */
public class NotificationController implements Initializable, NotificationObserver {

    @FXML
    private Label back;

    @FXML
    private VBox notificationsParent;

    private HamBurgerMenuController hamBurgerMenuController;

    private User user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    void initialize(HamBurgerMenuController controller, User user) {
        hamBurgerMenuController = controller;
        this.user = user;

        //System.out.println("registered");
        NotificationManager notificationManager = new NotificationManager(user.getUserIdentifier());
        notificationManager.register(this);
        notificationManager.start();
        //NotificationNode node = new NotificationNode(null, hamBurgerMenuController.getIdeController());
        //NotificationNode node2 = new NotificationNode(null, hamBurgerMenuController.getIdeController());
        //notificationsParent.getChildren().addAll(node, node2);
    }

    @FXML
    private void backClicked() {
        hamBurgerMenuController.closeSubMenu();
    }

    @FXML
    private void clearAllClicked(MouseEvent evt) {
        NotificationManager notiManager = new NotificationManager(user.getUserIdentifier());

        new Thread(() -> {
            try {
                notiManager.clearNotifications();
            } catch (ConnectivityFailureException ex) {
                Message.showError("Clear Notification Error", ex.getMessage());
                Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(400),
                new KeyValue(
                        notificationsParent.translateXProperty(), 300, WEB_EASE
                ),
                new KeyValue(
                        notificationsParent.opacityProperty(), 0, WEB_EASE
                )
        ));
        timeline.play();

        timeline.setOnFinished((evt2) -> {
            notificationsParent.getChildren().clear();
            notificationsParent.setTranslateX(0);
            notificationsParent.setOpacity(1);
        });
    }

    @Override
    public void notifyObserver(ArrayList<Notification> list) {
        //System.out.println("notified");

        for (Notification notification : list) {
            NotificationNode node = new NotificationNode(notification, hamBurgerMenuController.getIdeController());
            Platform.runLater(() -> {
                notificationsParent.getChildren().add(node);
            });
        }
    }

}
