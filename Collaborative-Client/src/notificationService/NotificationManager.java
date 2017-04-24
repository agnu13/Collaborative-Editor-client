package notificationService;

import codeEditor.networkLayer.SendPostRequest;
import static config.NetworkConfig.CLEAR_NOTIFICATION;
import static config.NetworkConfig.SERVER_ADDRESS;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.util.ArrayList;
import notificationService.notifyObservers.NotificationObserver;
import notificationService.notifyObservers.Notify;
import notificationService.notifyObservers.NotificationSubject;
import urlbuilder.URLBuilder;

public class NotificationManager {
    private final String userId;
    public ArrayList<Notification> notificationPool = new ArrayList<>();
    
    NotificationSubject notify = new Notify();
    NotificationService notificationService;
    
    public NotificationManager(String userId) {
        this.userId = userId;
        notificationService = new NotificationService(userId, this);
    }
    
    public void start() {
        notificationService.start();
    }
    
    public void register(NotificationObserver observer) {
        notify.addObserver(observer);
    }
    
    public void addNotifications(ArrayList<Notification> notifications) {
        notificationPool.addAll(notifications);
        notify.notifyObservers(notifications);
    }

    public void clearNotifications() throws ConnectivityFailureException {
        
        URLBuilder urlBuilder = new URLBuilder(); 
        urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(CLEAR_NOTIFICATION).toString();
        urlBuilder.addParameter("userId", userId);
        String url = urlBuilder.toString();
        
        try {
            SendPostRequest.sendPostRequest(url, "");
        } catch (IOException ex) {
            throw new ConnectivityFailureException("Unable to Connect");
        }    
    }
    
}
