package ui;

import config.Configuration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ui.nodes.toggle.ToggleState;
import ui.nodes.toggle.ToggleControl;

/**
 *
 * @author Ashish
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //AceEditor.generatePool();
        Configuration.getConfiguration();
        
        Pane pane = new Pane();
        ToggleControl toggle = new ToggleControl(ToggleState.OFF);
        toggle.setLayoutX(50);
        toggle.setLayoutY(50);
        pane.getChildren().add(toggle);
        
        stage.setScene(new Scene(pane, 500, 300));
        //stage.show();
        
        new Login();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
