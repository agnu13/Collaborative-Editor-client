package utility;

public class Request {
    private String requestUrl;
    private String serializedRequest;

    public Request(String requestUrl, String serializedRequest) {
        this.requestUrl = requestUrl;
        this.serializedRequest = serializedRequest;
    }
    
    public String getRequestUrl() {
        return requestUrl;
    }

    public Request setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    public String getSerializedRequest() {
        return serializedRequest;
    }

    public Request setSerializedRequest(String serializedRequest) {
        this.serializedRequest = serializedRequest;
        return this;
    }
}