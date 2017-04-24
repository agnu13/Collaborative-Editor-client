package codeEditor.operation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.RepositionOperation;
import java.lang.reflect.Type;

class OperationNotFound extends Exception{
        OperationNotFound(String errorMessage) {   
            super(errorMessage);
        }
}

public class Deserializer implements JsonDeserializer<Operation>{
    @Override
    public Operation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            JsonObject jsonObject = (JsonObject) json;
            String type = jsonObject.get("type").getAsString(); 
            Operation operation;
            switch(type) {
                case "INSERT": {
                    operation = new InsertOperation(json.toString());
                } break;
                case "ERASE": {
                    operation = new EraseOperation(json.toString());
                } break;
                case "REPOSITION": {
                    operation = new RepositionOperation(json.toString());
                } break;
                default: throw new OperationNotFound("No such Operation exists");
            }
            return operation;
        } catch (OperationNotFound e) {
            e.printStackTrace(System.err);
        }
        return null;
    }
}
