/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controllers;

import authenticate.entities.User;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import notificationService.NotificationManager;
import projectManager.Collections;
import ui.Main;
import static ui.controllers.IDEController.WEB_EASE;

/**
 * FXML Controller class
 *
 * @author Ashish
 */
public class HamBurgerMenuController implements Initializable {

    private IDEController ideController;
    private ProfileController profileController;
    private NotificationController notificationController;
    private ProjectsController projectsController;
    
    private User user;
    
    @FXML
    private Label hamburger, notificationLabel;

    @FXML
    private AnchorPane parent;

    @FXML
    private VBox recentProjectParent;

    private AnchorPane subHamBurgerMenu;
    private AnchorPane notifSubMenu, projectsSubMenu, profileSubMenu, settingsSubMenu;
    private final int sideMenuTransitionTime = 500;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        {
            Label label = new Label("project1");
            label.getStyleClass().add("recent-project-label");
            label.setCursor(Cursor.HAND);


            recentProjectParent.getChildren().add(label);
        }
        {
            Label label = new Label("project2");
            label.getStyleClass().add("recent-project-label");
            label.setCursor(Cursor.HAND);


            recentProjectParent.getChildren().add(label);
        }
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Notification.fxml"));
            notifSubMenu = fxmlLoader.load();
            notificationController = ((NotificationController) fxmlLoader.getController());
            //((NotificationController) fxmlLoader.getController()).setHamBurderMenuControllerInstance(this);
            AnchorPane.setLeftAnchor(notifSubMenu, 0d);
            AnchorPane.setTopAnchor(notifSubMenu, 0d);
            AnchorPane.setRightAnchor(notifSubMenu, 0d);
            AnchorPane.setBottomAnchor(notifSubMenu, 0d);

            FXMLLoader fxmlLoader2 = new FXMLLoader(Main.class.getResource("fxml/Profile.fxml"));
            profileSubMenu = fxmlLoader2.load();
            profileController = (ProfileController) fxmlLoader2.getController();
            ((ProfileController) fxmlLoader2.getController()).setHamBurderMenuControllerInstance(this);
            AnchorPane.setLeftAnchor(profileSubMenu, 0d);
            AnchorPane.setTopAnchor(profileSubMenu, 0d);
            AnchorPane.setRightAnchor(profileSubMenu, 0d);
            AnchorPane.setBottomAnchor(profileSubMenu, 0d);

            FXMLLoader fxmlLoader3 = new FXMLLoader(Main.class.getResource("fxml/Settings.fxml"));
            settingsSubMenu = fxmlLoader3.load();
            ((SettingsController) fxmlLoader3.getController()).setHamBurderMenuControllerInstance(this);
            AnchorPane.setLeftAnchor(settingsSubMenu, 0d);
            AnchorPane.setTopAnchor(settingsSubMenu, 0d);
            AnchorPane.setRightAnchor(settingsSubMenu, 0d);
            AnchorPane.setBottomAnchor(settingsSubMenu, 0d);
            
            FXMLLoader fxmlLoader4 = new FXMLLoader(Main.class.getResource("fxml/Projects.fxml"));
            projectsSubMenu = fxmlLoader4.load();
            projectsController = ((ProjectsController) fxmlLoader4.getController());
            ((ProjectsController) fxmlLoader4.getController()).setHamBurderMenuControllerInstance(this);
            AnchorPane.setLeftAnchor(projectsSubMenu, 0d);
            AnchorPane.setTopAnchor(projectsSubMenu, 0d);
            AnchorPane.setRightAnchor(projectsSubMenu, 0d);
            AnchorPane.setBottomAnchor(projectsSubMenu, 0d);

            subHamBurgerMenu = new AnchorPane();
            AnchorPane.setLeftAnchor(subHamBurgerMenu, 0d);
            AnchorPane.setTopAnchor(subHamBurgerMenu, 0d);
            AnchorPane.setRightAnchor(subHamBurgerMenu, 5d);
            AnchorPane.setBottomAnchor(subHamBurgerMenu, 0d);

            DropShadow ds = new DropShadow();
            ds.setRadius(4);
            ds.setOffsetX(0);
            //ds.setColor(Color.rgb(0, 0, 0, 0.5));

            subHamBurgerMenu.setEffect(ds);
        } catch (IOException ex) {
            Logger.getLogger(HamBurgerMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setIDEControllerInstance(IDEController ideController) {
        this.ideController = ideController;
    }

    public void setUser(User user) {
        this.user = user;
        profileController.setUser(user);
        notificationController.initialize(this, user);
    }

    @FXML
    private void hamburgerClicked(MouseEvent evt) {
        getIdeController().closeHamburgerMenu();
    }

    @FXML
    private void newProjectClicked(MouseEvent evt) {
        getIdeController().closeHamburgerMenu();
        getIdeController().newProjectLabelClicked(evt);
    }

    @FXML
    private void notificationLabelClicked(MouseEvent evt) {
        subHamBurgerMenu.getChildren().clear();
        subHamBurgerMenu.getChildren().add(notifSubMenu);

        showSubMenu();
    }

    @FXML
    private void profileLabelClicked(MouseEvent evt) {
        subHamBurgerMenu.getChildren().clear();
        subHamBurgerMenu.getChildren().add(profileSubMenu);

        showSubMenu();
    }

    @FXML
    private void settingsLabelClicked(MouseEvent evt) {
        subHamBurgerMenu.getChildren().clear();
        subHamBurgerMenu.getChildren().add(settingsSubMenu);

        showSubMenu();
    }

    @FXML
    private void projectsLabelClicked(MouseEvent evt) {
        
        
        subHamBurgerMenu.getChildren().clear();
        subHamBurgerMenu.getChildren().add(projectsSubMenu);

        showSubMenu();
        
        projectsController.refresh(user);
    }
    
    public void showSubMenu() {
        subHamBurgerMenu.setTranslateX(-320);
        parent.getChildren().remove(subHamBurgerMenu);
        parent.getChildren().add(subHamBurgerMenu);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(sideMenuTransitionTime),
                        //new KeyValue(menu.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(subHamBurgerMenu.translateXProperty(), 0, WEB_EASE)
                )
        );
        timeline.play();
    }

    public void closeSubMenu() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(250),
                        //new KeyValue(menu.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(subHamBurgerMenu.translateXProperty(), -320, WEB_EASE)
                )
        );
        timeline.play();
        timeline.setOnFinished((evt) -> {
            parent.getChildren().remove(subHamBurgerMenu);
        });
    }

    public void setRecentProjects(ArrayList<Collections> list) throws Exception {
        for (Collections project : list) {
            Label label = new Label(project.getName());
            label.getStyleClass().add("recent-project-label");
            label.setCursor(Cursor.HAND);

            if (getIdeController() == null) {
                throw new Exception("IDE controller not set");
            }

            label.setOnMouseClicked((MouseEvent evt) -> {
                getIdeController().loadProject(project);
            });

            recentProjectParent.getChildren().add(label);
        }
    }

    public IDEController getIdeController() {
        return ideController;
    }
}
