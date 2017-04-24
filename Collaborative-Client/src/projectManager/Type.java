package projectManager;

public enum Type {
    DOC("DOC"),
    COLLECTION("COLLECTION");

    private final String type;
    
    private Type(String type) {
        this.type = type;
    }
    
    @Override
    public String toString(){
        return this.type;
    }
}