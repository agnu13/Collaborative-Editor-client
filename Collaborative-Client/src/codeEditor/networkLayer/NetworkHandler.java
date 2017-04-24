package codeEditor.networkLayer;

import org.apache.http.HttpResponse;

public interface NetworkHandler {
    void handleRequest(Request request);
    void handleResponse(HttpResponse response);
    
    void start();
    void interrupt();
}
