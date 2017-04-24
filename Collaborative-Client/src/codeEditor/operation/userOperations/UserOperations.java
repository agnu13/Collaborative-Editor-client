package codeEditor.operation.userOperations;

import codeEditor.operation.OperationType;

public interface UserOperations {
    OperationType getType();
    String serialize();
    void deserialize(String json);
}
