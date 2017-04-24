package ui.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class GlobalSettings {

    private static GlobalSettings instance;

    private final BooleanProperty showNotificationsProperty = new SimpleBooleanProperty();
    private final BooleanProperty notificationsSoundProperty = new SimpleBooleanProperty();
    private final BooleanProperty cursorNameProperty = new SimpleBooleanProperty();
    private final BooleanProperty projectsPaneProperty = new SimpleBooleanProperty();

    public static GlobalSettings getInstance() {
        if (instance == null) {
            InputStream inpStream = null;
            try {
                Properties prop = new Properties();
                inpStream = new FileInputStream("config/settings.prop");
                prop.load(inpStream);

                boolean b1 = Boolean.parseBoolean(prop.getProperty("notiPopup"));
                boolean b2 = Boolean.parseBoolean(prop.getProperty("notiSound"));
                boolean b3 = Boolean.parseBoolean(prop.getProperty("cursorName"));
                boolean b4 = Boolean.parseBoolean(prop.getProperty("projectPane"));

                instance = new GlobalSettings();

                instance.showNotificationsProperty.set(b1);
                instance.notificationsSoundProperty.set(b2);
                instance.cursorNameProperty.set(b3);
                instance.projectsPaneProperty.set(b4);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(GlobalSettings.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GlobalSettings.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    inpStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(GlobalSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return instance;
    }

    private GlobalSettings() {
        showNotificationsProperty.addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                try {
                    Properties prop = new Properties();
                    InputStream inpStream = new FileInputStream("config/settings.prop");
                    prop.load(inpStream);
                    prop.setProperty("notiPopup", "" + newValue);
                    prop.store(new PrintWriter("config/settings.prop"), null);

                    inpStream.close();
                } catch (Exception ex) {
                    Logger.getLogger(GlobalSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        notificationsSoundProperty.addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                try {
                    Properties prop = new Properties();
                    InputStream inpStream = new FileInputStream("config/settings.prop");
                    prop.load(inpStream);
                    prop.setProperty("notiSound", "" + newValue);
                    prop.store(new PrintWriter("config/settings.prop"), null);

                    inpStream.close();
                } catch (Exception ex) {
                    Logger.getLogger(GlobalSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        cursorNameProperty.addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                try {
                    Properties prop = new Properties();
                    InputStream inpStream = new FileInputStream("config/settings.prop");
                    prop.load(inpStream);
                    prop.setProperty("cursorName", "" + newValue);
                    prop.store(new PrintWriter("config/settings.prop"), null);

                    inpStream.close();
                } catch (Exception ex) {
                    Logger.getLogger(GlobalSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        projectsPaneProperty.addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                try {
                    Properties prop = new Properties();
                    InputStream inpStream = new FileInputStream("config/settings.prop");
                    prop.load(inpStream);
                    prop.setProperty("projectPane", "" + newValue);
                    prop.store(new PrintWriter("config/settings.prop"), null);

                    inpStream.close();
                } catch (Exception ex) {
                    Logger.getLogger(GlobalSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public BooleanProperty getShowNotificationsProperty() {
        return showNotificationsProperty;
    }

    public BooleanProperty getNotificationsSoundProperty() {
        return notificationsSoundProperty;
    }

    public BooleanProperty getCursorNameProperty() {
        return cursorNameProperty;
    }

    public BooleanProperty getProjectsPaneProperty() {
        return projectsPaneProperty;
    }
}
