/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.nodes.projectTree;

import authenticate.entities.User;
import exception.ConnectivityFailureException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class ProjectTreeItem extends NodeTreeItem {

    protected Collections project;
    protected User user;
    protected Closer closer;
    protected TabPane tabPane;
    protected IDEController ideController;

    public ProjectTreeItem(Collections project, User user, Closer closer, TabPane tabPane, IDEController ideController) {
        super(project.getName());
        this.closer = closer;
        this.project = project;
        this.user = user;
        this.tabPane = tabPane;
        this.ideController = ideController;

        treeItemType = TreeItemType.PROJECT;

        Label lbl = new Label("", new ImageView(new Image(Main.class.getResourceAsStream("css/images/folder-small.png"))));
        this.setGraphic(lbl);

        ArrayList<projectManager.Node> nodeList = null;
        try {
            nodeList = project.getContent();
        } catch (ConnectivityFailureException ex) {
            Message.showError("Connectivity Error", ex.getMessage());
            Logger.getLogger(ProjectTreeItem.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (projectManager.Node node : nodeList) {
            //System.out.println("name:" + node.getName());
            //System.out.println("path:" + node.getPath());

            if (node.getType() == Type.COLLECTION) {
                Collections subCollection = (Collections) node;
                
                //FolderTreeItem item = new FolderTreeItem(subCollection, user, closer, tabPane, ideController);
                //this.getChildren().add(item);
            } else if (node.getType() == Type.DOC) {
                Doc doc = (Doc) node;
                
                TreeItem item = new DocTreeItem(project, doc, user, tabPane, ideController);
                this.getChildren().add(item);
            }
        }
    }

    public Closer getCloser() {
        return closer;
    }

    public Collections getProject() {
        return project;
    }

    @Override
    public ContextMenu getContextMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem menuItem1 = new MenuItem("  Close Project  ");
        //MenuItem menuItem2 = new MenuItem("  Delete Project  ");
        MenuItem menuItem3 = new MenuItem("  New File  ");
        MenuItem menuItem5 = new MenuItem("  New Package   ");
        MenuItem menuItem4 = new MenuItem("  Share Project   ");

        //menuItem2.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));

        menu.getItems().addAll(menuItem1, /*menuItem2,*/ menuItem3, menuItem5, menuItem4);

        menuItem1.setOnAction((evt) -> {
            for (Object treeItem : ProjectTreeItem.this.getChildren()) {
                if (((NodeTreeItem) treeItem).getTreeItemType() == TreeItemType.DOC) {
                    ((DocTreeItem) treeItem).closeFile();
                }
            }
            closer.closeProject(project);
        });

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

        menuItem4.setOnAction(evt -> {
            ideController.showMenu3(project);
            //ideController.getShareController().setUser(user);
        });

        menu.setOnShowing((evt) -> {
            menu.getScene().getRoot().setOpacity(0);
            //menu.getScene().getRoot().setScaleY(0);
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
