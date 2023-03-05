package lambda.login;

public class Token {

    private String grantType;
    private String accessToken;

    public Token(String accessToken) {
        this.grantType = "Bearer ";//error가 날수있음 확인해야한다.
        this.accessToken = accessToken;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
