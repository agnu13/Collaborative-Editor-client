/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Ashish
 */
public class MyNotiController implements Initializable {

    @FXML
    Label title, content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void init(String t1, String t2) {
        title.setText(t1);
        content.setText(t2);
    }

    @FXML
    private void closeClicked(MouseEvent evt) {
        Window stage = title.getScene().getWindow();

        Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(500),
                ev -> {
                    stage.hide();
                },
                new KeyValue(stage.getScene().getRoot().opacityProperty(), 0, IDEController.WEB_EASE)));
        timeline2.play();
    }
}
