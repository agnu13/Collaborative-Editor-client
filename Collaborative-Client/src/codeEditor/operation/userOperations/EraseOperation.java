package codeEditor.operation.userOperations;

import com.google.gson.Gson;
import codeEditor.operation.OperationType;
import codeEditor.operation.Operation;

public final class EraseOperation extends Operation implements UserOperations {
    public int synTimeStamp;
    public int position;
    
    public EraseOperation(String operationId, String userId, int position) {
        super(operationId,  userId, OperationType.ERASE);
        this.position = position;
    }
    
    public EraseOperation(String json) {
        this.deserialize(json);
    }
    
    public EraseOperation(EraseOperation o) {
        super(o.operationId, o.userId, o.type);
        this.position = o.position;
        this.synTimeStamp = o.synTimeStamp;
    }
    
    //Getters and Setters
    public synchronized EraseOperation setSynTimeStamp(int timeStamp) {
        this.synTimeStamp = timeStamp;
        return this;
    }
    
    public synchronized int getSynTimeStamp() {
        return this.synTimeStamp;
    }
    
    @Override
    public final String serialize() {
        return new Gson().toJson(this);
    }
    
    @Override
    public void deserialize(String json) {
        Gson gson = new Gson();
        EraseOperation jsonEntity = gson.fromJson(json, EraseOperation.class);
        this.position = jsonEntity.position;
        this.userId = jsonEntity.userId;
        this.operationId = jsonEntity.operationId;
        this.type = OperationType.ERASE;
        this.synTimeStamp = jsonEntity.synTimeStamp;
    }
    
    @Override
    public String toString() {
        String s = "";
        s += "OperationId: " + operationId + "\n";
        s += "UserId: " + userId + "\n";
        s += "Type: " + type + "\n";
        s += "Position: " + position + "\n";
        s += "SynTimeStamp: " + synTimeStamp + "\n";
        return s;
    }
}
