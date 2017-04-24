package ui.controllers;

import authenticate.entities.User;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import projectManager.CollectionFactory;
import projectManager.Collections;
import projectManager.Doc;
import projectManager.Type;
import projectManager.UnableToCreateException;
import ui.AceEditor;
import ui.util.GlobalSettings;
import ui.Main;
import ui.MyStage;
import ui.util.Message;
import ui.nodes.projectTree.ProjectTree;

public class IDEController implements Initializable {

    private MyStage stage;
    private AnchorPane menu, menuBack;
    private AnchorPane menu2, menuBack2;
    private AnchorPane menu3, menuBack3;
    private AnchorPane optionsPane, optionsPaneBack;

    private HamBurgerMenuController hamBurgerMenuController;
    private ShareController shareController;
    private VersionsController versionsController;

    private User user;

    @FXML
    private AnchorPane root;

    @FXML
    private Label newProjectLabel;

    @FXML
    private TreeView treeView;

    @FXML
    private TabPane tabPane;

    private ProjectTree projectTree;
    public static final Interpolator WEB_EASE = Interpolator.SPLINE(0.235, 1.000, 0.230, 0.975);
    private final int sideMenuTransitionTime = 500;

    public static Interpolator MY_EASE = new Interpolator() {
        private static final double S1 = -25.0 / 9.0;
        private static final double S2 = 50.0 / 9.0;
        private static final double S3 = -16.0 / 9.0;
        private static final double S4 = 10.0 / 9.0;

        @Override
        protected double curve(double t) {
            return clamp((t > 0.8) ? S1 * t * t + S2 * t + S3 : S4 * t);
        }

        @Override
        public String toString() {
            return "Interpolator.EASE_OUT";
        }

        double clamp(double t) {
            return (t < 0.0) ? 0.0 : (t > 1.0) ? 1.0 : t;
        }
    };

    private PopOver popup;
    private final AnchorPane newProjectPane = new AnchorPane();

    public static Font font12 = Font.loadFont(Main.class.getResourceAsStream("css/fonts/segoeui.ttf"), 13);
    public static Font font14 = Font.loadFont(Main.class.getResourceAsStream("css/fonts/segoeuil.ttf"), 14);
    public static Font font15 = Font.loadFont(Main.class.getResourceAsStream("css/fonts/segoeui.ttf"), 14);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Label l1 = new Label("New Project");
        //l1.setAlignment(Pos.CENTER);
        l1.setFont(font14);
        l1.setTextFill(Color.rgb(100, 100, 100));
        AnchorPane.setLeftAnchor(l1, 24d);
        AnchorPane.setTopAnchor(l1, 20d);
        AnchorPane.setRightAnchor(l1, 0d);

        TextField t1 = new TextField();
        t1.setPromptText("Enter name for new Project");
        t1.setMinWidth(100);
        AnchorPane.setLeftAnchor(t1, 5d);
        AnchorPane.setTopAnchor(t1, 10d);
        AnchorPane.setRightAnchor(t1, 5d);
        /*AnchorPane.setLeftAnchor(t1, 20d);
         AnchorPane.setTopAnchor(t1, 45d);
         AnchorPane.setRightAnchor(t1, 20d);*/

        Button b1 = new Button("Create");
        b1.setFont(font12);
        b1.getStyleClass().add("create-btn");
        b1.setMinWidth(80);

        b1.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent evt) {
                String name = t1.getText();
                
                if (name.length() == 0) {
                    return;
                }
                
                if (name.contains(" ") || name.matches(".*[^a-z0-9A-Z].*")) {
                    Message.showError("Incorrect Project Name", "Project Name cannot have spaces or special characters");
                    return;
                }
                
                String projectName = t1.getText();
                
                Collections collection = CollectionFactory.getCollection(user.getUserIdentifier());
                new Thread(() -> {
                    try {
                        Collections newProject = (Collections) collection.createNode(projectName, Type.COLLECTION);
                        Message.showInformation("New Project", "New project created successfully");
                        loadProject(newProject);
                    } catch (ConnectivityFailureException ex) {
                        Message.showError("New Project", ex.getMessage());
                        Logger.getLogger(IDEController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnableToCreateException ex) {
                        Message.showError("Error", ex.getMessage());
                        Logger.getLogger(IDEController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //System.out.println("Thread not ended, Add response.end()");
                }).start();
                
                t1.setText("");
            }
        });

        AnchorPane.setTopAnchor(b1, 40d);
        //AnchorPane.setTopAnchor(b1, 87d);
        AnchorPane.setRightAnchor(b1, 15d);
        AnchorPane.setBottomAnchor(b1, 15d);

        newProjectPane.setMinWidth(250);
        newProjectPane.getChildren().addAll(/*l1,*/t1, b1);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/HamBurgerMenu.fxml"));
        try {
            menu = fxmlLoader.load();
            setCache(menu, true);
            menuBack = new AnchorPane();

            hamBurgerMenuController = ((HamBurgerMenuController) fxmlLoader.getController());
            hamBurgerMenuController.setIDEControllerInstance(this);

            menuBack.setOpacity(0.001);
            AnchorPane.setLeftAnchor(menuBack, 0d);
            AnchorPane.setTopAnchor(menuBack, 0d);
            AnchorPane.setRightAnchor(menuBack, 0d);
            AnchorPane.setBottomAnchor(menuBack, 0d);

            menuBack.setOnMouseClicked((MouseEvent event) -> {
                closeHamburgerMenu();
            });

        } catch (IOException ex) {
            Logger.getLogger(IDEController.class.getName()).log(Level.SEVERE, null, ex);
        }

        fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Versions.fxml"));
        try {
            menu2 = fxmlLoader.load();
            setCache(menu2, true);
            menuBack2 = new AnchorPane();

            versionsController = ((VersionsController) fxmlLoader.getController());
            ((VersionsController) fxmlLoader.getController()).setIDEControllerInstance(this);

            menuBack2.setOpacity(0.001);
            AnchorPane.setLeftAnchor(menuBack2, 0d);
            AnchorPane.setTopAnchor(menuBack2, 0d);
            AnchorPane.setRightAnchor(menuBack2, 0d);
            AnchorPane.setBottomAnchor(menuBack2, 0d);

            menuBack2.setOnMouseClicked((MouseEvent event) -> {
                closeMenu2();
            });

        } catch (IOException ex) {
            Logger.getLogger(IDEController.class.getName()).log(Level.SEVERE, null, ex);
        }

        fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Share.fxml"));
        try {
            menu3 = fxmlLoader.load();
            setCache(menu3, true);
            shareController = ((ShareController) fxmlLoader.getController());

            menuBack3 = new AnchorPane();
            menuBack3.setOpacity(0.001);
            AnchorPane.setLeftAnchor(menuBack3, 0d);
            AnchorPane.setTopAnchor(menuBack3, 0d);
            AnchorPane.setRightAnchor(menuBack3, 0d);
            AnchorPane.setBottomAnchor(menuBack3, 0d);

            menuBack3.setOnMouseClicked((MouseEvent event) -> {
                closeMenu3();
            });

        } catch (IOException ex) {
            Logger.getLogger(IDEController.class.getName()).log(Level.SEVERE, null, ex);
        }

        optionsPane = new AnchorPane();
        optionsPaneBack = new AnchorPane();
        setAnchor(optionsPane, 150, 0, 0, 0);
        setAnchor(optionsPaneBack, 0, 0, 0, 0);
        optionsPaneBack.getChildren().add(optionsPane);
        optionsPane.getStyleClass().add("options-pane");
        optionsPaneBack.getStyleClass().add("options-pane-back");
        optionsPane.setOnMouseClicked((MouseEvent evt) -> {
            evt.consume();
        });

        optionsPaneBack.setOnMouseClicked((MouseEvent evt) -> {
            hideOptionsPane();
        });
        
        GlobalSettings settings = GlobalSettings.getInstance();
        if (settings.getProjectsPaneProperty().get()) {
            try {
                hamburgerClicked(null);
            } catch (Exception ex) {
                Logger.getLogger(IDEController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setStage(MyStage stage) {
        this.stage = stage;
    }

    public void hamburgerClicked(MouseEvent evt) throws Exception {
        //showOptionsPane();
        hamBurgerMenuController.closeSubMenu();
        AnchorPane.setLeftAnchor(menu, 0d);
        AnchorPane.setTopAnchor(menu, 0d);
        AnchorPane.setBottomAnchor(menu, 0d);

        DropShadow ds = new DropShadow();
        ds.setRadius(10);
        menu.setEffect(ds);
        menu.setTranslateX(-290);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(sideMenuTransitionTime),
                        new KeyValue(menu.translateXProperty(), 0, WEB_EASE)
                )
        );
        timeline.play();

        root.getChildren().add(menuBack);
        root.getChildren().remove(menu);
        root.getChildren().add(menu);
    }

    public void closeHamburgerMenu() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(200),
                        //new KeyValue(menu.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(menu.translateXProperty(), -330, WEB_EASE)
                )
        );
        timeline.play();

        root.getChildren().remove(menuBack);
    }

    public void showMenu2(User user, Collections project, Doc doc, AceEditor aceEditor) {
        versionsController.refresh(user, project, doc, aceEditor);
        AnchorPane.setRightAnchor(menu2, 0d);
        AnchorPane.setTopAnchor(menu2, 0d);
        AnchorPane.setBottomAnchor(menu2, 0d);

        DropShadow ds = new DropShadow();
        ds.setRadius(10);
        menu2.setEffect(ds);
        menu2.setTranslateX(320);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(sideMenuTransitionTime),
                        new KeyValue(menu2.translateXProperty(), 0, WEB_EASE)
                )
        );
        timeline.play();

        root.getChildren().add(menuBack2);
        root.getChildren().remove(menu2);
        root.getChildren().add(menu2);
    }

    public void closeMenu2() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(200),
                        //new KeyValue(menu.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(menu2.translateXProperty(), 330, WEB_EASE)
                )
        );
        timeline.play();

        root.getChildren().remove(menuBack2);
    }

    public void showMenu3(Collections project) {
        getShareController().refresh(user, project);

        AnchorPane.setRightAnchor(menu3, 0d);
        AnchorPane.setTopAnchor(menu3, 0d);
        AnchorPane.setBottomAnchor(menu3, 0d);

        DropShadow ds = new DropShadow();
        ds.setRadius(10);
        menu3.setEffect(ds);
        menu3.setTranslateX(320);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(sideMenuTransitionTime),
                        new KeyValue(menu3.translateXProperty(), 0, WEB_EASE)
                )
        );
        timeline.play();

        root.getChildren().add(menuBack3);
        root.getChildren().remove(menu3);
        root.getChildren().add(menu3);
    }

    public void closeMenu3() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(200),
                        //new KeyValue(menu.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(menu3.translateXProperty(), 330, WEB_EASE)
                )
        );
        timeline.play();

        root.getChildren().remove(menuBack3);
    }

    public void showOptionsPane() throws Exception {
        optionsPaneBack.getChildren().clear();
        optionsPane.getChildren().clear();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Login.fxml"));
        AnchorPane pane = fxmlLoader.load();
        AnchorPane.setLeftAnchor(pane, 0d);
        AnchorPane.setTopAnchor(pane, 0d);
        AnchorPane.setRightAnchor(pane, 0d);
        AnchorPane.setBottomAnchor(pane, 0d);

        optionsPane.getChildren().add(pane);
        optionsPaneBack.getChildren().add(optionsPane);

        optionsPaneBack.setOpacity(0);
        optionsPane.setOpacity(0);
        optionsPane.setTranslateX(50);

        setCache(optionsPane, true);
        setCache(optionsPaneBack, true);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new KeyValue(optionsPaneBack.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(optionsPane.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(optionsPane.translateXProperty(), 0, WEB_EASE)
                )
        );
        timeline.setOnFinished((ActionEvent event) -> {
            setCache(optionsPane, false);
            setCache(optionsPaneBack, false);
        });
        timeline.play();

        stage.setColorMode(MyStage.ColorMode.BLACK);
        root.getChildren().add(optionsPaneBack);
    }

    public void hideOptionsPane() {
        setCache(optionsPane, true);
        setCache(optionsPaneBack, true);

        Timeline timeline = new Timeline(72,
                new KeyFrame(Duration.millis(200),
                        new KeyValue(optionsPaneBack.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(optionsPane.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(optionsPane.translateXProperty(), 40, WEB_EASE)
                )
        );
        timeline.setOnFinished((ActionEvent event) -> {
            setCache(optionsPane, false);
            setCache(optionsPaneBack, false);

            root.getChildren().remove(optionsPaneBack);
        });
        timeline.play();

        stage.setColorMode(MyStage.ColorMode.WHITE);
    }

    public void loadProject(Collections project) {
        /*TreeItem rootItem = new DocTreeItem(project.getName(), user,
         new ImageView(new Image(Main.class.getResourceAsStream("css/images/folder-small.png"))), tabPane);
         rootItem.setExpanded(true);

         ArrayList<project_manager.Node> nodeList = project.getContent();
         for (project_manager.Node node : nodeList) {
         System.out.println("name:" + node.getName());
         System.out.println("path:" + node.getPath());
         if (node.getType() == Type.COLLECTION) {
         Collections subCollection = (Collections) node;
         subCollection.getContent();
         } else if (node.getType() == Type.DOC) {
         Doc doc = (Doc) node;
         System.out.println(doc.getIdentifier());

         TreeItem item = new DocTreeItem(doc.getIdentifier(), user, new ImageView(new Image(Main.class.getResourceAsStream("css/images/file-small.png"))), tabPane);
         rootItem.getChildren().add(item);
         }
         }*/

        /*for (int i = 1; i < 6; i++) {
         TreeItem item = new MyTreeItem("a" + i + ".cpp", new ImageView(new Image(Main.class.getResourceAsStream("css/images/file-small.png"))), tabPane);
         rootItem.getChildren().add(item);
         }*/
        /*
         treeView.setOnKeyPressed(new EventHandler<KeyEvent>() {

         @Override
         public void handle(KeyEvent event) {
         if (event.getCode() == KeyCode.ENTER) {
         DocTreeItem selected = (DocTreeItem) treeView.getSelectionModel().getSelectedItems().get(0);

         selected.loadFile();
         }
         }
         });
         treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {

         @Override
         public void handle(MouseEvent event) {
         if (event.getClickCount() == 2) {
         DocTreeItem selected = (DocTreeItem) treeView.getSelectionModel().getSelectedItems().get(0);

         selected.loadFile();
         }
         }
         });
         treeView.setRoot(rootItem);

         treeView.setVisible(true);*/
        projectTree.loadProject(project);
    }

    public void setCache(AnchorPane node, Boolean b) {
        node.setCache(b);
        node.setCacheShape(b);
        if (b) {
            node.setCacheHint(CacheHint.SPEED);
        } else {
            node.setCacheHint(CacheHint.DEFAULT);
        }
    }

    public void setAnchor(Node node, double left, double top, double right, double bottom) {
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setBottomAnchor(node, bottom);
    }

    public void init(MyStage stage, User user) {
        this.stage = stage;
        
        this.user = user;
        projectTree = new ProjectTree(user, treeView, tabPane, this);
        hamBurgerMenuController.setUser(user);
        treeView.setVisible(false);
    }
    
    public void setUser(User user) {
        this.user = user;
        projectTree = new ProjectTree(user, treeView, tabPane, this);
        hamBurgerMenuController.setUser(user);
        treeView.setVisible(false);
    }

    public void newProjectLabelClicked(MouseEvent evt) {
        if (popup != null && popup.isShowing()) {
            popup.hide();
            return;
        }

        if (popup == null) {
            popup = new PopOver(newProjectPane);
            popup.setStyle("-fx-effect: dropshadow( gaussian , rgba(0, 0, 0, 0.4) , 0,0,1,1 );");
            //popup.getScene().getStylesheets().add("ui/css/IDEStyle.css");
        }
        //popup.setDetachedTitle("Notifications");
        popup.setDetachable(false);
        //popup.setArrowIndent(10);
        popup.setCornerRadius(3);
        popup.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
        popup.setAutoHide(true);

        popup.show(newProjectLabel);
        ((Parent) popup.getSkin().getNode()).getStylesheets().add("ui/css/IDEStyle.css");
        popup.show(newProjectLabel);
    }

    public ShareController getShareController() {
        return shareController;
    }

}
