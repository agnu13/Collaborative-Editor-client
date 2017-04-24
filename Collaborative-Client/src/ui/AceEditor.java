package ui;

import ui.util.GlobalSettings;
import ui.util.Work;
import ui.util.RandomGenerator;
import authenticate.entities.User;
import codeEditor.eventNotification.EventObserver;
import codeEditor.operation.OperationType;
import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.RepositionOperation;
import codeEditor.sessionLayer.Session;
import codeEditor.utility.RandomGen;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.Debug;
import static config.Debug.KEYLOG_DEBUG;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import ui.nodes.DocumentTab;

public class AceEditor extends Region implements EventObserver {

    private static final Queue<AceEditor> pool = new LinkedList<>();

    private Session session;
    private String userIdentifier;
    private String text;
    private boolean loaded;

    private final WebView webView = new WebView();
    private final WebEngine webEngine = webView.getEngine();

    private final HashMap<String, String> editUserColor = new HashMap<>();
    private final HashMap<String, Integer> editUserPosition = new HashMap<>();

    private final Stack<String> colorPool = new Stack();
    private Timeline cursorSender;
    private DocumentTab documentTab;

    public static AceEditor getInstance(DocumentTab documentTab, Session session, User user, String tempUserID) {
        AceEditor ace;

        /*if (pool.isEmpty()) {
         ace = new AceEditor();
         } else {
         ace = pool.poll();
         }*/
        ace = new AceEditor();

        ace.editUserColor.clear();
        ace.editUserPosition.clear();
        ace.colorPool.clear();
        ace.colorPool.add("#7DC4E8");
        ace.colorPool.add("#81A480");
        ace.colorPool.add("#BC9C63");
        ace.colorPool.add("#7AB900");
        ace.colorPool.add("#DB6559");
        ace.colorPool.add("#A766DA");
        ace.colorPool.add("#5CD0CE");
        Collections.shuffle(ace.colorPool);

        ace.documentTab = documentTab;
        ace.init(session, user, tempUserID);
        return ace;
    }

    public static void generatePool() {
        for (int i = 0; i < 10; i++) {
            pool.add(new AceEditor());
        }
    }

    private AceEditor() {
        getStyleClass().add("browser");
        String path = System.getProperty("user.dir");
        path = path.replace("\\\\", "/");
        path += "/ace/editor.html";

        /*editUserColor.put("Ashish", "#7DC4E8");
         editUserColor.put("Swapnil", "#81A480");

         editUserPosition.put("Ashish", 60);
         editUserPosition.put("Swapnil", 70);*/
        webEngine.load("file:///" + path);
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                //System.out.println("new State " + newState);

                if (newState == Worker.State.SUCCEEDED) {
                    loaded = true;

                    //webEngine.executeScript("addMarker(\"Ashish\", \"#7DC4E8\", 10)");
                    GlobalSettings settings = GlobalSettings.getInstance();
                    settings.getCursorNameProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (newValue == true) {
                            showAllCursors();
                        } else {
                            hideAllCursors();
                        }
                    });
                }
            }
        });

        JSObject win = (JSObject) webEngine.executeScript("window");
        win.setMember("javaObject", this);

        webView.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.V) {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();

                    String content = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
                    content = encodeString(content);

                    webEngine.executeScript("pasteContent(\"" + content + "\")");
                } else if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.C) {
                    String myString = (String) webEngine.executeScript("getCopyString()");
                    StringSelection stringSelection = new StringSelection(myString);
                    java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clpbrd.setContents(stringSelection, null);
                }
            }
        });

        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {

            public void handle(WebEvent<String> we) {
                System.out.println("Alert received: " + we.getData());
                if (we.getData() != null && we.getData().startsWith("copy: ")) {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(we.getData().substring(6));
                    clipboard.setContent(content);
                }
            }
        });
        getChildren().add(webView);
    }

    public void init(Session session, User user, String tempUserID) {
        Work work = () -> {
            this.session = session;
            //session = new Session("user1", "abcd");
            session.register(AceEditor.this);
            userIdentifier = session.startSession();

            if (cursorSender != null) {
                cursorSender.stop();
            }

            cursorSender = new Timeline(new KeyFrame(Duration.millis(200), evt -> {
                //System.out.println("cursor: " + getCursorPosition());
                RepositionOperation op = new RepositionOperation(RandomGen.getRandom(), tempUserID/*user.getUserIdentifier()*/, getCursorPosition(), user.getName());
                //System.out.println("sent: " + user.getUserIdentifier());
                session.pushOperation(op);
            }));
            cursorSender.setCycleCount(Animation.INDEFINITE);
            cursorSender.play();
        };

        performWhenLoaded(work);
    }

    public void destroy() {
        session.stopSession();
        cursorSender.stop();
        pool.add(this);
    }

    private void insertStringAtPosition(int index, String txt) {
        Platform.runLater(() -> {
            webEngine.executeScript("insert(" + index + ",\"" + encodeString(txt) + "\")");

            if (GlobalSettings.getInstance().getCursorNameProperty().get()) {
                showAllCursors();
            }
        });
    }

    
    public  void setReadOnly() {
        Platform.runLater(() -> {
            webEngine.executeScript("setReadOnly()");
        });
    }
    
    public void unsetReadOnly() {
        Platform.runLater(() -> {
            webEngine.executeScript("unsetReadOnly()");    
        });
    }
    
    
    private void performBackSpaceAtPosition(int index) {
        Platform.runLater(() -> {
            webEngine.executeScript("erase(" + (index) + ")");
        });
    }

    private int getCursorPosition() {
        return Integer.parseInt((String) webEngine.executeScript("getCursorPosition()"));
    }

    private void showAllCursors() {
        webEngine.executeScript("hideAllMarkers()");
        for (String name : editUserColor.keySet()) {
            String str1 = name;
            String str2 = editUserColor.get(name);
            Integer str3 = editUserPosition.get(name);
            
            str1 = str1.replace(" ", "_");
            
            webEngine.executeScript("addMarker(\"" + str1 + "\", \"" + str2 + "\", " + str3 + ")");
        }
    }

    private void hideAllCursors() {
        webEngine.executeScript("hideAllMarkers()");
    }

    public void print(String txt) {
        System.out.println(txt);
    }

    public synchronized void notifyTextChange(String json, int start, int end) {
        Debug.log("Key Pressed", KEYLOG_DEBUG);
        try {
            session.lockMutex();
        
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            TextChange change = gson.fromJson(json, TextChange.class);
            if ("remove".equals(change.action)) {
                int size = 0;
                for (int i = 0; i < change.lines.size(); i++) {
                    String line = change.lines.get(i);
                    size += line.length();
                    if (i + 1 < change.lines.size()) {
                        size++;
                    }
                }
                end = start + size;

                for (int i = end - 1; i >= start; i--) {
                    EraseOperation eraseOperation = new EraseOperation(RandomGenerator.getRandom(), userIdentifier, i);
                    session.pushOperation(eraseOperation);
                }
                return;
            }
            if (change.lines.isEmpty()) {
                return;
            }
            int ind = start;
            for (int i = 0; i < change.lines.size(); i++) {
                for (int j = 0; j < change.lines.get(i).length(); j++) {
                    char c = change.lines.get(i).charAt(j);

                    InsertOperation insertOperation = new InsertOperation(RandomGenerator.getRandom(), userIdentifier, ind++, c);
                    session.pushOperation(insertOperation);
                }

                if (i + 1 < change.lines.size()) {
                    InsertOperation insertOperation = new InsertOperation(RandomGenerator.getRandom(), userIdentifier, ind++, '\n');
                    session.pushOperation(insertOperation);
                }
            }
            
        } finally {
            session.unlockMutex();
        }
    }

    private String encodeString(String content) {
        content = content.replaceAll("\r\n", "\n");
        int count = 0;
        StringBuilder ssb = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '\n') {
                count++;
            } else {
                while (count >= 1) {
                    ssb.append("\n");
                    count--;
                }
                ssb.append(content.charAt(i));
            }
        }
        while (count >= 1) {
            ssb.append("\n");
            count--;
        }
        content = ssb.toString();

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < content.length(); i++) {
            sb.append("" + ((int) content.charAt(i)));
            if (i + 1 < content.length()) {
                sb.append(((char) 127));
            }
        }

        return sb.toString();
    }

    public String getText() {
        text = (String) webEngine.executeScript("getText()");
        return text;
    }

    public void setText(String txt) {
        webEngine.executeScript("setText(\"" + encodeString(txt) + "\")");
    }

    public void performWhenLoaded(Work work) {
        if (isLoaded()) {
            work.doWork();
        } else {
            webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                    if (newState == Worker.State.SUCCEEDED) {
                        work.doWork();
                    }
                }
            });
        }
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(webView, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height) {
        return 750;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 500;
    }

    @Override
    public void notifyObserver(Operation operation) {
        if (operation.getType() == OperationType.INSERT) {
            InsertOperation insertOperation = (InsertOperation) operation;

            Platform.runLater(() -> {
                insertStringAtPosition(insertOperation.position, "" + insertOperation.charToInsert);
            });
        } else if (operation.getType() == OperationType.ERASE) {
            EraseOperation eraseOperation = (EraseOperation) operation;
            Platform.runLater(() -> {
                performBackSpaceAtPosition(eraseOperation.position);
            });
        } else if (operation.getType() == OperationType.REPOSITION) {
            RepositionOperation repositionOperation = (RepositionOperation) operation;

            if (editUserPosition.containsKey(repositionOperation.username)) {
                editUserPosition.put(repositionOperation.username, repositionOperation.position);
            } else {
                editUserColor.put(repositionOperation.username, colorPool.pop());
                editUserPosition.put(repositionOperation.username, repositionOperation.position);
            }

            //System.out.println("received: " + repositionOperation.userId);
            Platform.runLater(() -> {
                GlobalSettings settings = GlobalSettings.getInstance();
                if (settings.getCursorNameProperty().get()) {
                    hideAllCursors();
                    showAllCursors();
                } else {
                    hideAllCursors();
                }

                documentTab.updateOnlineUsers(editUserColor);
            });
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    private class TextChange {

        Position start, end;
        String action;

        ArrayList<String> lines;
    }

    private class Position {

        int row, column;
    }
}
