package codeEditor.dataControl;

import codeEditor.operation.Operation;
import codeEditor.buffer.Buffer;
import config.Debug;
import static config.Debug.EXECUTOR_DEBUG;

public class Executor extends Thread{
    Editor editorCore;
    Buffer operationBuffer;
    
    public Executor(Editor editorCore, Buffer operationBuffer) {
        this.editorCore = editorCore;
        this.operationBuffer = operationBuffer; 
    }
    
    public void pushOperation(Operation operation) {
        try {
            operationBuffer.put(operation);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    @Override
    public void run() {
        Debug.log("Executor Started", EXECUTOR_DEBUG);
        
        while (!this.isInterrupted()) {
            try {
                Operation operation = (Operation) operationBuffer.take();
                editorCore.performOperation(operation);
            } catch (InterruptedException ex) {
                break;
            }
        }
        
        Debug.log("Executor Stopped", EXECUTOR_DEBUG);
    }
}
