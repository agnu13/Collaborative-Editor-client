package notificationService;

import codeEditor.networkLayer.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import static config.NetworkConfig.NOTIFICATIONS;
import static config.NetworkConfig.NOTIFICATIONS_THREAD_SLEEP_TIME;
import static config.NetworkConfig.SERVER_ADDRESS;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import urlbuilder.URLBuilder;

public final class NotificationService extends Thread {
    private final String userId;
    private final NotificationManager notificationManager;
    
    public NotificationService(String userId, NotificationManager notificationManager){
        this.notificationManager = notificationManager;
        this.userId = userId;
    }
    
    public void handleRequest(Request request) {
        try {
            HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());    
            handleResponse(response);
        } catch (IOException e) {
            System.err.println("Unable to send HTTP request. Connection Refused. Retrying...");
        }
    }
    
    public void handleResponse(HttpResponse response){
        HttpEntity httpEntity = response.getEntity();
        try {
            InputStream inStream = httpEntity.getContent();
            String content = IOUtils.toString(inStream);
            if (content.equals("")) {
            } else {
                java.lang.reflect.Type listType = new TypeToken<ArrayList<Notification>>() {}.getType();
                ArrayList<Notification> list = new Gson().fromJson(content, listType);
                notificationManager.addNotifications(list);
            }
        } catch (IOException | UnsupportedOperationException ex) {
            ex.printStackTrace(System.err);
        } 
        //System.out.println("Response Received");
    }
    
    @Override
    public void run(){
        
        URLBuilder urlBuilder = new URLBuilder(); 
        urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(NOTIFICATIONS).toString();
        urlBuilder.addParameter("userId", userId);
        String url = urlBuilder.toString();
        
        while (true) {
            handleRequest(new Request(url, ""));
            try {
                Thread.sleep(NOTIFICATIONS_THREAD_SLEEP_TIME);
            } catch (InterruptedException ex) {
            }
        }
    }
}
