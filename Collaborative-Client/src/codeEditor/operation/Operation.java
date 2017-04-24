package codeEditor.operation;

public abstract class Operation {
    public String operationId;
    public String userId;
    public OperationType type;
    
    public Operation() {    
    }
    
    public Operation(String operationId, String userIdentifier, OperationType type) {
        this.operationId = operationId;
        this.type = type;
        this.userId = userIdentifier;
    }
    
    public OperationType getType() {
        return type;
    }
    
    public String getOperationId() {
        return operationId;
    }
    
    public abstract String serialize();
    
    @Override
    public String toString() {
        String s = "";
        s += "OperationId: " + operationId + "\n";
        s += "UserId: " + userId + "\n";
        s += "Type: " + type + "\n";
        return s;
    }
}
