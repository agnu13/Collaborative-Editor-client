package urlbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Pair<Tk, Tv> {
    Tk key;
    Tv value;
    
    Pair(Tk key, Tv value) {
        this.key = key;
        this.value = value;
    }
}

public class URLBuilder { 
    private String serverAddress;
    private String method;
    private HashMap<String, String> query = new HashMap<>();
    
    public URLBuilder setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        return this;
    }
    
    public URLBuilder setMethod(String method) {
        this.method = method;
        return this;
    }
    
    public URLBuilder addParameter(String parameter, String argument) {
        query.put(parameter, argument);
        return this;
    }
    
    @Override
    public String toString() {
        String url = "";
        url += serverAddress;
        url += "/";
        url += method;
        ArrayList<Pair<String,String> > list = new ArrayList<>();
        for (Map.Entry<String, String> entry: query.entrySet()) {
            list.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        
        if (list.size() > 0) { 
            url += "?";
            url += list.get(0).key + "=" +  list.get(0).value;  
            for (int i = 1; i < list.size(); i++) {
                url += "&";
                url += list.get(i).key + "=" +  list.get(i).value;  
            }
        }
        
        //System.err.println(url);
        return url;
    }
}
