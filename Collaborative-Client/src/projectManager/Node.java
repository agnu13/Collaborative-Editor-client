package projectManager;

import com.google.gson.Gson;

public class Node {
    private final String name;
    private final String path;    
    private final Type type;
    
   public Node(String name, String path, Type type) {
        this.name = name;
        this.type = type;
        this.path = path;
    }
    
    //Converts the node into JSON Format
    public String toJson() {
        String json = new Gson().toJson(this); 
        return json;
    }
    
    //Getters and Setters
    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }
    public Type getType() {
        return type;
    }
}
