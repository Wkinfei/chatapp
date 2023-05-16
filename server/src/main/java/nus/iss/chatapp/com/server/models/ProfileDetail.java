package nus.iss.chatapp.com.server.models;

public class ProfileDetail {
    private Integer userId;
    private String loginUsername;
    private String loginPassword;
    private String displayName;
    private String email;
    private String imageUrl;
    
    public Integer getuserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getLoginUsername() {
        return loginUsername;
    }
    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }
    public String getLoginPassword() {
        return loginPassword;
    }
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @Override
    public String toString() {
        return "ProfileDetail [userId=" + userId + ", loginUsername=" + loginUsername + ", loginPassword="
                + loginPassword + ", displayName=" + displayName + ", email=" + email + ", imageUrl=" + imageUrl + "]";
    }
  

    

}
