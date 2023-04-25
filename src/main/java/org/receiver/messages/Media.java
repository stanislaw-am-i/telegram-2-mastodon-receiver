package org.receiver.messages;

import java.util.UUID;

public class Media {

    private static final String MEDIA_ID = UUID.randomUUID().toString();
    private String messageId;
    private String externalId;
    private String pathToFile;
    private boolean isUploaded;

    public Media(String pathToFile, String messageId) {
        this.pathToFile = pathToFile;
        this.messageId = messageId;
    }

    public String getId() {
        return MEDIA_ID;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public boolean getUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    @Override
    public String toString() {
        return "Media{" +
                "messageId='" + messageId + '\'' +
                ", externalId='" + externalId + '\'' +
                ", pathToFile='" + pathToFile + '\'' +
                '}';
    }
}
