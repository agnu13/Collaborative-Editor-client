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
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import projectManager.CollectionFactory;
import projectManager.Collections;
import projectManager.Node;
import projectManager.Type;
import share.Share;
import ui.util.Message;

/**
 * FXML Controller class
 *
 * @author Ashish
 */
public class ProjectsController implements Initializable {

    @FXML
    private Label back;

    @FXML
    ToggleButton tb1, tb2;

    @FXML
    private AnchorPane projectsPane, sharedPane;

    @FXML
    private VBox projectsParent, sharedParent;

    private HamBurgerMenuController hamBurgerMenuController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup group = new ToggleGroup();
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
                return;
            }

            projectsPane.setVisible(false);
            sharedPane.setVisible(false);

            if (newValue == tb1) {
                //System.out.println("tab1");
                projectsPane.setVisible(true);
            } else {
                //System.out.println("tab2");
                sharedPane.setVisible(true);
            }
        });

        tb1.setToggleGroup(group);
        tb2.setToggleGroup(group);
    }

    void setHamBurderMenuControllerInstance(HamBurgerMenuController controller) {
        this.hamBurgerMenuController = controller;

        //NotificationNode node = new NotificationNode(null, hamBurgerMenuController.getIdeController());
        //NotificationNode node2 = new NotificationNode(null, hamBurgerMenuController.getIdeController());
        //sharedParent.getChildren().addAll(node, node2);
        /*{
         Label label = new Label("project1");
         label.getStyleClass().add("recent-project-label");
         label.setCursor(Cursor.HAND);

         projectsParent.getChildren().add(label);
         }
         {
         Label label = new Label("project2");
         label.getStyleClass().add("recent-project-label");
         label.setCursor(Cursor.HAND);

         projectsParent.getChildren().add(label);
         }

         // retreve list
         ArrayList<Collections> list = null;*/

        /*for (Collections project : list) {
         Label label = new Label(project.getName());
         label.getStyleClass().add("recent-project-label");
         label.setCursor(Cursor.HAND);


         label.setOnMouseClicked((MouseEvent evt) -> {
         hamBurgerMenuController.getIdeController().loadProject(project);
         });

         projectsParent.getChildren().add(label);
         }*/
    }

    @FXML
    private void backClicked() {
        hamBurgerMenuController.closeSubMenu();
    }

    public void refresh(User user) {
        projectsParent.getChildren().clear();
        sharedParent.getChildren().clear();
        new Thread(() -> {
            Collections collection = CollectionFactory.getCollection(user.getUsername());
            ArrayList<Node> nodeList = null;
            try {
                nodeList = collection.getContent();
            } catch (ConnectivityFailureException ex) {
                Message.showError("Connectivity Error", ex.getMessage());
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }

            projectRefreshDoneApplyNow(nodeList);
        }).start();
        
        new Thread(() -> {
            ArrayList<Collections> nodeList = null;
            
            try {
                nodeList = Share.getSharedProjects(user.getUserIdentifier());
                sharedProjectRefreshDoneApplyNow(nodeList);
            } catch (ConnectivityFailureException ex) {
                Message.showError("Connectivity Error", ex.getMessage());
                Logger.getLogger(ProjectsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    private void projectRefreshDoneApplyNow(ArrayList<Node> list) {
        Platform.runLater(() -> {
            for (Node node : list) {
                if (node.getType() == Type.DOC) {
                    continue;
                }

                Collections project = (Collections) node;

                Label label = new Label(project.getName());
                label.getStyleClass().add("recent-project-label");
                label.setCursor(Cursor.HAND);

                label.setOnMouseClicked((evt) -> {
                    hamBurgerMenuController.getIdeController().loadProject(project);
                    hamBurgerMenuController.getIdeController().closeHamburgerMenu();
                });

                projectsParent.getChildren().add(label);
            }
        });
    }
    
    private void sharedProjectRefreshDoneApplyNow(ArrayList<Collections> list) {
        Platform.runLater(() -> {
            for (Node node : list) {
                if (node.getType() == Type.DOC) {
                    continue;
                }

                Collections project = (Collections) node;

                Label label = new Label(project.getName());
                label.getStyleClass().add("recent-project-label");
                label.setCursor(Cursor.HAND);

                label.setOnMouseClicked((evt) -> {
                    hamBurgerMenuController.getIdeController().loadProject(project);
                    hamBurgerMenuController.getIdeController().closeHamburgerMenu();
                });

                sharedParent.getChildren().add(label);
            }
        });
    }
}
