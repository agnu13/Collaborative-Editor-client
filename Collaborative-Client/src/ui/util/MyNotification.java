package ui.util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ui.Main;
import ui.controllers.IDEController;
import static ui.controllers.IDEController.WEB_EASE;
import ui.controllers.MyNotiController;

public class MyNotification {

    private static int top = 0, bottom = 0;
    private static ArrayList<Popup> list = new ArrayList<>();

    public static void show(String title, String text) {
        if (title == null || title.length() == 0) {
            //return;
        }
        
        GlobalSettings settings = GlobalSettings.getInstance();

        if (settings.getNotificationsSoundProperty().get()) {
            new Thread(() -> {
                //System.out.println("Ashish");
                URL url = Main.class.getResource("css/images/clip.wav");
                AudioClip ac1 = Applet.newAudioClip(url);
                ac1.play();
            }).start();
        }

        if (!settings.getShowNotificationsProperty().get()) {
            return;
        }

        try {
            for (Iterator<Popup> iterator = list.iterator(); iterator.hasNext();) {
                Popup stage = iterator.next();
                if (!stage.isShowing()) {
                    iterator.remove();
                }
            }

            for (Popup stage : list) {
                double res = stage.getY() - 90;
                Timeline timeline = new Timeline();

                DoubleProperty dp = new SimpleDoubleProperty(stage.getY());
                dp.addListener(new ChangeListener<Number>() {

                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        stage.setY(newValue.doubleValue());
                    }
                });
                
                KeyFrame keyFrame = new KeyFrame(Duration.millis(200), new KeyValue(dp, res, WEB_EASE));
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
            }

            /*Notifications not = Notifications.create()
             .title(title)
             .text(text)
             .hideAfter(Duration.seconds(7));
            
             not.show();*/
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/MyNoti.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            AnchorPane.setLeftAnchor(root1, 10d);
            AnchorPane.setRightAnchor(root1, 10d);
            AnchorPane.setTopAnchor(root1, 10d);
            AnchorPane.setBottomAnchor(root1, 10d);

            DropShadow ds = new DropShadow();
            ds.setRadius(10);
            ds.setColor(Color.rgb(0, 0, 0, 0.4));

            root1.setEffect(ds);

            AnchorPane root = new AnchorPane();
            root.setStyle("-fx-background-color: transparent");
            root.getChildren().add(root1);

            MyNotiController controller = fxmlLoader.getController();
            controller.init(title, text);

            /*Scene scene = new Scene(root);
            scene.setFill(null);*/
            
            Popup stage = new Popup();
            stage.getContent().add(root);
            //Stage stage = new Stage();
            /*stage.initModality(Modality.NONE);
            stage.setTitle("Notification");
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);*/
            list.add(stage);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();

            stage.setX(width - 485);
            stage.setY(height - 142);
            //stage.setAlwaysOnTop(true);

            root.setOpacity(0);
            Scene tmpScene = new Scene(new Pane());
            tmpScene.getStylesheets().add("ui/css/notification.css");
            Stage tmpStage = new Stage();
            tmpStage.setScene(tmpScene);
            tmpStage.initStyle(StageStyle.UTILITY);
            tmpStage.setOpacity(0.0001);
            tmpStage.setAlwaysOnTop(true);
            tmpStage.show();
            stage.show(tmpStage);
            top++;

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5000), evt -> {
                //stage.hide();
                Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(500),
                        ev -> {
                            stage.hide();
                            bottom++;
                            if (top == bottom) {
                                top = bottom = 0;
                            }
                        },
                        new KeyValue(root.opacityProperty(), 0, IDEController.WEB_EASE)));
                timeline2.play();
            }));
            timeline.play();

            Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(500), new KeyValue(root.opacityProperty(), 1, IDEController.WEB_EASE)));
            timeline2.play();
        } catch (IOException ex) {
            Logger.getLogger(MyNotification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
