package codeEditor.operation.userOperations;

import com.google.gson.Gson;
import codeEditor.operation.OperationType;
import codeEditor.operation.Operation;

public class RepositionOperation extends Operation implements UserOperations {
    public int position;
    public String username;
    
    public RepositionOperation(String operationId, String userId, int position, String username) {
        super(operationId,  userId, OperationType.REPOSITION);
        this.position = position;
        this.username = username;
    }
    
    public RepositionOperation(String json) {
        this.deserialize(json);
    }
    
    
    public RepositionOperation(RepositionOperation o) {
        super(o.operationId, o.userId, o.type);
        this.position = o.position;
        this.username = o.username;
    }
    
    @Override
    public final String serialize() {
        return new Gson().toJson(this);
    }
    
    @Override
    public final void deserialize(String json) {
        Gson gson = new Gson();
        RepositionOperation jsonEntity = gson.fromJson(json, RepositionOperation.class);
        this.position = jsonEntity.position;
        this.username = jsonEntity.username;
        
        this.userId = jsonEntity.userId;
        this.operationId = jsonEntity.operationId;
        this.type = OperationType.REPOSITION;
    }
    
    @Override
    public String toString() {
        String s = "";
        /*s += "OperationId: " + operationId + "\n";
        s += "UserId: " + userId + "\n";
        s += "Type: " + type + "\n";
        s += "Position: " + position + "\n";*/
        return s;
    }
}
