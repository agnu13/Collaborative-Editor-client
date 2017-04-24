/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.util;

import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.Main;

/**
 *
 * @author Ashish
 */
public class Message {

    public static void showInformation(String title, String msg) {
        /*Dialogs.create()
         .styleClass(Dialog.STYLE_CLASS_CROSS_PLATFORM)
         .title(title)
         .message(msg)
         .actions(Dialog.ACTION_OK)
         .showInformation();*/

        Platform.runLater(() -> {
            MyNotification.show(title, msg);
        });

    }

    public static void showError(String title, String msg) {
        /*Dialogs.create()
         .styleClass(Dialog.STYLE_CLASS_CROSS_PLATFORM)
         .title(title)
         .message(msg)
         .actions(Dialog.ACTION_OK)
         .showError();*/
        Platform.runLater(() -> {
            MyNotification.show(title, msg);
        });
    }

    public static boolean showConfirm(String title, String message) {
        /*Action result = Dialogs.create()
                .styleClass(Dialog.STYLE_CLASS_CROSS_PLATFORM)
                .title(title)
                .message(message)
                .actions(Dialog.ACTION_OK, Dialog.ACTION_CANCEL)
                .showConfirm();*/

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("css/images/icon.png")));

        Optional<ButtonType> op = alert.showAndWait();
        
        if (op.isPresent() && op.get() == ButtonType.OK) {
            return true;
        }

        //return result == Dialog.ACTION_OK;
        return false;
    }
}
