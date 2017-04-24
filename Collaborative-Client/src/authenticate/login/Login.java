package authenticate.login;

import authenticate.entities.LoginInfo;
import authenticate.exception.IncorrectPasswordException;
import authenticate.entities.Status;
import authenticate.entities.User;
import codeEditor.networkLayer.SendPostRequest;
import com.google.gson.Gson;
import static config.NetworkConfig.GET_USERINFO;
import static config.NetworkConfig.LOGIN;
import static config.NetworkConfig.SERVER_ADDRESS;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import urlbuilder.URLBuilder;
import utility.Request;


public class Login {
    private static final int SUCCESS = 200;
    
    public static User doLogin(String username, String password) throws IncorrectPasswordException, ConnectivityFailureException{ 
        LoginInfo info = new LoginInfo(username, password);
        URLBuilder urlBuilder = new URLBuilder(); 
        String url = urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(LOGIN).toString();
        Request request = new Request(url, info.serialize());
        try {
            HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
            HttpEntity httpEntity = response.getEntity();
            try {
                InputStream inStream = httpEntity.getContent();
                String content = IOUtils.toString(inStream);
                Gson gson = new Gson();
                Status status = gson.fromJson(content, Status.class);

                if (status.statusCode == SUCCESS) {
                    return getUser(username, password);
                } else {
                    throw new IncorrectPasswordException(status.statusMessage);
                }
            } catch (IOException | UnsupportedOperationException ex) {
                throw new ConnectivityFailureException("Unable to connect.");
            }
        } catch(IOException e) {
            throw new ConnectivityFailureException("Unable to connect.");
        }
    }
    
    private static User getUser(String username, String password) throws ConnectivityFailureException { 
        LoginInfo info = new LoginInfo(username, password);
        
        URLBuilder urlBuilder = new URLBuilder(); 
        String url = urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(GET_USERINFO).toString();
        
        Request request = new Request(url, info.serialize());
        try {
            HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
            HttpEntity httpEntity = response.getEntity();
            try {
                InputStream inStream = httpEntity.getContent();
                String content = IOUtils.toString(inStream);

                Gson gson = new Gson();
                User user = gson.fromJson(content, User.class);
                
                return user;
            } catch (IOException | UnsupportedOperationException ex) {
                throw new ConnectivityFailureException("Unable to connect.");
            }
        } catch(IOException e) {
            throw new ConnectivityFailureException("Unable to connect.");
        }
    }
}
