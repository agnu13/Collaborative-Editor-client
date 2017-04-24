package authenticate.entities;

import com.google.gson.Gson;

public class LoginInfo {
    private String username;
    private String password;
    
    public LoginInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public LoginInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginInfo setPassword(String password) {
        this.password = password;
        return this;
    }
    
    public String serialize(){
        String json = new Gson().toJson(this); 
        return json;
    }
}
