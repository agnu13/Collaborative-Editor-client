package codeEditor.sessionLayer;

import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.RepositionOperation;
import config.Debug;
import static config.Debug.KEYLOG_DEBUG;

public class Session extends AbstractSession {
    
    public Session(String userId, String docId) {
        super(userId, docId);
    }
    
    public void pushOperation(InsertOperation insertOperation){
        Debug.log("Key Pressed Notified", KEYLOG_DEBUG);
        
        insertOperation.setSynTimeStamp(this.getLastSynchronized());
        executor.pushOperation((Operation) insertOperation);
        transformation.addOperation(insertOperation);
        try {
            pushBuffer.put(insertOperation);
        } catch(InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    public void pushOperation(EraseOperation eraseOperation) {
        Debug.log("Key Pressed Notified", KEYLOG_DEBUG);
        
        eraseOperation.setSynTimeStamp(this.getLastSynchronized());
        executor.pushOperation((Operation) eraseOperation);
        transformation.addOperation(eraseOperation);
        try {
            pushBuffer.put(eraseOperation);
        } catch(InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    public void pushOperation(RepositionOperation repositionOperation) {
        try {
            pushBuffer.put(repositionOperation);    
        } catch(InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
    }
}