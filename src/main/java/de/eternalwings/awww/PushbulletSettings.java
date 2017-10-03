package de.eternalwings.awww;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("pushbullet")
public class PushbulletSettings {

    private String key;
    private String noteTitle = "Stream is live!";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }
}
