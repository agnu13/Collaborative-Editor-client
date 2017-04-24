package ui.nodes;

import ui.util.Message;
import ui.util.MyNotification;
import authenticate.entities.User;
import codeEditor.sessionLayer.Session;
import com.fxexperience.javafx.animation.FadeInTransition;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import org.controlsfx.control.PopOver;
import projectManager.Collections;
import projectManager.Doc;
import ui.AceEditor;
import ui.Main;
import ui.controllers.IDEController;
import compile.Compiler;

public final class DocumentTab extends Tab {

    private AnchorPane content;
    private AnchorPane toolBar;
    private AceEditor aceEditor;
    private TabPane tabPane;
    private ImageView loadingView;
    private StackPane gifParent = new StackPane();
    private User user;
    private Doc doc;
    private Collections project;
    private Label down;

    private VBox sharedUserVBox = new VBox();

    double toolBarHeight = 44;

    public DocumentTab(Collections project, Doc doc, User user, TabPane tabPane, IDEController ideController) {
        super(doc.getName() + "  ");

        this.project = project;
        this.doc = doc;
        this.user = user;
        //String code = readFile(file);

        toolBar = new AnchorPane();
        content = new AnchorPane();

        toolBar.getStyleClass().add("toolBar");
        toolBar.setPrefHeight(toolBarHeight);

        AnchorPane.setLeftAnchor(toolBar, 0d);
        AnchorPane.setTopAnchor(toolBar, 0d);
        AnchorPane.setRightAnchor(toolBar, 0d);

        Label compileLbl = new Label(/*"   ▶   "*/"", new ImageView(new Image(Main.class.getResourceAsStream("css/images/compile.png"))));
        compileLbl.getStyleClass().addAll("compile-lbl", "tool");
        compileLbl.setFont(new Font(16));
        compileLbl.setAlignment(Pos.CENTER);
        compileLbl.setPrefWidth(toolBarHeight);
        compileLbl.setTooltip(new Tooltip("Compile and Run"));
        AnchorPane.setLeftAnchor(compileLbl, 5d);
        AnchorPane.setTopAnchor(compileLbl, 0d);
        AnchorPane.setBottomAnchor(compileLbl, 0d);
        compileLbl.setOnMouseClicked(evt -> {
            compileLbl.setDisable(true);
            loadingView.setVisible(true);
            File file = new File("tmp");
            file.mkdirs();

            String path = file.getAbsolutePath();

            try (PrintWriter out = new PrintWriter("tmp/a.cpp")) {
                out.print(aceEditor.getText());
                out.flush();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DocumentTab.class.getName()).log(Level.SEVERE, null, ex);
            }

            Compiler c = new Compiler();
            c.setFilePath(path);
            c.setFileName("a.cpp");

            new Thread(() -> {
                //c.compile();
                boolean flag = c.checkCompilation();
                
                Platform.runLater(() -> {
                    compileLbl.setDisable(false);
                    loadingView.setVisible(false);
                });
                
                if (flag) {
                    c.performExecution();
                } else {
                    c.showCompilationError();
                }
            }).start();
        });

        loadingView = new ImageView(new Image(Main.class.getResourceAsStream("css/images/728.GIF")));
        loadingView.setVisible(false);
        AnchorPane.setLeftAnchor(loadingView, toolBarHeight + 5);
        AnchorPane.setTopAnchor(loadingView, 8d);
        AnchorPane.setBottomAnchor(loadingView, 0d);
        loadingView.setFitHeight(toolBarHeight - 15);
        loadingView.setFitWidth(toolBarHeight - 15);

        Label compileRunLbl = new Label(/*"   ▶   "*/"", new ImageView(new Image(Main.class.getResourceAsStream("css/images/compile-run.png"))));
        compileRunLbl.getStyleClass().addAll("compile-lbl", "tool");
        compileRunLbl.setFont(new Font(16));
        compileRunLbl.setAlignment(Pos.CENTER);
        compileRunLbl.setPrefWidth(toolBarHeight);
        compileRunLbl.setTooltip(new Tooltip("Compile and Run"));
        AnchorPane.setLeftAnchor(compileRunLbl, 5d + toolBarHeight);
        AnchorPane.setTopAnchor(compileRunLbl, 0d);
        AnchorPane.setBottomAnchor(compileRunLbl, 0d);

        ImageView icon0 = new ImageView(new Image(Main.class.getResourceAsStream("css/images/down.png")));
        down = new Label("", icon0);
        //down.setDisable(true);
        down = new Label("", icon0);
        down.setFont(new Font(16));
        down.setAlignment(Pos.CENTER);
        down.setTooltip(new Tooltip("People editing this document"));
        down.getStyleClass().addAll("compile-lbl", "tool");
        down.setPrefWidth(toolBarHeight);
        AnchorPane.setTopAnchor(down, 0d);
        AnchorPane.setRightAnchor(down, 3 * toolBarHeight);
        AnchorPane.setBottomAnchor(down, 0d);

        ImageView icon1 = new ImageView(new Image(Main.class.getResourceAsStream("css/images/commit.png")));
        Label commit = new Label("", icon1);
        commit.setFont(new Font(16));
        commit.setAlignment(Pos.CENTER);
        commit.setTooltip(new Tooltip("Commit"));
        commit.getStyleClass().addAll("compile-lbl", "tool");
        commit.setPrefWidth(toolBarHeight);
        AnchorPane.setTopAnchor(commit, 0d);
        AnchorPane.setRightAnchor(commit, 2 * toolBarHeight);
        AnchorPane.setBottomAnchor(commit, 0d);

        ImageView icon2 = new ImageView(new Image(Main.class.getResourceAsStream("css/images/Share-128.png")));
        Label shareLabel = new Label("", icon2);
        shareLabel.setFont(new Font(16));
        shareLabel.setAlignment(Pos.CENTER);
        shareLabel.setTooltip(new Tooltip("Share"));
        shareLabel.getStyleClass().addAll("compile-lbl", "tool");
        shareLabel.setPrefWidth(toolBarHeight);
        AnchorPane.setTopAnchor(shareLabel, 0d);
        AnchorPane.setRightAnchor(shareLabel, toolBarHeight);
        AnchorPane.setBottomAnchor(shareLabel, 0d);
        shareLabel.setOnMouseClicked((evt) -> {
            ideController.showMenu3(project);
            //ideController.getShareController().setUser(user);
        });

        ImageView icon3 = new ImageView(new Image(Main.class.getResourceAsStream("css/images/1Capture.png")));
        Label versionLabel = new Label("", icon3);
        versionLabel.setFont(new Font(16));
        versionLabel.setAlignment(Pos.CENTER);
        versionLabel.setTooltip(new Tooltip("Versions of this document"));
        versionLabel.getStyleClass().addAll("compile-lbl", "tool");
        versionLabel.setPrefWidth(toolBarHeight);
        AnchorPane.setTopAnchor(versionLabel, 0d);
        AnchorPane.setRightAnchor(versionLabel, 0d);
        AnchorPane.setBottomAnchor(versionLabel, 0d);
        versionLabel.setOnMouseClicked((evt) -> {
            ideController.showMenu2(user, project, doc, aceEditor);
        });

        toolBar.getChildren().addAll(compileLbl, loadingView, /*compileRunLbl,*/ down, commit, shareLabel, versionLabel);
        sharedUserVBox.setPadding(new Insets(0, 0, 10, 0));
        //sharedUserVBox.setStyle("-fx-background-color: blue;");
        //sharedUserVBox.setPrefHeight(10);
        //sharedUserVBox.setMinHeight(10);
        //sharedUserVBox.setMaxHeight(10);
        sharedUserVBox.setAlignment(Pos.CENTER);

        //sharedUserVBox.setMaxHeight(10);
        //sharedUserVBox.setMinHeight(0);
        //sharedUserVBox.setPrefHeight(0);
        /*sharedUserVBox.getChildren().add(new ColorLabelPane(Color.web("#7DC4E8"), "Ashish"));
         sharedUserVBox.getChildren().add(new ColorLabelPane(Color.web("#81A480"), "Swapnil"));*/
        DropShadow ds = new DropShadow();
        ds.setRadius(2);

        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: red;");
        stackPane.setAlignment(Pos.CENTER);
        //stackPane.getChildren().add(sharedUserVBox);

        PopOver popup = new PopOver(sharedUserVBox);
        //popup.setMinHeight(10);
        //popup.setPrefHeight(10);
        popup.setHeight(10);
        popup.setStyle("-fx-effect: dropshadow( gaussian , rgba(0, 0, 0, 0.4) , 3,0,0,0 );"
                + "-fx-background-color: rgba(255, 255, 255, 0.8)");
        popup.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popup.setDetachable(false);
        popup.setCornerRadius(3.0);
        down.setOnMouseClicked((evt) -> {
            if (sharedUserVBox.getChildren().isEmpty()) {
                return;
            }
            popup.show(down);
            ((Parent) popup.getSkin().getNode()).getStylesheets().add("ui/css/IDEStyle.css");
            //popup.hide();
            popup.show(down);
        });

        AnchorPane commitPane = new AnchorPane();

        TextField t1 = new TextField();
        t1.setPromptText("Enter name for commit");
        t1.setMinWidth(100);
        AnchorPane.setLeftAnchor(t1, 5d);
        AnchorPane.setTopAnchor(t1, 10d);
        AnchorPane.setRightAnchor(t1, 5d);

        Button b1 = new Button("Commit");
        b1.setFont(IDEController.font12);
        b1.getStyleClass().add("create-btn");
        b1.setMinWidth(80);

        PopOver popup2 = new PopOver(commitPane);
        b1.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent evt) {
                String commitName = t1.getText();
                if (commitName.length() == 0) {
                    Message.showError("Empty Name", "Cannot make commit with empty name");
                    return;
                }

                if (commitName.contains(" ") || commitName.matches(".*[^a-z0-9A-Z].*")) {
                    Message.showError("Incorrect Commit Name", "Commits cannot have spaces or special characters");
                    return;
                }
                
                System.out.println(doc.getPath());

                //String path = "Commits/" + user.getUsername() + "/" + project.getName() + "/" + doc.getName();
                StringBuilder path2 = new StringBuilder("Commits");
                
                String arr[] = doc.getPath().split("\\.");
                for (String dir: arr) {
                    if (dir.length() == 0) {
                        continue;
                    }
                    
                    path2.append("/").append(dir);
                }
                path2.append("/").append(doc.getName());
                //System.out.println("path2 " + doc.getPath() + " " + arr.length);
                
                String path = path2.toString();

                //System.out.println(path);
                File file = new File(path);
                file.mkdirs();

                file = new File(path + "/" + commitName);
                if (file.exists()) {
                    MyNotification.show("Already Exists", "version with name " + commitName + " already exists");
                    return;
                }

                try {
                    PrintWriter out = new PrintWriter(path + "/" + commitName);
                    out.println(aceEditor.getText());
                    out.flush();
                    out.close();

                    popup2.hide();
                    MyNotification.show("Version saved successfully", commitName + " successfully saved as a new version for the document " + doc.getName());
                } catch (FileNotFoundException ex) {
                    Message.showError("Incorrect Commit Name", "Commits cannot have spaces or special characters");
                    Logger.getLogger(DocumentTab.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        AnchorPane.setTopAnchor(b1, 40d);
        AnchorPane.setRightAnchor(b1, 15d);
        AnchorPane.setBottomAnchor(b1, 15d);

        commitPane.setMinWidth(250);
        commitPane.getChildren().addAll(t1, b1);

        popup2.setStyle("-fx-effect: dropshadow( gaussian , rgba(0, 0, 0, 0.4) , 3,0,0,0 );"
                + "-fx-background-color: rgba(255, 255, 255, 0.8)");
        popup2.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popup2.setDetachable(false);
        popup2.setCornerRadius(3.0);
        commit.setOnMouseClicked((evt) -> {
            double x = evt.getScreenX() - evt.getX() + toolBarHeight / 2 - 90;
            double y = evt.getScreenY() - evt.getY() + toolBarHeight - 5;

            //System.out.println(x + " " + y + " " + evt.getScreenX());
            t1.clear();
            t1.requestFocus();
            popup2.show(commit.getScene().getWindow(), x, y);
            ((Parent) popup2.getSkin().getNode()).getStylesheets().add("ui/css/IDEStyle.css");
            popup2.show(commit, x, y);
        });

        this.setContent(content);
        content.getChildren().addAll(toolBar/*, aceEditor*/);

        tabPane.getTabs().add(DocumentTab.this);
        tabPane.getSelectionModel().select(DocumentTab.this);

        ImageView imgView = new ImageView(new Image(Main.class.getResourceAsStream("css/images/2ring.gif")));
        gifParent.setStyle("-fx-background-color: white;");
        gifParent.getChildren().add(imgView);
        gifParent.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(gifParent, 0d);
        AnchorPane.setTopAnchor(gifParent, toolBarHeight);
        AnchorPane.setRightAnchor(gifParent, 0d);
        AnchorPane.setBottomAnchor(gifParent, 0d);
        content.getChildren().add(gifParent);

        this.setOnCloseRequest((Event event) -> {
            closeRequest();
        });

        new Thread(new AceLoader(this/*, code*/)).start();
    }

    public void doWhenLoaded(AceEditor aceEditor) {
        this.aceEditor = aceEditor;

        Platform.runLater(() -> {
            AnchorPane.setLeftAnchor(aceEditor, 0d);
            AnchorPane.setTopAnchor(aceEditor, toolBarHeight);
            AnchorPane.setRightAnchor(aceEditor, 0d);
            AnchorPane.setBottomAnchor(aceEditor, 0d);

            aceEditor.setOpacity(0);

            //content.getChildren().remove(gifParent);
            gifParent.getChildren().clear();
            content.getChildren().add(aceEditor);

            FadeInTransition ff = new FadeInTransition(aceEditor);
            ff.setRate(2);
            ff.play();
        });
    }

    public String readFile(String file) {
        try {
            InputStream inpStream = new FileInputStream(file);
            File f = new File(file);

            byte[] bytes = new byte[(int) f.length()];
            inpStream.read(bytes);

            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getUser() {
        return user;
    }

    public Doc getDoc() {
        return doc;
    }

    public void closeRequest() {
        if (aceEditor == null) {
            return;
        }
        aceEditor.destroy();
    }

    public void updateOnlineUsers(HashMap<String, String> editUserColor) {
        sharedUserVBox.getChildren().clear();
        sharedUserVBox.setPrefHeight(0);
        
        for (String name : editUserColor.keySet()) {
            //down.setDisable(false);
            sharedUserVBox.getChildren().add(new ColorLabelPane(Color.web(editUserColor.get(name)), name));
        }
        /*sharedUserVBox.getChildren().add(new ColorLabelPane(Color.web("#7DC4E8"), "Ashish"));
         sharedUserVBox.getChildren().add(new ColorLabelPane(Color.web("#81A480"), "Swapnil"));
         sharedUserVBox.getChildren().add(new ColorLabelPane(Color.web("#7DC4E8"), "Ashish"));
         sharedUserVBox.getChildren().add(new ColorLabelPane(Color.web("#81A480"), "Swapnil"));*/
        /*sharedUserVBox.getChildren().add(new ColorLabelPane(Color.web("#81A480"), "Swapnil"));*/
    }

    private class AceLoader implements Runnable {

        DocumentTab tab;
        String code;
        AceEditor aceEditor;

        public AceLoader(DocumentTab tab/*, String code*/) {
            this.tab = tab;
            //this.code = code;
        }

        @Override
        public void run() {
            try {
                //Thread.sleep(700);
                String tempUserID = tab.getUser().getUserIdentifier();//RandomGen.getRandom();
                Session session = new Session(tempUserID/*tab.getUser().getUserID()*/, tab.getDoc().getIdentifier());
                Platform.runLater(() -> {
                    aceEditor = AceEditor.getInstance(DocumentTab.this, session, user, tempUserID);
                    //aceEditor.init();

                    aceEditor.performWhenLoaded(() -> {
                        //aceEditor.setText(code);
                        tab.doWhenLoaded(aceEditor);
                    });
                });
            } catch (Exception ex) {
                Logger.getLogger(AceLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

interface SharedUsersListener {

    void addUser(User user);
}

class ColorLabelPane extends AnchorPane {

    public ColorLabelPane(Color color, String name) {
        Circle circle = new Circle(5, color);
        //circle.setStroke(Color.GREY);

        Label l1 = new Label("", circle);
        l1.setPadding(new Insets(10, 10, 0, 10));

        AnchorPane.setLeftAnchor(l1, 0d);
        AnchorPane.setTopAnchor(l1, 0d);
        AnchorPane.setBottomAnchor(l1, 0d);

        Label l2 = new Label(name);
        l2.getStyleClass().add("lbl");
        l2.setPadding(new Insets(10, 10, 0, 10));
        AnchorPane.setLeftAnchor(l2, 16d);
        AnchorPane.setTopAnchor(l2, 0d);
        AnchorPane.setBottomAnchor(l2, 0d);

        this.getChildren().addAll(l1, l2);
    }
}
