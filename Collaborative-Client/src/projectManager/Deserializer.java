package projectManager;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;

class FileTypeNotFound extends Exception{
        FileTypeNotFound(String errorMessage) {   
            super(errorMessage);
        }
}

public class Deserializer implements JsonDeserializer<Node>{
    @Override
    public Node deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            JsonObject jsonObject = (JsonObject) json;
            
            String type = jsonObject.get("type").getAsString();
            String name = jsonObject.get("name").getAsString();
            String path = jsonObject.get("path").getAsString();
            
            Node node;
            switch(type) {
                case "COLLECTION": {
                    node = new Collections(name, path);
                } break;
                case "DOC": {
                    String identifier = jsonObject.get("identifier").getAsString();
                    node = new Doc(name, path, identifier);
                } break;
                default: throw new FileTypeNotFound("No such FileType exists");
            }
            return node;
        } catch (FileTypeNotFound e) {
            e.printStackTrace(System.err);
        }
        return null;
    }
}
