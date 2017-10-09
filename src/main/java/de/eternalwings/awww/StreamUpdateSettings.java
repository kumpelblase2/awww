package de.eternalwings.awww;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("stream-updates")
public class StreamUpdateSettings {

    private String twitchChannel;
    private String pushbulletChannel;

    public String getTwitchChannel() {
        return twitchChannel;
    }

    public void setTwitchChannel(String twitchChannel) {
        this.twitchChannel = twitchChannel;
    }

    public String getPushbulletChannel() {
        return pushbulletChannel;
    }

    public void setPushbulletChannel(String pushbulletChannel) {
        this.pushbulletChannel = pushbulletChannel;
    }
}
