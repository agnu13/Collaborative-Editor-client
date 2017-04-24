package share;

import authenticate.entities.User;
import codeEditor.networkLayer.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import static config.NetworkConfig.GET_USERS;
import static config.NetworkConfig.SERVER_ADDRESS;
import static config.NetworkConfig.SHARE;
import static config.NetworkConfig.SHARE_VIEW;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import projectManager.Collections;
import projectManager.Node;
import urlbuilder.URLBuilder;
import utility.SendPostRequest;

public class Share {
    public static ArrayList<User> getUsers() throws ConnectivityFailureException { 
        
        URLBuilder urlBuilder = new URLBuilder(); 
        urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(GET_USERS);
        String getUsersUrl = urlBuilder.toString();
        
        Request request = new Request(getUsersUrl, "");
        try {
            HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
            HttpEntity httpEntity = response.getEntity();
            try {
                InputStream inStream = httpEntity.getContent();
                String content = IOUtils.toString(inStream);
                
                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<ArrayList<User>>() {}.getType();
                ArrayList<User> userList = gson.fromJson(content, listType);
                
                return userList;
            } catch (UnsupportedOperationException ex) {
                ex.printStackTrace(System.err);
            }
        } catch(IOException ex) {
            throw new ConnectivityFailureException("Unable to connect.");
        }
        return null;
    }
    
    public static void shareWith(String userId, String projectName, ArrayList<String> shareIds) throws ConnectivityFailureException {
        
        URLBuilder urlBuilder = new URLBuilder(); 
        urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(SHARE).toString();
        urlBuilder.addParameter("userId", userId);
        urlBuilder.addParameter("docId", projectName);
        urlBuilder.addParameter("shareId", shareIds.get(0));
        
        String shareUrl = urlBuilder.toString();
        Request request = new Request(shareUrl, "");
        try {
            SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
        } catch(IOException ex) {
            throw new ConnectivityFailureException("Unable to connect.");
        }
    }

    public static ArrayList<Collections> getSharedProjects(String userId) throws ConnectivityFailureException { 
        try {
            ArrayList<Collections> node = new ArrayList<>();
            
            URLBuilder urlBuilder = new URLBuilder(); 
            urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(SHARE_VIEW).toString();
            urlBuilder.addParameter("userId", userId);
            
            String projectViewURL = urlBuilder.toString();
            utility.Request request = new utility.Request(projectViewURL, "");
            HttpResponse response = codeEditor.networkLayer.SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());
            HttpEntity httpEntity = response.getEntity();
            try {
                InputStream inStream = httpEntity.getContent();
                String serializedContent = IOUtils.toString(inStream);
                if (serializedContent.equals("")) {
                } else {
                    //System.out.println(serializedContent);

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Node.class, new projectManager.Deserializer());
                    Gson gson = gsonBuilder.create();

                    java.lang.reflect.Type listType = new TypeToken<ArrayList<Collections>>() {}.getType();
                    ArrayList<Collections> list = gson.fromJson(serializedContent, listType);
                    return list;
                 }
            } catch (UnsupportedOperationException ex) {
                ex.printStackTrace(System.err);
            } catch (IOException e) {
                throw new ConnectivityFailureException("Unable to Connect");
            }
        } catch (IOException e) {
            throw new ConnectivityFailureException("Unable to Connect");
        }
        return null;
    }
}
