package codeEditor.operation;

import com.google.gson.Gson;

public class UnregisterUserOperation extends Operation {
    public UnregisterUserOperation(String operationId, String userIdentifier) {
        super(operationId, userIdentifier, OperationType.UNREGISTER);
    }
    
    @Override
    public String serialize() {
        return new Gson().toJson(this);
    }
}