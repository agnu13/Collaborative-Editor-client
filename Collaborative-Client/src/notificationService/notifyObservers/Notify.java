package notificationService.notifyObservers;

import java.util.ArrayList;
import notificationService.Notification;

public class Notify implements NotificationSubject{
    
    ArrayList<NotificationObserver> observerList = new ArrayList<>();  
    
    @Override
    public void addObserver(NotificationObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void notifyObservers(ArrayList<Notification> notifications) {
        for (NotificationObserver observer: observerList) {
            observer.notifyObserver(notifications);
        }
    }
}
