/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controllers;

import authenticate.entities.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import projectManager.Collections;
import projectManager.Doc;
import ui.AceEditor;
import ui.util.Message;

/**
 * FXML Controller class
 *
 * @author Ashish
 */
public class VersionsController implements Initializable {

    /*@FXML
     private Label back;*/
    @FXML
    private VBox versionsParent;

    //private ArrayList<Label> versionsList;
    private IDEController ideController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void backClicked(MouseEvent evt) {
        ideController.closeMenu2();;
    }

    public void setIDEControllerInstance(IDEController controller) {
        this.ideController = controller;
    }

    public void reload(ArrayList<String> list) {
        //versionsList = new ArrayList<>();
        versionsParent.getChildren().clear();

        for (int i = 0; i < 5; i++) {
            Label label = new Label("Version" + i);
            label.getStyleClass().add("recent-project-label");
            //versionsList.add(label);

            label.setTooltip(new Tooltip("Restore Version: " + "Version" + i));
            label.setCursor(Cursor.HAND);
            label.setOnMouseClicked((evt) -> {
                // restore Version
                // IDEController.restoreVersion(version);
            });

            versionsParent.getChildren().add(label);
        }

    }

    public void refresh(User user, Collections project, Doc doc, AceEditor aceEditor) {
        versionsParent.getChildren().clear();

        //String path = "Commits/" + user.getUsername() + "/" + project.getName() + "/" + doc.getName();
        StringBuilder path2 = new StringBuilder("Commits");

        String arr[] = doc.getPath().split("\\.");
        for (String dir : arr) {
            if (dir.length() == 0) {
                continue;
            }

            path2.append("/").append(dir);
        }
        path2.append("/").append(doc.getName());

        String path = path2.toString();

        File f = new File(path);

        if (!f.exists()) {
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));

                Platform.runLater(() -> {
                    for (File file : files) {
                        String name = file.getName();
                        Label label = new Label(name);
                        label.getStyleClass().add("recent-project-label");
                        //versionsList.add(label);

                        label.setTooltip(new Tooltip("Restore Version: " + name));
                        label.setCursor(Cursor.HAND);
                        label.setOnMouseClicked((evt) -> {
                            /*Alert alert = new Alert(AlertType.CONFIRMATION);
                             alert.getButtonTypes().clear();
                             alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
                             alert.getDialogPane().setMaxHeight(20);
                             Stage stage = new Stage();
                             stage.initStyle(StageStyle.UTILITY);
                             stage.setScene(new Scene(new Group(), 400, 300));
                             Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                             stage.setX(primScreenBounds.getWidth() / 2 - 150);
                             stage.setY(primScreenBounds.getHeight() / 2 - 200);

                             //stage.setX(alert.getOwner().getX());
                             //stage.setY(alert.getOwner().getY());
                             stage.getIcons().add(new Image(Main.class.getResourceAsStream("css/images/icon-small.png")));
                             alert.initOwner(stage);
                             //alert.initStyle(StageStyle.TRANSPARENT);

                             alert.getDialogPane().getStyleClass().add("dialog-pane1");
                             alert.getDialogPane().getStylesheets().add("ui/css/IDEStyle.css");
                             //alert.setTitle("Restore Version");
                             alert.setHeaderText("   Are you sure to restore version: " + file.getName());
                             //alert.setContentText("Are you sure to restore version: " + file.getName());

                             Optional<ButtonType> result = alert.showAndWait();
                             if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                             aceEditor.setText(read(file));
                             }*/

                            boolean bool = Message.showConfirm("Restore Version", "Are you sure to restore version - " + file.getName());

                            if (bool) {
                                aceEditor.setText(read(file));
                            }
                        });

                        versionsParent.getChildren().add(label);
                    }
                });

            }
        }).start();
    }

    public String read(File f) {
        try {
            FileInputStream inpStream = new FileInputStream(f);

            byte[] bytes = new byte[(int) f.length()];
            inpStream.read(bytes);

            return new String(bytes);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VersionsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VersionsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }
}
