package de.eternalwings.awww;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("twitch")
public class TwitchSettings {

    private String appUsername;
    private String appOauthToken;
    private Integer messageLimit;

    private List<String> initialChannels;
    private List<String> trustedUsers;
    private List<String> admins;

    public TwitchSettings() {
    }

    public TwitchSettings(String appUsername, String appOauthToken, Integer messageLimit, List<String> initialChannels, List<String> trustedUsers, List<String> admins) {
        this.appUsername = appUsername;
        this.appOauthToken = appOauthToken;
        this.messageLimit = messageLimit;
        this.initialChannels = initialChannels;
        this.trustedUsers = trustedUsers;
        this.admins = admins;
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

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public Integer getMessageLimit() {
        return messageLimit;
    }

    public void setMessageLimit(Integer messageLimit) {
        this.messageLimit = messageLimit;
    }
}
