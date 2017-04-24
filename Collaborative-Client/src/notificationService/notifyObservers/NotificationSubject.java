package notificationService.notifyObservers;

import java.util.ArrayList;
import notificationService.Notification;


public interface NotificationSubject {
    public void addObserver(NotificationObserver observer);
    public void notifyObservers(ArrayList<Notification> notifications);
}
