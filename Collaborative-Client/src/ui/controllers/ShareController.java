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
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;
import projectManager.Collections;
import share.Share;
import ui.util.Message;
import ui.util.MyNotification;

/**
 * FXML Controller class
 *
 * @author Ashish
 */
public class ShareController implements Initializable {

    @FXML
    private TextField userNameTxt;

    private Collections project;
    private HashMap<String, User> userNameMap = new HashMap<>();

    private User user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void shareClicked() {
        String userName = userNameTxt.getText();
        User anotherUser = userNameMap.get(userName);

        if (anotherUser == null) {
            MyNotification.show("Invalid user name", "No user exists with user name : " + userName);
            System.err.println("No user exists with user name: " + userName);
            return;
        }

        ArrayList<String> userList = new ArrayList<>();
        userList.add(anotherUser.getUserIdentifier());

        new Thread(() -> {
            try {
                //System.out.println("i m sharing: " + user.getUserIdentifier() + " with " + userList.get(0));
                
                Share.shareWith(user.getUserIdentifier(), project.getPath() + "." + project.getName(), userList);

                Platform.runLater(() -> {
                    userNameTxt.clear();
                    Message.showInformation("Share", "File Successfully shared");
                });
            } catch (ConnectivityFailureException ex) {
                //Message.showError("Share Error", ex.getMessage());
                
                Platform.runLater(() -> {
                    MyNotification.show("Cannot Share", "Error during sharing project");
                });
            }
        }).start();
    }

    public void refresh(User user, Collections project) {
        this.user = user;
        this.project = project;
        new Thread(() -> {
            try {
                ArrayList<User> list = Share.getUsers();
                ArrayList<String> names = new ArrayList<>();

                for (User u : list) {
                    //System.out.println(u.getUsername() + ":" + u.getUserIdentifier());
                    names.add(u.getUsername());
                    userNameMap.put(u.getUsername(), u);
                }

                Platform.runLater(() -> {
                    TextFields.bindAutoCompletion(userNameTxt, names);
                    userNameTxt.requestFocus();
                });
            } catch (ConnectivityFailureException ex) {
                Message.showError("Connectivity Error", ex.getMessage());
                Logger.getLogger(ShareController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}
