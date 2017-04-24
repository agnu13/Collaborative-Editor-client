/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.nodes.projectTree;

import authenticate.entities.User;
import exception.ConnectivityFailureException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import projectManager.Collections;
import projectManager.Doc;
import projectManager.Type;
import projectManager.UnableToCreateException;
import ui.Main;
import ui.controllers.IDEController;
import ui.util.Message;

/**
 *
 * @author Ashish
 */
public class FolderTreeItem extends ProjectTreeItem {

    public FolderTreeItem(Collections project, User user, Closer closer, TabPane tabPane, IDEController ideController) {
        super(project, user, closer, tabPane, ideController);
    }

    @Override
    public ContextMenu getContextMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem menuItem3 = new MenuItem("  New File  ");
        MenuItem menuItem5 = new MenuItem("  New Package   ");
        menu.getItems().addAll(menuItem3, menuItem5);

        menuItem3.setOnAction((evt) -> {
            /*Optional<String> result = Dialogs.create()
                    .styleClass(Dialog.STYLE_CLASS_CROSS_PLATFORM)
                    .title("New File")
                    .message("Enter name for new file")
                    .showTextInput();*/
            
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New File");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter name for new File");
            ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("css/images/icon.png")));

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(val -> {
                new Thread(() -> {
                    try {
                        Doc doc = (Doc) project.createNode(val, Type.DOC);

                        TreeItem item = new DocTreeItem(project, doc, user, tabPane, ideController);
                        this.getChildren().add(item);
                    } catch (ConnectivityFailureException ex) {
                        Message.showError("Connectivity Error", ex.getMessage());
                        Logger.getLogger(ProjectTreeItem.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnableToCreateException ex) {
                        Message.showError("Error", ex.getMessage());
                        Logger.getLogger(ProjectTreeItem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();
            });
        });

        menuItem5.setOnAction((evt) -> {
            /*Optional<String> result = Dialogs.create()
                    .styleClass(Dialog.STYLE_CLASS_CROSS_PLATFORM)
                    .title("New Package")
                    .message("Enter name for new package")
                    .showTextInput();*/

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New Package");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter name for new Package");
            ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("css/images/icon.png")));

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(val -> {
                new Thread(() -> {
                    try {
                        Collections collection = (Collections) project.createNode(val, Type.COLLECTION);

                        FolderTreeItem item = new FolderTreeItem(collection, user, closer, tabPane, ideController);
                        this.getChildren().add(item);
                    } catch (ConnectivityFailureException ex) {
                        Message.showError("Connectivity Error", ex.getMessage());
                        Logger.getLogger(ProjectTreeItem.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnableToCreateException ex) {
                        Message.showError("Error", ex.getMessage());
                        Logger.getLogger(ProjectTreeItem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();
            });
        });

        menu.setOnShowing((evt) -> {
            menu.getScene().getRoot().setOpacity(0);
            menu.getScene().getRoot().setTranslateY(-300);

            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(400),
                            new KeyValue(
                                    menu.getScene().getRoot().translateYProperty(), 0,
                                    IDEController.WEB_EASE
                            ),
                            new KeyValue(
                                    menu.getScene().getRoot().opacityProperty(), 1,
                                    IDEController.WEB_EASE
                            )
                    )
            );
            timeline.play();
        });

        menu.getStyleClass().add("context-menu1");
        return menu;
    }
}
