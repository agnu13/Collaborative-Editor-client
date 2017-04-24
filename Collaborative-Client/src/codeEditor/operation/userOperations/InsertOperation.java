package codeEditor.operation.userOperations;

import com.google.gson.Gson;
import codeEditor.operation.OperationType;
import codeEditor.operation.Operation;

public class InsertOperation extends Operation implements UserOperations {
    public int synTimeStamp;
    public int position;
    public char charToInsert;
    
    public InsertOperation(String json) {
        deserialize(json);
    }
    
    public InsertOperation(String operationId, String userId, int position, char charToInsert){
        super(operationId, userId, OperationType.INSERT);
        this.charToInsert = charToInsert;       
        this.position = position;
    }
    
    public InsertOperation(InsertOperation o) {
        super(o.operationId, o.userId,  o.type);
        this.charToInsert = o.charToInsert;
        this.position = o.position;    
        this.synTimeStamp = o.synTimeStamp;
    }
    
    @Override
    public final String serialize() {
        return new Gson().toJson(this);
    }
    
    @Override
    public final void deserialize(String json) {
        Gson gson = new Gson();
        InsertOperation jsonEntity = gson.fromJson(json, InsertOperation.class);
        this.charToInsert = jsonEntity.charToInsert;
        this.position = jsonEntity.position;
        this.userId = jsonEntity.userId;
        this.operationId = jsonEntity.operationId;
        this.type = OperationType.INSERT;
        this.synTimeStamp = jsonEntity.synTimeStamp;
    }   

    //Setters and Getters
    public synchronized InsertOperation setSynTimeStamp(int timeStamp) {
        this.synTimeStamp = timeStamp;
        return this;
    }
    
    public synchronized int getSynTimeStamp() {
        return this.synTimeStamp;
    }
    
    @Override
    public String toString() {
        String s = "";
        s += "OperationId: " + operationId + "\n";
        s += "UserId: " + userId + "\n";
        s += "Type: " + type + "\n";
        s += "CharToInsert: " + charToInsert + "\n";
        s += "Position: " + position + "\n";
        s += "SynTimeStamp: " + synTimeStamp + "\n";
        return s;
    }
}