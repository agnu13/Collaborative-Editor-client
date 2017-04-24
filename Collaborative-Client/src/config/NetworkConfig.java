package config;

import static config.ConfigFilePaths.NETWORK_CONF_FILE_PATH;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public abstract class NetworkConfig {
    //Default Values;
    public static String SERVER_ADDRESS = "http://127.0.0.1:3000";
   
    //Registers the user for the first time
    public static String REGISTER = "register";
    public static String UNREGISTER = "unregister";
    
    //Push and Get Operations
    public static String PUSH_OPERATIONS = "push_operation";
    public static String GET_OPERATIONS = "get_operation";
    
    //Signup and Authenticate
    public static String SIGNUP = "signup";
    public static String LOGIN = "login";
    public static String GET_USERINFO = "get_info";
    
    //Project_View_and_Create
    public static String PROJECT_VIEW = "view";
    public static String PROJECT_ADD_NODE = "add_node";
    
    //Share Doc and View Users
    public static String SHARE = "share";
    public static String GET_USERS = "get_users";
    public static String SHARE_VIEW = "get_shared_projects";
    
    //Notification Service
    public static String NOTIFICATIONS = "get_notifications";
    public static String CLEAR_NOTIFICATION = "clear_notifications";
    
    //Polling Thread Sleep Time
    public static int POLLING_THREAD_SLEEP_TIME = 500;
    public static int NOTIFICATIONS_THREAD_SLEEP_TIME = 60000;
    
    public static void getConfig(){
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(NETWORK_CONF_FILE_PATH));
            
            String SERVER_IP = properties.getProperty("SERVER_IP");
            String SERVER_PORT = properties.getProperty("SERVER_PORT");  
            SERVER_ADDRESS = "http://" + SERVER_IP + ":" + SERVER_PORT;
            
            POLLING_THREAD_SLEEP_TIME  = Integer.parseInt(properties.getProperty("POLLING_THREAD_SLEEP_TIME"));
            NOTIFICATIONS_THREAD_SLEEP_TIME  = Integer.parseInt(properties.getProperty("NOTIFICATIONS_THREAD_SLEEP_TIME"));            
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException. Error Reading Config File.");
            e.printStackTrace(System.err);
        } catch (IOException e) {
            System.out.println("IO Exception. Error Reading Config File.");
            e.printStackTrace(System.err);
        }
    }
}
