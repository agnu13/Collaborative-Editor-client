package codeEditor.eventNotification;

import codeEditor.operation.Operation;
import java.util.ArrayList;

public class EventNotification implements EventSubject{
    
    ArrayList<EventObserver> observerList = new ArrayList<>();  
    
    @Override
    public void addObserver(EventObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void notifyObservers(Operation operation) {
        for (EventObserver observer: observerList) {
            observer.notifyObserver(operation);
        }
    }
}
