package codeEditor.eventNotification;

import codeEditor.operation.Operation;

public interface EventSubject {
    public void addObserver(EventObserver observer);
    public void notifyObservers(Operation operation);
}
