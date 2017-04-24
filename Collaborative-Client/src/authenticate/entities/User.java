package authenticate.entities;

import com.google.gson.Gson;

public class User {
    private String name;
    private String userId;
    private String username;
    private String password;
    private String emailId;
    
    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmailId() {
        return emailId;
    }

    public User setEmailId(String emailId) {
        this.emailId = emailId;
        return this;
    }

    public String getUserIdentifier() {
        return userId;
    }

    public User setUserIdentifier(String userIdentifier) {
        this.userId = userIdentifier;
        return this;
    }
    
    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }
    
    public String serialize(){
        String json = new Gson().toJson(this); 
        return json;
    }
}
