package codeEditor.eventNotification;

import codeEditor.operation.Operation;

public interface EventObserver extends Lockable{
    void notifyObserver(Operation operation);
}
