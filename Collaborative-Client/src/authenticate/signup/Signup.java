package authenticate.signup;

import authenticate.entities.Status;
import authenticate.entities.User;
import codeEditor.networkLayer.SendPostRequest;
import com.google.gson.Gson;
import static config.NetworkConfig.SERVER_ADDRESS;
import static config.NetworkConfig.SIGNUP;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import urlbuilder.URLBuilder;

public class Signup {
    private static final boolean DO_RETRY = true;
    
    public static Status doSignUp(User user) throws ConnectivityFailureException {
        URLBuilder urlBuilder = new URLBuilder(); 
        String url = urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(SIGNUP).toString();
        boolean retry = !DO_RETRY;
        do {
            utility.Request request = new utility.Request(url, user.serialize());
            try {
                HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
                retry = true;
                HttpEntity httpEntity = response.getEntity();
                try {
                    InputStream inStream = httpEntity.getContent();
                    String content = IOUtils.toString(inStream);
                    Gson gson = new Gson();
                    Status status = gson.fromJson(content, Status.class);
                    return status;
                } catch (IOException | UnsupportedOperationException ex) {
                    throw new ConnectivityFailureException("Unable to connect.");
                }
            } catch(IOException e) {
                throw new ConnectivityFailureException("Unable to connect.");
            }
        } while (!retry);
    }
}
