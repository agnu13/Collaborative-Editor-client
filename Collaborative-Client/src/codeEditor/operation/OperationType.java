package codeEditor.operation;

public enum OperationType {
    INSERT("INSERT"),
    ERASE("ERASE"),
    REPOSITION("REPOSITION"),
    REGISTER("REGISTER"),
    UNREGISTER("UNREGISTER");
      
    private final String operation;
    
    private OperationType(String operation){
        this.operation = operation;
    }
    
    @Override
    public String toString(){
        return this.operation;
    }
}