package projectManager;

import codeEditor.networkLayer.SendPostRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import config.Debug;
import static config.Debug.PROJECT_MANAGER_DEBUG;
import static config.NetworkConfig.PROJECT_ADD_NODE;
import static config.NetworkConfig.PROJECT_VIEW;
import static config.NetworkConfig.SERVER_ADDRESS;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import urlbuilder.URLBuilder;
import utility.Request;

public class Collections extends Node {
    public Collections(String name, String path) {
        super(name, path, Type.COLLECTION);
    }
    
    public Node createNode(String name, Type type) throws ConnectivityFailureException, UnableToCreateException {
        //System.out.println(name);
        String projectPath = getPath() + "." + getName();
        Node node = new Node(name, projectPath, type); 
        return addNode(node);
    }
    
    private Node addNode(Node node) throws ConnectivityFailureException, UnableToCreateException {
       
        URLBuilder urlBuilder = new URLBuilder(); 
        urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(PROJECT_ADD_NODE).toString();
        urlBuilder.addParameter("path", getPath() + "." + getName());
        String nodeAddUrl = urlBuilder.toString();
            
        Request request = new Request(nodeAddUrl, node.toJson());
        try {
            HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
            HttpEntity httpEntity = response.getEntity();
            try {
                InputStream inStream = httpEntity.getContent();
                String serializedContent = IOUtils.toString(inStream);
                if (serializedContent.equals("")) {
                    throw new UnableToCreateException("Unable To Create");
                } else {
                    if (node.getType() == Type.COLLECTION) {
                        Debug.log(serializedContent, PROJECT_MANAGER_DEBUG);
                        Collections newCollection = new Gson().fromJson(serializedContent, Collections.class);
                        return newCollection;
                    } else {
                        Doc newDoc = new Gson().fromJson(serializedContent, Doc.class);
                        return newDoc;
                    }
                }
            } catch (IOException | UnsupportedOperationException ex) {
                ex.printStackTrace(System.err);
            }
        } catch(IOException e) {
            throw new ConnectivityFailureException("Unable to Connect");
        }
        return null;
    }
    
    public ArrayList getContent() throws ConnectivityFailureException {
        try {
            ArrayList<Collections> node = new ArrayList<>();
            
            URLBuilder urlBuilder = new URLBuilder(); 
            urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(PROJECT_VIEW).toString();
            urlBuilder.addParameter("path", getPath() + "." + getName());
            String nodeViewUrl = urlBuilder.toString();
            
            Request request = new Request(nodeViewUrl, "");
            HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());
            HttpEntity httpEntity = response.getEntity();
            try {
                InputStream inStream = httpEntity.getContent();
                String serializedContent = IOUtils.toString(inStream);
                if (serializedContent.equals("")) {
                } else {
                    Debug.log(serializedContent, PROJECT_MANAGER_DEBUG);

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Node.class, new projectManager.Deserializer());
                    Gson gson = gsonBuilder.create();

                    java.lang.reflect.Type listType = new TypeToken<ArrayList<Node>>() {}.getType();
                    ArrayList<Node> list = gson.fromJson(serializedContent, listType);
                    return list;
                 }
            } catch (IOException | UnsupportedOperationException ex) {
                ex.printStackTrace(System.err);
            } 
        } catch (IOException e) {
            throw new ConnectivityFailureException("Unable to Connect");
        }
        return null;
    }
    
    
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (getClass() != object.getClass()) {
            return false;
        } else {
            final Collections other = (Collections) object;
            return (this.getPath() + this.getName()).equals(other.getPath() + other.getName());
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }
}
