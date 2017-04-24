/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.nodes.projectTree;

import authenticate.entities.User;
import com.fxexperience.javafx.animation.FadeInDownTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.util.Duration;
import projectManager.Collections;
import projectManager.Doc;
import ui.Main;
import ui.controllers.IDEController;
import ui.nodes.DocumentTab;

/**
 *
 * @author Ashish
 */
public class DocTreeItem extends NodeTreeItem {

    private Collections project;
    private Doc file;
    private TabPane tabPane;
    private boolean fileLoaded;
    private DocumentTab tab;
    private User user;
    
    private IDEController ideController;

    public DocTreeItem(Collections project, Doc doc, User user, TabPane tabPane, IDEController ideController) {
        super(doc.getName());

        treeItemType = TreeItemType.DOC;
        this.ideController = ideController;

        Label lbl = new Label("", new ImageView(new Image(Main.class.getResourceAsStream("css/images/file-small.png"))));
        this.setGraphic(lbl);
        this.tabPane = tabPane;
        this.project = project;
        this.user = user;
        file = doc;
    }

    public void openFile() {
        if (fileLoaded) {
            tabPane.getSelectionModel().select(tab);
            return;
        }

        fileLoaded = true;
        tab = new DocumentTab(project, file, user, tabPane, ideController);
        tab.setOnClosed(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                fileLoaded = false;
            }
        });
    }

    public void closeFile() {
        if (tab == null) {
            return;
        }
        tab.closeRequest();
        tabPane.getTabs().remove(tab);
    }

    @Override
    public ContextMenu getContextMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem menuItem1 = new MenuItem("  Open File  ");
        //MenuItem menuItem2 = new MenuItem("  Delete File  ");
        //MenuItem menuItem3 = new MenuItem("  Run File  ");

        //menuItem2.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
        //menuItem3.setAccelerator(new KeyCodeCombination(KeyCode.F5));

        menu.getItems().addAll(menuItem1/*, menuItem2*//*, menuItem3*/);

        menuItem1.setOnAction((ActionEvent) -> {
            openFile();
        });

        /*menu.setOnShowing((evt) -> {
            menu.getScene().getRoot().setOpacity(0);
            menu.getScene().getRoot().setScaleY(0);
            menu.getScene().getRoot().setTranslateY(-100);
            
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(200),
                            new KeyValue(
                                    menu.getScene().getRoot().scaleYProperty(), 1,
                                    IDEController.WEB_EASE
                            ),
                            new KeyValue(
                                    menu.getScene().getRoot().opacityProperty(), 1,
                                    IDEController.WEB_EASE
                            ),
                            new KeyValue(
                                    menu.getScene().getRoot().translateYProperty(), 0,
                                    IDEController.WEB_EASE
                            )
                    )
            );
            timeline.play();
        });*/
        menu.setOnShowing((evt) -> {
            menu.getScene().getRoot().setOpacity(0);
            FadeInDownTransition ff = new FadeInDownTransition(menu.getScene().getRoot());
            ff.setDelay(Duration.ZERO);
            ff.setRate(4);
            ff.play();
        });

        menu.getStyleClass().add("context-menu1");
        return menu;
    }
}
