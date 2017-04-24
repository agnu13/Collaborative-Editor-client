package codeEditor.sessionLayer;

import codeEditor.dataControl.Executor;
import codeEditor.networkLayer.Request;
import codeEditor.networkLayer.SendPostRequest;
import codeEditor.operation.RegisterUserOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.utility.RandomGen;
import static config.NetworkConfig.REGISTER;
import static config.NetworkConfig.SERVER_ADDRESS;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import urlbuilder.URLBuilder;

public class RegisterUser {
    private final String userId;
    private final String docId;
    private final Executor executor;
    
    public RegisterUser(String userId, String docId,  Executor executeOperationThread) {
        this.userId = userId;
        this.docId = docId;
        this.executor = executeOperationThread; 
    }
    
    public final void registerUserOnDoc() {
        URLBuilder urlBuilder = new URLBuilder(); 
        urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(REGISTER).toString();
        urlBuilder.addParameter("userId", userId).addParameter("docId", docId);
        String registerMessage = urlBuilder.toString();
        
        RegisterUserOperation registerOperation = new RegisterUserOperation(RandomGen.getRandom(), userId);
        Request registerRequest = new Request(registerMessage, registerOperation.serialize()); 
        boolean retry = true;
        do {
            try {
                HttpResponse response = SendPostRequest.sendPostRequest(registerRequest.getRequestUrl(), registerRequest.getSerializedRequest());
                HttpEntity httpEntity = response.getEntity();
                InputStream inStream = httpEntity.getContent();
                String content = IOUtils.toString(inStream);
                //
                content = content.replaceAll("\r\n", "\n");
                for (int i = 0; i < content.length(); i++) {
                    executor.pushOperation(new InsertOperation(RandomGen.getRandom(), "0", i, content.charAt(i)));
                }
                //
                retry = false;
            } catch (IOException | UnsupportedOperationException ex) {
                System.err.println("Unable to register due to connectivity failure");
            }
        } while (retry);
    }
}
