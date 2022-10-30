package auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security.jwt.token")
public class AuthProperties {

    private String secretKey;
    private long expireLength;

    public String getSecretKey() {
        return secretKey;
    }

    public long getExpireLength() {
        return expireLength;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpireLength(long expireLength) {
        this.expireLength = expireLength;
    }
}
