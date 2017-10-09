package de.eternalwings.awww;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("twitch")
public class TwitchSettings {

    private String appUsername;
    private String appOauthToken;

    private List<String> initialChannels;
    private List<String> trustedUsers;

    public TwitchSettings() {
    }

    public TwitchSettings(String appUsername, String appOauthToken, List<String> initialChannels, List<String> trustedUsers) {
        this.appUsername = appUsername;
        this.appOauthToken = appOauthToken;
        this.initialChannels = initialChannels;
        this.trustedUsers = trustedUsers;
    }

    public String getAppUsername() {
        return appUsername;
    }

    public void setAppUsername(String appUsername) {
        this.appUsername = appUsername;
    }

    public String getAppOauthToken() {
        return appOauthToken;
    }

    public void setAppOauthToken(String appOauthToken) {
        this.appOauthToken = appOauthToken;
    }

    public List<String> getInitialChannels() {
        return initialChannels;
    }

    public void setInitialChannels(List<String> initialChannels) {
        this.initialChannels = initialChannels;
    }

    public List<String> getTrustedUsers() {
        return trustedUsers;
    }

    public void setTrustedUsers(List<String> trustedUsers) {
        this.trustedUsers = trustedUsers;
    }
}
