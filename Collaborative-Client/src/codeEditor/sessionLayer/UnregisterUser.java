package codeEditor.sessionLayer;

import codeEditor.dataControl.Executor;
import codeEditor.networkLayer.Request;
import codeEditor.operation.UnregisterUserOperation;
import codeEditor.utility.RandomGen;
import static config.NetworkConfig.SERVER_ADDRESS;
import static config.NetworkConfig.UNREGISTER;
import java.io.IOException;
import urlbuilder.URLBuilder;

public class UnregisterUser {
    private final String userId;
    private final String docId;
    private final Executor executor;
    
    public UnregisterUser(String userId, String docId,  Executor executor) {
        this.userId = userId;
        this.docId = docId;
        this.executor = executor; 
    }
    
    public final void unregisterUser() {
        URLBuilder urlBuilder = new URLBuilder(); 
        urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(UNREGISTER).toString();
        urlBuilder.addParameter("userId", userId).addParameter("docId", docId);
        String unregisterUrl = urlBuilder.toString();
        
        UnregisterUserOperation unregisterOperation = new UnregisterUserOperation(RandomGen.getRandom(), userId);
        Request request = new Request(unregisterUrl, unregisterOperation.serialize());
        try {
            utility.SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
        } catch(IOException ex) {
            System.err.println("Unable to unregister due to connectivity failure.");
        }
    }
}
