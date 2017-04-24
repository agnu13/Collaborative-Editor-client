package codeEditor.sessionLayer;

import codeEditor.dataControl.Editor;
import codeEditor.dataControl.Executor;
import codeEditor.eventNotification.EventSubject;
import codeEditor.transform.Transformation;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.EventObserver;
import codeEditor.networkLayer.PollService;
import codeEditor.networkLayer.PushService;
import config.Configuration;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractSession {
    
    protected final String docId;
    protected final String userId;
    
    protected volatile int lastSynchronized;
    protected final ReentrantLock updateState = new ReentrantLock();
    
    protected final Editor model;
    
    protected final Buffer pushBuffer;
    protected final Buffer executeBuffer;
   
    protected final Executor executor;
    
    protected final Transformation transformation;
   
    protected final PushService pushService; 
    protected final PollService pollService;
    
    protected final EventSubject eventNotification;
    
    protected EventObserver workspace;
    
    public AbstractSession(String userId, String docId) {
        
        Configuration.getConfiguration();
        
        this.userId = userId;
        this.docId = docId;
        
        AbstractSessionFactory sessionFactory = new SessionFactory();

        eventNotification = sessionFactory.createNotificationService();
        model = sessionFactory.createEditorInstance(userId, docId, eventNotification, this);
        pushBuffer = sessionFactory.createBuffer();
        executeBuffer = sessionFactory.createBuffer();
        transformation = sessionFactory.createTranformation(userId);
        pushService = sessionFactory.createPushService(userId, docId, pushBuffer);
        pollService = sessionFactory.createPollService()
                    .setUserId(userId)
                    .setDocId(docId)
                    .setEditor(model)
                    .setTranformation(transformation)
                    .setSession(this);
        executor = sessionFactory.createExecutor(model, executeBuffer);
    }
    
    //Starts and Stops the session
    public String startSession() {
        //Register user on doc
        new RegisterUser(userId, docId, executor).registerUserOnDoc();
  
        pushService.start();
        pollService.start();
        executor.start();
       
        return userId;
    }
    
    public void stopSession() {
        pushService.interrupt();
        pollService.interrupt();
        executor.interrupt();
        
        //Unregister user from doc
        new UnregisterUser(userId, docId, executor).unregisterUser();
    }
    
    //Set Reflect Operations Flag
    public void setReflectOperations(boolean flag){
        model.setReflectOperations(flag);
    }
       
    //Register the user for updates from remote
    public void register(EventObserver observer) {
        this.workspace = observer;
        this.eventNotification.addObserver(observer);
    }
        
    
    //Get the last time session is synchronized with remote
    public synchronized int getLastSynchronized() {
        return lastSynchronized;
    }

    public synchronized void setLastSynchronized(int lastSynchronized) {
        this.lastSynchronized = lastSynchronized;
    }
    
 
    //Lock and Unlock Session
    public void lockWorkspace() throws InterruptedException {
        //no operation can be performed on session until session is unlocked 
        updateState.lock();
        workspace.setReadOnly();
    }
    
    public void unlockWorkspace() {
        workspace.unsetReadOnly();
        updateState.unlock();
    }
    
    public void lockMutex() {
        updateState.lock();
    }
    
    public void unlockMutex() {
        updateState.unlock();
    }
}

