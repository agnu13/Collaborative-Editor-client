package ui;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.commons.lang.StringEscapeUtils;

public class MyStage {

    public Stage stage;
    private boolean isMaximized;
    private AnchorPane parent;

    private final AnchorPane contentHolder;
    private AnchorPane titleBar;
    private final AnchorPane content;

    private final Label minimize = new Label(StringEscapeUtils.unescapeJava("\\uD83D\\uDDD5"));
    private final Label maximize = new Label(StringEscapeUtils.unescapeJava("\uD83D\uDDD6"));
    private final Label close = new Label(StringEscapeUtils.unescapeJava("\uD83D\uDDD9"));

    private final Label hamburger = new Label();

    private final Label title = new Label("Collaborative IDE");
    private final ImageView icon = new ImageView(new Image(this.getClass().getResourceAsStream("css/images/icon-small.png")));

    private AnchorPane root;
    private Scene scene;

    private final double resizeMargin = 5;
    private final double resizerWidth = 15;
    private final double titleBarHeight = 30;
    private final double titleButtonsWidth = 45;

    private double stageWidth = 800;
    private double stageHeight = 500;
    private double stageX;
    private double stageY;

    private boolean cache = true;

    private double maxWidth = 800, maxHeight = 500;

    private final Interpolator WEB_EASE = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);

    public MyStage(Stage stage) {
        Font font = Font.loadFont(MyStage.class.getResourceAsStream("css/fonts/Symbola.ttf"), 15);
        minimize.setFont(font);
        maximize.setFont(font);
        close.setFont(font);

        minimize.setPrefSize(titleButtonsWidth, titleBarHeight);
        maximize.setPrefSize(titleButtonsWidth, titleBarHeight);
        close.setPrefSize(titleButtonsWidth, titleBarHeight);

        minimize.setTextAlignment(TextAlignment.CENTER);
        minimize.setAlignment(Pos.CENTER);
        maximize.setTextAlignment(TextAlignment.CENTER);
        maximize.setAlignment(Pos.CENTER);
        close.setTextAlignment(TextAlignment.CENTER);
        close.setAlignment(Pos.CENTER);

        minimize.getStyleClass().add("title-button-white");
        maximize.getStyleClass().add("title-button-white");
        close.getStyleClass().add("close-button-white");

        minimize.setPadding(new Insets(0, 0, 5, 0));
        maximize.setPadding(new Insets(0, 0, 5, 0));
        close.setPadding(new Insets(0, 0, 5, 0));

        minimize.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (cache) {
                    parent.setCache(true);
                    parent.setCacheShape(true);
                    parent.setCacheHint(CacheHint.SPEED);
                }
                Timeline timeline = new Timeline(
                        new KeyFrame(
                                Duration.millis(200),
                                new KeyValue(scene.getRoot().opacityProperty(), 0, WEB_EASE),
                                new KeyValue(scene.getRoot().translateYProperty(), 200, WEB_EASE),
                                new KeyValue(scene.getRoot().scaleXProperty(), 0.5, WEB_EASE),
                                new KeyValue(scene.getRoot().scaleYProperty(), 0.5, WEB_EASE)
                        )
                );

                timeline.setDelay(Duration.ZERO);
                timeline.play();
                timeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (cache) {
                            parent.setCache(false);
                            parent.setCacheShape(false);
                            parent.setCacheHint(CacheHint.DEFAULT);
                        }
                        stage.setIconified(true);
                    }
                });
            }
        });
        maximize.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                setMaximized(!isMaximized());
            }
        });
        close.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                close();
            }
        });

        AnchorPane.setLeftAnchor(icon, 4d);
        AnchorPane.setTopAnchor(icon, 4d);
        AnchorPane.setBottomAnchor(icon, 4d);

        AnchorPane.setLeftAnchor(hamburger, 0d);
        AnchorPane.setTopAnchor(hamburger, 0d);

        hamburger.setPrefSize(45, 35);
        hamburger.getStyleClass().add("hamburger");

        //AnchorPane.setLeftAnchor(title, 30d);
        AnchorPane.setLeftAnchor(title, 0d);
        AnchorPane.setRightAnchor(title, 0d);
        AnchorPane.setTopAnchor(title, 4d);
        AnchorPane.setBottomAnchor(title, 4d);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        title.setTextFill(Color.rgb(255, 255, 255, 0.9));
        title.setFont(new Font(14));

        parent = new AnchorPane();

        AnchorPane pane0 = new AnchorPane();
        AnchorPane pane1 = new AnchorPane();
        AnchorPane pane2 = new AnchorPane();
        AnchorPane pane3 = new AnchorPane();

        AnchorPane pane11 = new AnchorPane();
        AnchorPane pane12 = new AnchorPane();
        AnchorPane pane13 = new AnchorPane();

        AnchorPane pane21 = new AnchorPane();
        AnchorPane pane22 = new AnchorPane();

        AnchorPane pane31 = new AnchorPane();
        AnchorPane pane32 = new AnchorPane();
        AnchorPane pane33 = new AnchorPane();

        pane1.setMinHeight(resizerWidth);
        pane2.setMinHeight(resizerWidth);
        pane3.setMinHeight(resizerWidth);

        pane11.setMinWidth(resizerWidth);
        pane11.setMinHeight(resizerWidth);
        pane12.setMinHeight(resizerWidth);
        pane13.setMinWidth(resizerWidth);
        pane13.setMinHeight(resizerWidth);

        pane21.setMinWidth(resizerWidth);
        pane21.setMinHeight(resizerWidth);
        pane22.setMinWidth(resizerWidth);
        pane22.setMinHeight(resizerWidth);

        pane31.setMinWidth(resizerWidth);
        pane31.setMinHeight(resizerWidth);
        pane32.setMinHeight(resizerWidth);
        pane33.setMinWidth(resizerWidth);
        pane33.setMinHeight(resizerWidth);

        AnchorPane.setLeftAnchor(pane0, resizeMargin);
        AnchorPane.setTopAnchor(pane0, resizeMargin);
        AnchorPane.setRightAnchor(pane0, resizeMargin);
        AnchorPane.setBottomAnchor(pane0, resizeMargin);

        AnchorPane.setLeftAnchor(pane1, 0d);
        AnchorPane.setTopAnchor(pane1, 0d);
        AnchorPane.setRightAnchor(pane1, 0d);

        AnchorPane.setLeftAnchor(pane2, 0d);
        AnchorPane.setRightAnchor(pane2, 0d);
        AnchorPane.setTopAnchor(pane2, resizerWidth);
        AnchorPane.setBottomAnchor(pane2, resizerWidth);

        AnchorPane.setLeftAnchor(pane3, 0d);
        AnchorPane.setRightAnchor(pane3, 0d);
        AnchorPane.setBottomAnchor(pane3, 0d);

        AnchorPane.setLeftAnchor(pane11, 0d);
        AnchorPane.setTopAnchor(pane11, 0d);
        AnchorPane.setTopAnchor(pane12, 0d);
        AnchorPane.setLeftAnchor(pane12, resizerWidth);
        AnchorPane.setRightAnchor(pane12, resizerWidth);
        AnchorPane.setTopAnchor(pane13, 0d);
        AnchorPane.setRightAnchor(pane13, 0d);

        AnchorPane.setLeftAnchor(pane21, 0d);
        AnchorPane.setTopAnchor(pane21, 0d);
        AnchorPane.setBottomAnchor(pane21, 0d);
        AnchorPane.setRightAnchor(pane22, 0d);
        AnchorPane.setTopAnchor(pane22, 0d);
        AnchorPane.setBottomAnchor(pane22, 0d);

        AnchorPane.setLeftAnchor(pane31, 0d);
        AnchorPane.setBottomAnchor(pane31, 0d);
        AnchorPane.setBottomAnchor(pane32, 0d);
        AnchorPane.setLeftAnchor(pane32, resizerWidth);
        AnchorPane.setRightAnchor(pane32, resizerWidth);
        AnchorPane.setBottomAnchor(pane33, 0d);
        AnchorPane.setRightAnchor(pane33, 0d);

        pane11.setCursor(Cursor.NW_RESIZE);
        pane12.setCursor(Cursor.V_RESIZE);
        pane13.setCursor(Cursor.NE_RESIZE);
        pane21.setCursor(Cursor.E_RESIZE);
        pane22.setCursor(Cursor.W_RESIZE);
        pane31.setCursor(Cursor.SW_RESIZE);
        pane32.setCursor(Cursor.V_RESIZE);
        pane33.setCursor(Cursor.SE_RESIZE);

        pane0.setStyle("-fx-background-color: rgba(1, 1, 1, 0.001)");
        pane1.setStyle("-fx-background-color: rgba(1, 1, 1, 0.001)");
        pane2.setStyle("-fx-background-color: rgba(1, 1, 1, 0.001)");
        pane3.setStyle("-fx-background-color: rgba(1, 1, 1, 0.001)");

        final Resizer dragDelta = new Resizer();

        pane31.setOnMousePressed(dragDelta::reset);
        pane31.setOnMouseDragged(dragDelta::resizeSouthEast);
        pane31.setOnMouseReleased(dragDelta::release);

        pane33.setOnMousePressed(dragDelta::reset);
        pane33.setOnMouseDragged(dragDelta::resizeSouthWest);
        pane33.setOnMouseReleased(dragDelta::release);

        pane11.setOnMousePressed(dragDelta::reset);
        pane11.setOnMouseDragged(dragDelta::resizeNorthEast);
        pane11.setOnMouseReleased(dragDelta::release);

        pane13.setOnMousePressed(dragDelta::reset);
        pane13.setOnMouseDragged(dragDelta::resizeNorthWest);
        pane13.setOnMouseReleased(dragDelta::release);

        pane22.setOnMousePressed(dragDelta::reset);
        pane22.setOnMouseDragged(dragDelta::resizeWest);
        pane22.setOnMouseReleased(dragDelta::release);

        pane21.setOnMousePressed(dragDelta::reset);
        pane21.setOnMouseDragged(dragDelta::resizeEast);
        pane21.setOnMouseReleased(dragDelta::release);

        pane12.setOnMousePressed(dragDelta::reset);
        pane12.setOnMouseDragged(dragDelta::resizeNorth);
        pane12.setOnMouseReleased(dragDelta::release);

        pane32.setOnMousePressed(dragDelta::reset);
        pane32.setOnMouseDragged(dragDelta::resizeSouth);
        pane32.setOnMouseReleased(dragDelta::release);

        contentHolder = new AnchorPane();
        titleBar = new AnchorPane();
        content = new AnchorPane();

        AnchorPane.setLeftAnchor(contentHolder, resizerWidth + resizeMargin);
        AnchorPane.setTopAnchor(contentHolder, resizerWidth + resizeMargin);
        AnchorPane.setRightAnchor(contentHolder, resizerWidth + resizeMargin);
        AnchorPane.setBottomAnchor(contentHolder, resizerWidth + resizeMargin);

        AnchorPane.setLeftAnchor(titleBar, 0d);
        AnchorPane.setTopAnchor(titleBar, 0d);
        AnchorPane.setRightAnchor(titleBar, 0d);

        AnchorPane.setLeftAnchor(content, 0d);
        //AnchorPane.setTopAnchor(content, titleBarHeight);
        AnchorPane.setTopAnchor(content, 0d);
        AnchorPane.setRightAnchor(content, 0d);
        AnchorPane.setBottomAnchor(content, 0d);

        titleBar.setPrefHeight(titleBarHeight);
        titleBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dragDelta.reset(mouseEvent);

                if (isMaximized()) {
                    dragDelta.sx = 0;
                    dragDelta.sy = 0;
                    dragDelta.x = resizeMargin + 200;
                    dragDelta.y = resizeMargin + resizerWidth + 5;
                }
            }
        });
        titleBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isMaximized()) {
                    restoreWithoutAnimation();
                }
                stage.setX(mouseEvent.getScreenX() + dragDelta.sx - dragDelta.x);
                stage.setY(mouseEvent.getScreenY() + dragDelta.sy - dragDelta.y);
            }

        });
        titleBar.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    setMaximized(!isMaximized());
                }
            }
        });
        titleBar.setOnDragDetected(evt -> titleBar.startFullDrag());
        titleBar.setOnMouseDragReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (!isMaximized()) {
                    double maxScreenWidth = Screen.getPrimary().getVisualBounds().getWidth();

                    if (event.getScreenY() == 0) {
                        setMaximized(true);
                    } else if (event.getScreenX() == 0) {
                        maximize(MaximizeMode.LEFT);
                    } else if (Math.abs(event.getScreenX() - maxScreenWidth) < 1.01) {
                        maximize(MaximizeMode.RIGHT);
                    }
                }
            }
        });

        parent.getStyleClass().add("parent");
        contentHolder.getStyleClass().add("content-holder");
        titleBar.getStyleClass().add("titleBar");
        content.getStyleClass().add("content");

        HBox titleButtonBox = new HBox(minimize, maximize, close);
        AnchorPane.setTopAnchor(titleButtonBox, 0d);
        AnchorPane.setRightAnchor(titleButtonBox, 0d);
        AnchorPane.setBottomAnchor(titleButtonBox, 0d);

        AnchorPane shadowPane = new AnchorPane();
        DropShadow ds = new DropShadow();
        ds.setRadius(10);
        ds.setColor(Color.rgb(0, 0, 0, 0.4));
        shadowPane.setEffect(ds);
        shadowPane.setStyle("-fx-background-color: white;");
        AnchorPane.setLeftAnchor(shadowPane, resizerWidth + resizeMargin);
        AnchorPane.setTopAnchor(shadowPane, resizerWidth + resizeMargin);
        AnchorPane.setRightAnchor(shadowPane, resizerWidth + resizeMargin);
        AnchorPane.setBottomAnchor(shadowPane, resizerWidth + resizeMargin);

        pane1.getChildren().addAll(pane11, pane12, pane13);
        pane2.getChildren().addAll(pane21, pane22);
        pane3.getChildren().addAll(pane31, pane32, pane33);
        pane0.getChildren().addAll(pane1, pane2, pane3);
        titleBar.getChildren().addAll(/*icon,*/title, /*hamburger,*/ titleButtonBox);
        contentHolder.getChildren().addAll(content, titleBar);
        parent.getChildren().addAll(shadowPane, pane0, /*pane1, pane2, pane3,*/ contentHolder);
        contentHolder.getStyleClass().add("maximize");

        scene = new Scene(parent, 800, 500);
        if (cache) {
            parent.setCache(true);
            parent.setCacheShape(true);
            parent.setCacheHint(CacheHint.QUALITY);
        }
        scene.setFill(null);
        scene.getStylesheets().add("ui/css/MyStage.css");

        this.stage = stage;
        stage.setTitle("Collaborative IDE");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("css/images/icon.png")));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.widthProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!isMaximized) {
                    stageWidth = newValue.doubleValue();
                }
            }
        });
        stage.heightProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!isMaximized) {
                    stageHeight = newValue.doubleValue();
                }
            }
        });
        stage.xProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!isMaximized) {
                    stageX = newValue.doubleValue();
                }
            }
        });
        stage.yProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!isMaximized) {
                    stageY = newValue.doubleValue();
                }
            }
        });

        stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                    Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (cache) {
                        parent.setCache(true);
                        parent.setCacheShape(true);
                        parent.setCacheHint(CacheHint.SPEED);
                    }
                    Timeline timeline = new Timeline(
                            new KeyFrame(
                                    Duration.millis(200),
                                    new KeyValue(scene.getRoot().opacityProperty(), 1, WEB_EASE),
                                    new KeyValue(scene.getRoot().translateYProperty(), 0, WEB_EASE),
                                    new KeyValue(scene.getRoot().scaleXProperty(), 1, WEB_EASE),
                                    new KeyValue(scene.getRoot().scaleYProperty(), 1, WEB_EASE)
                            )
                    );

                    timeline.setOnFinished(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            if (cache) {
                                parent.setCache(false);
                                parent.setCacheShape(false);
                                parent.setCacheHint(CacheHint.DEFAULT);
                            }
                        }
                    });
                    timeline.setDelay(Duration.ZERO);
                    timeline.play();
                }
            }
        });

        setMaximized(true);
        
        parent.setCache(true);
        parent.setCacheShape(true);
        parent.setCacheHint(CacheHint.SPEED);
    }

    public void setRoot(AnchorPane root) {
        this.root = root;

        AnchorPane.setLeftAnchor(root, 0d);
        AnchorPane.setTopAnchor(root, 0d);
        AnchorPane.setRightAnchor(root, 0d);
        AnchorPane.setBottomAnchor(root, 0d);

        //root.getStyleClass().add("root-style");
        content.getChildren().add(root);
    }

    public AnchorPane getRoot() {
        return root;
    }

    public void setMaximized(boolean bool) {
        if (bool) {
            maximize(MaximizeMode.FULL);
        } else {
            restore();
        }
    }

    private void maximize(MaximizeMode mode) {
        if (isMaximized()) {
            return;
        }
        contentHolder.getStyleClass().remove("maximize");
        isMaximized = true;
        maximize.setText(StringEscapeUtils.unescapeJava("\uD83D\uDDD7"));

        stage.hide();

        AnchorPane.setLeftAnchor(contentHolder, 0d);
        AnchorPane.setTopAnchor(contentHolder, 0d);
        AnchorPane.setRightAnchor(contentHolder, 0d);
        AnchorPane.setBottomAnchor(contentHolder, 0d);

        double maxScreenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double maxScreenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        if (mode == MaximizeMode.LEFT || mode == MaximizeMode.FULL) {
            stage.setX(0);
        } else {
            stage.setX(maxScreenWidth / 2);
        }

        stage.setY(0);

        if (mode == MaximizeMode.FULL) {
            stage.setWidth(maxScreenWidth);
        } else {
            stage.setWidth(maxScreenWidth / 2);
        }

        stage.setHeight(maxScreenHeight);
        stage.setOpacity(0);

        parent.setScaleX(0.95);
        parent.setScaleY(0.95);

        if (cache) {
            parent.setCache(true);
            parent.setCacheShape(true);
            parent.setCacheHint(CacheHint.SPEED);
        }
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(300),
                        new KeyValue(stage.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(parent.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(parent.scaleYProperty(), 1, WEB_EASE)
                )
        );
        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (cache) {
                    parent.setCache(false);
                    parent.setCacheShape(false);
                    parent.setCacheHint(CacheHint.DEFAULT);
                }
            }
        });

        timeline.setDelay(Duration.ZERO);
        timeline.play();

        stage.show();
    }

    private void restore() {
        if (!isMaximized()) {
            return;
        }
        contentHolder.getStyleClass().add("maximize");
        maximize.setText(StringEscapeUtils.unescapeJava("\uD83D\uDDD6"));

        stage.hide();

        AnchorPane.setLeftAnchor(contentHolder, resizerWidth + resizeMargin);
        AnchorPane.setTopAnchor(contentHolder, resizerWidth + resizeMargin);
        AnchorPane.setRightAnchor(contentHolder, resizerWidth + resizeMargin);
        AnchorPane.setBottomAnchor(contentHolder, resizerWidth + resizeMargin);

        stage.setX(stageX);
        stage.setY(stageY);
        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);
        stage.setOpacity(0);

        parent.setScaleX(1.05);
        parent.setScaleY(1.05);

        Interpolator WEB_EASE = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);

        if (cache) {
            parent.setCache(true);
            parent.setCacheShape(true);
            parent.setCacheHint(CacheHint.SPEED);
        }

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(300),
                        new KeyValue(stage.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(parent.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(parent.scaleYProperty(), 1, WEB_EASE)
                )
        );

        timeline.setDelay(Duration.ZERO);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (cache) {
                    parent.setCache(false);
                    parent.setCacheShape(false);
                    parent.setCacheHint(CacheHint.DEFAULT);
                }
            }
        });
        timeline.play();
        isMaximized = false;

        stage.show();
    }

    private void restoreWithoutAnimation() {
        if (!isMaximized()) {
            return;
        }
        contentHolder.getStyleClass().add("maximize");
        maximize.setText(StringEscapeUtils.unescapeJava("\uD83D\uDDD6"));

        AnchorPane.setLeftAnchor(contentHolder, resizerWidth + resizeMargin);
        AnchorPane.setTopAnchor(contentHolder, resizerWidth + resizeMargin);
        AnchorPane.setRightAnchor(contentHolder, resizerWidth + resizeMargin);
        AnchorPane.setBottomAnchor(contentHolder, resizerWidth + resizeMargin);

        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);

        stage.show();

        isMaximized = false;
    }

    public boolean isMaximized() {
        return isMaximized;
    }

    public void show() {
        Parent root = parent;
        root.setScaleX(0.95);
        root.setScaleY(0.95);
        root.setOpacity(0);

        if (cache) {
            parent.setCache(true);
            parent.setCacheShape(true);
            parent.setCacheHint(CacheHint.SCALE);
        }

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(400),
                        new KeyValue(root.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(root.scaleYProperty(), 1, WEB_EASE),
                        new KeyValue(root.opacityProperty(), 1, WEB_EASE)
                )
        );
        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (cache) {
                    parent.setCache(false);
                    parent.setCacheShape(false);
                    parent.setCacheHint(CacheHint.DEFAULT);
                }
            }
        });

        timeline.setDelay(Duration.millis(600));
        timeline.play();
        stage.show();
    }

    public void close() {
        Parent root = parent;

        if (cache) {
            parent.setCache(true);
            parent.setCacheShape(true);
            parent.setCacheHint(CacheHint.SPEED);
        }

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(200),
                        new KeyValue(root.scaleXProperty(), 0.95, WEB_EASE),
                        new KeyValue(root.scaleYProperty(), 0.95, WEB_EASE),
                        new KeyValue(root.opacityProperty(), 0, WEB_EASE)
                )
        );

        timeline.setDelay(Duration.ZERO);
        timeline.play();
        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.hide();
                System.exit(0);
            }
        });
    }

    public void setIcon(Image image) {

    }

    public void setColorMode(ColorMode mode) {
        if (mode == ColorMode.BLACK) {
            minimize.getStyleClass().remove("title-button-white");
            maximize.getStyleClass().remove("title-button-white");
            close.getStyleClass().remove("close-button-white");

            minimize.getStyleClass().add("title-button-black");
            maximize.getStyleClass().add("title-button-black");
            close.getStyleClass().add("close-button-black");

            //title.setTextFill(Color.rgb(0, 0, 0, 0.8));
        } else {
            minimize.getStyleClass().remove("title-button-black");
            maximize.getStyleClass().remove("title-button-black");
            close.getStyleClass().remove("close-button-black");

            minimize.getStyleClass().add("title-button-white");
            maximize.getStyleClass().add("title-button-white");
            close.getStyleClass().add("close-button-white");

            //title.setTextFill(Color.rgb(255, 255, 255, 0.9));
        }
    }

    private class Resizer {

        private double x, y;
        private double sw, sh;
        private double sx, sy;

        public void reset(MouseEvent mouseEvent) {
            this.x = mouseEvent.getScreenX();
            this.y = mouseEvent.getScreenY();

            this.sx = stage.getX();
            this.sy = stage.getY();

            this.sw = stage.getWidth();
            this.sh = stage.getHeight();

            if (cache) {
                parent.setCache(true);
                parent.setCacheShape(true);
                parent.setCacheHint(CacheHint.SPEED);
            }
        }

        public void resizeSouthWest(MouseEvent mouseEvent) {
            resizeSouth(mouseEvent);
            resizeWest(mouseEvent);
        }

        public void resizeSouthEast(MouseEvent mouseEvent) {
            resizeSouth(mouseEvent);
            resizeEast(mouseEvent);
        }

        public void resizeNorthEast(MouseEvent mouseEvent) {
            resizeNorth(mouseEvent);
            resizeEast(mouseEvent);
        }

        public void resizeNorthWest(MouseEvent mouseEvent) {
            resizeNorth(mouseEvent);
            resizeWest(mouseEvent);
        }

        public void resizeWest(MouseEvent mouseEvent) {
            double differenceX = mouseEvent.getScreenX() - x;

            if (sw + differenceX > maxWidth) {
                stage.setWidth(sw + differenceX);
            }
        }

        public void resizeEast(MouseEvent mouseEvent) {
            double differenceX = mouseEvent.getScreenX() - x;

            if (sw - differenceX > maxWidth) {
                stage.setX(sx + differenceX);
                stage.setWidth(sw - differenceX);
            }
        }

        public void resizeNorth(MouseEvent mouseEvent) {
            double differenceY = mouseEvent.getScreenY() - y;

            if (sh - differenceY > maxHeight) {
                stage.setY(sy + differenceY);
                stage.setHeight(sh - differenceY);
            }
        }

        public void resizeSouth(MouseEvent mouseEvent) {
            double differenceY = mouseEvent.getScreenY() - y;

            if (sh + differenceY > maxHeight) {
                stage.setHeight(sh + differenceY);
            }
        }

        public void release(MouseEvent evt) {
            if (cache) {
                parent.setCache(false);
                parent.setCacheShape(false);
                parent.setCacheHint(CacheHint.DEFAULT);
            }
        }
    }

    public static enum MaximizeMode {

        LEFT, RIGHT, FULL;
    }

    public static enum ColorMode {

        BLACK, WHITE
    }
}
