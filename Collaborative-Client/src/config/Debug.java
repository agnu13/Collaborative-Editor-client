package config;

public class Debug {
    public static boolean POLL_THREAD_DEBUG = false;
    public static boolean PUSH_THREAD_DEBUG = false;
    public static boolean KEYLOG_DEBUG = false;
    public static boolean PROJECT_MANAGER_DEBUG = false;
    public static boolean EXECUTOR_DEBUG = false;
    
    public static void log(String s, boolean flag) {
        if (flag) {
            System.err.println(s);
        }
    }
}
