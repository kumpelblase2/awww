package de.eternalwings.awww;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("imgur")
public class ImgurSettings {

    private String clientId;
    private String clientSecret;

    private String cacheLocation;

    public ImgurSettings() {
    }

    public ImgurSettings(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getCacheLocation() {
        return cacheLocation;
    }

    public void setCacheLocation(String cacheLocation) {
        this.cacheLocation = cacheLocation;
    }
}
