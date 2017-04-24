package notificationService.notifyObservers;

import java.util.ArrayList;
import notificationService.Notification;

public interface NotificationObserver {
    void notifyObserver(ArrayList<Notification> operation);
}
