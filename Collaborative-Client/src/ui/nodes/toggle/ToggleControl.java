package ui.nodes.toggle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import ui.controllers.IDEController;

public final class ToggleControl extends Label {

    EventHandler<StateChangeEvent> stateChangeHandler;

    private Label toggleHandle = new Label();
    private AnchorPane backgroundPane = new AnchorPane();

    private ToggleState state;

    private BooleanProperty toggleProperty;

    private final double sizeMultiplier = 2.5;
    private double radius = sizeMultiplier * 3;
    private double length = sizeMultiplier * 12;

    public ToggleControl(ToggleState state1) {
        this.state = state1;
        toggleProperty = new SimpleBooleanProperty(state == ToggleState.ON);
        Circle circle1 = new Circle();
        circle1.setRadius(radius);
        circle1.setFill(Color.RED);
        circle1.setCenterX(radius);
        circle1.setCenterY(radius);

        Circle circle2 = new Circle();
        circle2.setRadius(radius);
        circle2.setFill(Color.WHEAT);
        circle2.setCenterX(length - radius);
        circle2.setCenterY(radius);

        Rectangle r = new Rectangle();
        r.setWidth(length - 2 * radius);
        r.setHeight(2 * radius);
        r.setFill(Color.AQUA);
        r.setX(radius);
        r.setY(0);

        Shape shape = Path.union(Path.union(circle1, circle2), r);

        Circle circle = new Circle(radius - 2, Color.WHITE);
        circle.getStyleClass().add("toggle-handle");

        double handleRadius = radius;

        toggleHandle.setMinSize(handleRadius * 2, handleRadius * 2);
        toggleHandle.setMaxSize(handleRadius * 2, handleRadius * 2);
        toggleHandle.setStyle("-fx-background-color: white; -fx-background-radius: " + handleRadius);
        toggleHandle.setPadding(new Insets(2, 2, 2, 2));
        //toggleHandle.setGraphic(circle);
        toggleHandle.setTranslateX((state == ToggleState.OFF) ? 0 : length - 2 * radius);

        //toggleHandle.getStyleClass().add("toggle-handle");
        AnchorPane.setLeftAnchor(toggleHandle, 0d);

        backgroundPane.setMinSize(length, 2 * radius);
        backgroundPane.setMaxSize(length, 2 * radius);
        backgroundPane.getStyleClass().add((state == ToggleState.ON) ? "background" : "background-off");
        backgroundPane.setClip(shape);
        backgroundPane.getChildren().add(toggleHandle);

        this.setOnMouseClicked((evt) -> {
            state = state.toggle();
            backgroundPane.getStyleClass().clear();
            backgroundPane.getStyleClass().add((state == ToggleState.ON) ? "background" : "background-off");

            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(150),
                            new KeyValue(toggleHandle.translateXProperty(),
                                    (state == ToggleState.OFF) ? 0 : length - 2 * radius, IDEController.WEB_EASE)
                    )
            );
            timeline.setDelay(Duration.ZERO);
            timeline.play();

            if (stateChangeHandler != null) {
                StateChangeEvent event = new StateChangeEvent(null, state);
                stateChangeHandler.handle(event);
            }
        });

        this.setGraphic(backgroundPane);
        this.getStyleClass().add("toggle");
        this.getStylesheets().add(this.getClass().getResource("toggle.css").toExternalForm());

        this.setOnStateChanged((StateChangeEvent evt2) -> {
            toggleProperty.set((evt2.getState() == ToggleState.ON));
        });
    }

    private void setOnStateChanged(EventHandler<StateChangeEvent> handler) {
        this.stateChangeHandler = handler;
    }

    public BooleanProperty getToggleProperty() {
        return toggleProperty;
    }
}
