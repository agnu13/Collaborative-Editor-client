package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.controllers.LoginController;

public class Login {
    
    public Login() throws Exception {
        Stage primaryStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Login.fxml"));
        AnchorPane root1 = fxmlLoader.load();
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(root1.widthProperty());
        clip.heightProperty().bind(root1.heightProperty());
        root1.setClip(clip);

        AnchorPane root = new AnchorPane();
        root.getChildren().add(root1);
        root.setCache(true);
        root.setCacheShape(true);
        root.setCacheHint(CacheHint.SPEED);
        
        DropShadow ds = new DropShadow();
        ds.setRadius(10);
        ds.setSpread(0);
        ds.setColor(Color.rgb(0, 0, 0, 0.4));
        root.setEffect(ds);
        
        AnchorPane.setLeftAnchor(root, 10d);
        AnchorPane.setTopAnchor(root, 10d);
        AnchorPane.setRightAnchor(root, 10d);
        AnchorPane.setBottomAnchor(root, 10d);
        
        AnchorPane rootParent = new AnchorPane();
        rootParent.setStyle("-fx-background-color: transparent");
        rootParent.getChildren().add(root);

        Scene scene = new Scene(rootParent);
        scene.setFill(null);
        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("css/images/icon.png")));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setOnCloseRequest(evt -> {
            System.exit(0);
        });
        ((LoginController) fxmlLoader.getController()).setStage(primaryStage);

        /*root.setOpacity(0);
        FlipInXTransition ff = new FlipInXTransition(root);
        ff.setDelay(Duration.seconds(2));
        ff.play();*/
        
        primaryStage.show();
    }
}
