package ui.controllers;

import authenticate.entities.User;
import authenticate.exception.IncorrectPasswordException;
import authenticate.login.Login;
import authenticate.signup.Signup;
import com.fxexperience.javafx.animation.*;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import projectManager.CollectionFactory;
import projectManager.Collections;
import projectManager.Node;
import ui.Main;
import ui.MyStage;
import static ui.controllers.IDEController.WEB_EASE;
import ui.util.Message;

public class LoginController implements Initializable {

    private IDEController ideController;
    private Stage loginStage;

    @FXML
    Label loginTab, registerTab;

    @FXML
    AnchorPane tabParent, root;

    @FXML
    AnchorPane tab1, tab2;

    @FXML
    TextField loginTxt, r1, r2, r4;

    @FXML
    PasswordField passwordTxt, r3;

    @FXML
    Button b1, b2;

    double xOffset, yOffset;

    boolean selected = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setPrefHeight(420);
        root.setMinHeight(420);
        root.setMaxHeight(420);
        //root.getScene().getWindow().setHeight(420);
    }

    public void setIDEControllerInstance(IDEController ideController) {
        this.ideController = ideController;
    }

    public void loginTabClicked(MouseEvent evt) {
        if (selected) {
            return;
        }
        loginTxt.requestFocus();
        selected = true;

        registerTab.getStyleClass().remove("tab-button-selected");
        registerTab.getStyleClass().add("tab-button");

        loginTab.getStyleClass().remove("tab-button");
        loginTab.getStyleClass().add("tab-button-selected");

        //FadeOutRightTransition ff = new FadeOutRightTransition(tab2);
        FadeOutTransition ff = new FadeOutTransition(tab2);
        ff.setDelay(Duration.ZERO);
        ff.setRate(3);
        ff.setOnFinished((event) -> {
            tab2.setVisible(false);
        });
        ff.play();

        //FadeInLeftTransition ff2 = new FadeInLeftTransition(tab1);
        FadeInTransition ff2 = new FadeInTransition(tab1);
        ff2.setDelay(Duration.ZERO);
        ff2.setRate(3);
        ff2.play();

        /*((AnchorPane)root.getScene().getRoot()).setMinHeight(100);
         ((AnchorPane)root.getScene().getRoot()).setMaxHeight(100);
        
         root.getScene().getWindow().setHeight(100);*/
        DoubleProperty heightProperty = new SimpleDoubleProperty(root.getScene().getWindow().getHeight());
        heightProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            root.getScene().getWindow().setHeight(newValue.doubleValue());
        });

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(270),
                        new KeyValue(root.prefHeightProperty(), 420, WEB_EASE),
                        new KeyValue(root.maxHeightProperty(), 420, WEB_EASE),
                        new KeyValue(root.minHeightProperty(), 420, WEB_EASE),
                        new KeyValue(heightProperty, 440, WEB_EASE)
                )
        );
        timeline.play();
    }

    public void registerTabClicked(MouseEvent evt) {
        if (!selected) {
            return;
        }
        selected = false;
        tab2.setOpacity(0);
        tab2.setVisible(true);

        loginTab.getStyleClass().remove("tab-button-selected");
        loginTab.getStyleClass().add("tab-button");

        registerTab.getStyleClass().remove("tab-button");
        registerTab.getStyleClass().add("tab-button-selected");

        //FadeOutLeftTransition ff = new FadeOutLeftTransition(tab1);
        FadeOutTransition ff = new FadeOutTransition(tab1);
        ff.setDelay(Duration.ZERO);
        ff.setRate(3);
        ff.play();

        //FadeInRightTransition ff2 = new FadeInRightTransition(tab2);
        FadeInTransition ff2 = new FadeInTransition(tab2);
        ff2.setDelay(Duration.ZERO);
        ff2.setRate(3);
        ff2.play();

        DoubleProperty heightProperty = new SimpleDoubleProperty(root.getScene().getWindow().getHeight());
        heightProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            root.getScene().getWindow().setHeight(newValue.doubleValue());
        });

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(270),
                        new KeyValue(root.prefHeightProperty(), 550, WEB_EASE),
                        new KeyValue(root.maxHeightProperty(), 550, WEB_EASE),
                        new KeyValue(root.minHeightProperty(), 550, WEB_EASE),
                        new KeyValue(heightProperty, 570, WEB_EASE)
                )
        );
        timeline.play();
        r1.requestFocus();
    }

    public void setStage(Stage primaryStage) {
        this.loginStage = primaryStage;

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
    }

    @FXML
    public void loginAction(ActionEvent evt) throws IOException {
        String userName = loginTxt.getText();
        String password = passwordTxt.getText();
        b1.setDisable(true);

        new Thread(() -> {
            try {
                User user = Login.doLogin(userName, password);
                Message.showInformation("Login Successful", userName + "! You have logged in successfully.");

                Collections collection = CollectionFactory.getCollection(userName);
                ArrayList<Node> nodeList = collection.getContent();

                Platform.runLater(() -> {
                    doAfterLoginDone(nodeList, user);
                });
            } catch (ConnectivityFailureException ex) {
                Message.showError("Connectivity Error", ex.getMessage());
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IncorrectPasswordException ex) {
                Message.showError("Incorrect user name or password", ex.getMessage());
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Platform.runLater(() -> {
                b1.setDisable(false);
            });
        }).start();
    }

    private void doAfterLoginDone(ArrayList<Node> nodeList, User user) {
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/IDE.fxml"));
        AnchorPane root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(root.widthProperty());
        clip.heightProperty().bind(root.heightProperty());
        root.setClip(clip);

        MyStage stage = new MyStage(primaryStage);
        stage.stage.getScene().getStylesheets().add("ui/css/IDEStyle.css");
        stage.setRoot(root);
        //stage.setMaximized(true);

        //((IDEController) fxmlLoader.getController()).setStage(stage);
        //((IDEController) fxmlLoader.getController()).setUser(user);
        ((IDEController) fxmlLoader.getController()).init(stage, user);

        if (!nodeList.isEmpty()) {
            ((IDEController) fxmlLoader.getController()).loadProject((Collections) nodeList.get(0));
        }

        loginStage.close();
        stage.show();
    }

    @FXML
    public void userFieldAction(ActionEvent evt) throws IOException {
        passwordTxt.requestFocus();
    }

    @FXML
    public void passFieldAction(ActionEvent evt) throws IOException {
        loginAction(evt);
    }

    @FXML
    public void passKeyPressed(KeyEvent evt) {
        if (evt.getCode() == KeyCode.TAB) {
            evt.consume();
            loginTxt.requestFocus();
        }
    }

    @FXML
    public void r4KeyPressed(KeyEvent evt) {
        if (evt.getCode() == KeyCode.TAB) {
            evt.consume();
            r1.requestFocus();
        }
    }

    @FXML
    public void r1Action(ActionEvent evt) {
        r2.requestFocus();
    }

    @FXML
    public void r2Action(ActionEvent evt) {
        r3.requestFocus();
    }

    @FXML
    public void r3Action(ActionEvent evt) {
        r4.requestFocus();
    }

    @FXML
    public void r4Action(ActionEvent evt) {
        registerClicked(null);
    }

    @FXML
    private void registerClicked(ActionEvent evt) {
        String name = r1.getText();
        String userName = r2.getText();
        String password = r3.getText();
        String email = r4.getText();
        b2.setDisable(true);

        if (name.length() == 0 || userName.length() == 0 || password.length() == 0 || email.length() == 0) {
            //System.err.println("Incomplete Details");
            Message.showError("Incomplete Details", "Enter credentials correctly");
            b2.setDisable(false);
            return;
        }

        User user = new User();
        user.setName(name);
        user.setUsername(userName);
        user.setEmailId(email);
        user.setPassword(password);

        try {
            Signup.doSignUp(user);

            r1.clear();
            r2.clear();
            r3.clear();
            r4.clear();

            Message.showInformation("Register", userName + "! You have successfully registered");

            loginTabClicked(null);
        } catch (ConnectivityFailureException ex) {
            Message.showError("Connectivity Error", ex.getMessage());
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        b2.setDisable(false);
    }

}
