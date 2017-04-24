package codeEditor.dataControl;

import codeEditor.operation.Operation;

public interface Editor {
    void performOperation(Operation operation); 
    void setReflectOperations(boolean flag); 
    
    //Debugging Information
    int getSize();
    char charAt(int positionToErase);
}
