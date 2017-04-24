package projectManager;

public class Doc extends Node {
    private final String identifier;
   
    public Doc(String name, String path, String identifier) {
        super(name, path, Type.DOC);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
