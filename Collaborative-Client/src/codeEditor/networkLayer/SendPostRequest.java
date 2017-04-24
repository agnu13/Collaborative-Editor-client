package codeEditor.networkLayer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class SendPostRequest {
    public static final HttpResponse sendPostRequest(String postUrl, String serializedObject) throws IOException {
        HttpPost post = new HttpPost(postUrl);
        try {
            StringEntity  postingString =new StringEntity(serializedObject);
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(post);
            return response;
        } catch (UnsupportedEncodingException e) {
            System.err.println("UnsupportedEncodingException.");
            e.printStackTrace(System.out);
        } catch (IOException e){
            throw e;
        }
        return null;
    }
}
