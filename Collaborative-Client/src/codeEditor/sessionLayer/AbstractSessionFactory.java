package codeEditor.sessionLayer;

import codeEditor.dataControl.Editor;
import codeEditor.dataControl.Executor;
import codeEditor.transform.Transformation;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.EventSubject;
import codeEditor.eventNotification.EventNotification;
import codeEditor.networkLayer.PollService;
import codeEditor.networkLayer.PushService;

public abstract class AbstractSessionFactory {
   
    public abstract Buffer createBuffer();
    
    public abstract EventNotification createNotificationService();
    
    public abstract PollService createPollService();
    
    public abstract PushService createPushService
        (String userId, String docId, Buffer requestBuffer);
    
    public abstract Editor createEditorInstance
        (String userId, String docId, EventSubject notificationService, AbstractSession session);
    
    public abstract Transformation createTranformation
        (String userId);
    
    public abstract Executor createExecutor
        (Editor editorCore, Buffer operationBuffer);
}
