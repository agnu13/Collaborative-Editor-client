package codeEditor.sessionLayer;

import codeEditor.buffer.SynchronizedBuffer;
import codeEditor.buffer.Buffer;
import codeEditor.dataControl.Editor;
import codeEditor.dataControl.EditorCore;
import codeEditor.dataControl.Executor;
import codeEditor.eventNotification.EventSubject;
import codeEditor.eventNotification.EventNotification;
import codeEditor.networkLayer.PollService;
import codeEditor.networkLayer.PushService;
import codeEditor.transform.Transformation;

public class SessionFactory extends AbstractSessionFactory{

    @Override
    public PollService createPollService() {
        return new PollService();
    }

    @Override
    public PushService createPushService(String userId, String docId, Buffer requestBuffer) {
        return  new PushService(userId, docId, requestBuffer); 
    }
    @Override
    public Editor createEditorInstance(String userId, String docId, EventSubject notificationService, AbstractSession session) {
        return new EditorCore(userId, docId, notificationService, session);
    }

    @Override
    public Buffer createBuffer() {
        return new SynchronizedBuffer();
    }

    @Override
    public Transformation createTranformation(String userId) {
        return new Transformation(userId);    
    }

    @Override
    public EventNotification createNotificationService() {
        return new EventNotification();
    }

    @Override
    public Executor createExecutor(Editor editorCore, Buffer operationBuffer) {
        return new Executor(editorCore, operationBuffer);
    }
    
}
