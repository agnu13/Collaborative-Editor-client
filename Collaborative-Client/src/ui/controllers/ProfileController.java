/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controllers;

import authenticate.entities.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Ashish
 */
public class ProfileController implements Initializable {

    User user;
    
    @FXML
    private Label back, userNameLabel, nameLabel, emailLabel;

    private HamBurgerMenuController hamBurgerMenuController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void setHamBurderMenuControllerInstance(HamBurgerMenuController controller) {
        hamBurgerMenuController = controller;
    }

    @FXML
    private void backClicked() {
        hamBurgerMenuController.closeSubMenu();
    }

    public void setUser(User user) {
        this.user = user;
        userNameLabel.setText("Hi " + user.getUsername() + "!");
        nameLabel.setText(user.getName());
        emailLabel.setText(user.getEmailId());
    }
}
