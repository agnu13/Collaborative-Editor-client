package codeEditor.dataControl;

import codeEditor.buffer.SynchronizedBuffer;
import codeEditor.buffer.Buffer;
import codeEditor.model.Model;
import codeEditor.model.Treap;
import codeEditor.eventNotification.EventSubject;
import codeEditor.operation.Operation;
import codeEditor.operation.OperationType;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.sessionLayer.AbstractSession;
import exception.OperationNotSupported;

public class EditorCore implements Editor {
    
    Buffer operationBuffer = new SynchronizedBuffer();
    
    private final Model dataModel;
    private final String userId;
    private final String docId;
    private final AbstractSession session;
    private final EventSubject notificationService; 
    
    private boolean REFLECT_OPERATIONS_FLAG = false;
    
    public EditorCore(String userId, String docId, EventSubject notificationService, AbstractSession session) {
        this.userId = userId;
        this.docId = docId;    
        this.dataModel = new Treap();
        this.notificationService = notificationService;
        this.session = session;
    }
    
    private void insertCharacter(int positionToInsert, char character) {
        dataModel.insert(positionToInsert, character);
    }

    private void eraseCharacter(int positionToErase) {
        dataModel.erase(positionToErase);
    }
    
    @Override
    public char charAt(int positionToSeek) {
        return dataModel.charAt(positionToSeek);
    }
    
    @Override
    public int getSize() {
        return dataModel.getSize();
    }
    
    @Override
    public void performOperation(Operation operation) {
        try {
            //perform The operation on the model
            int lastSynchronized = -1;
            switch(operation.getType().toString()) {
                case "INSERT":{
                    InsertOperation insertOperation = (InsertOperation) operation;
                    this.insertCharacter(insertOperation.position, insertOperation.charToInsert); 
                    lastSynchronized = insertOperation.synTimeStamp+1;
                } break;
                case "ERASE": {
                    EraseOperation eraseOperation = (EraseOperation) operation;
                    this.eraseCharacter(eraseOperation.position);
                    lastSynchronized = eraseOperation.synTimeStamp+1;    
                } break;
                case "REPOSITION": {
                } break;    
                default: {
                    throw new OperationNotSupported(operation.getType() + " operation not supported.");
                }
            }
            
            if (operation.userId.equals(this.userId)) {
                if (REFLECT_OPERATIONS_FLAG) {
                    //notifies the user of his own operations
                    notificationService.notifyObservers(operation);
                } else {
                    //ignore if character belong to same user.
                }
            } else {
                //update the lastSynchrnonized 
                if (operation.getType() == OperationType.INSERT || operation.getType() == OperationType.ERASE) {
                    session.setLastSynchronized(lastSynchronized);
                }
                //
                notificationService.notifyObservers(operation);
            }
        } catch (OperationNotSupported e){
            e.printStackTrace(System.err);
        }
    }
    
    @Override
    public void setReflectOperations(boolean flag) {
        this.REFLECT_OPERATIONS_FLAG = flag;
    }
}
