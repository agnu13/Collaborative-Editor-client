/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ui.util.GlobalSettings;
import ui.nodes.toggle.ToggleControl;
import ui.nodes.toggle.ToggleState;

/**
 * FXML Controller class
 *
 * @author Ashish
 */
public class SettingsController implements Initializable {

    @FXML
    private Label back;

    @FXML
    private AnchorPane contentParent, toggleParent1, toggleParent2, toggleParent3, toggleParent4;
    
    private HamBurgerMenuController hamBurgerMenuController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GlobalSettings settings = GlobalSettings.getInstance();
        
        boolean b1 = settings.getShowNotificationsProperty()  .get();
        boolean b2 = settings.getNotificationsSoundProperty() .get();
        boolean b3 = settings.getCursorNameProperty()         .get();
        boolean b4 = settings.getProjectsPaneProperty()       .get();
        
        ToggleControl toggle1 = new ToggleControl((b1) ? ToggleState.ON : ToggleState.OFF);
        ToggleControl toggle2 = new ToggleControl((b2) ? ToggleState.ON : ToggleState.OFF);
        ToggleControl toggle3 = new ToggleControl((b3) ? ToggleState.ON : ToggleState.OFF);
        ToggleControl toggle4 = new ToggleControl((b4) ? ToggleState.ON : ToggleState.OFF);
        
        settings.getShowNotificationsProperty()  .bind(toggle1.getToggleProperty());
        settings.getNotificationsSoundProperty() .bind(toggle2.getToggleProperty());
        settings.getCursorNameProperty()         .bind(toggle3.getToggleProperty());
        settings.getProjectsPaneProperty()       .bind(toggle4.getToggleProperty());
        
        toggleParent1.getChildren().add(toggle1);
        toggleParent2.getChildren().add(toggle2);
        toggleParent3.getChildren().add(toggle3);
        toggleParent4.getChildren().add(toggle4);
    }

    void setHamBurderMenuControllerInstance(HamBurgerMenuController controller) {
        hamBurgerMenuController = controller;
    }

    @FXML
    private void backClicked() {
        hamBurgerMenuController.closeSubMenu();
    }

}
