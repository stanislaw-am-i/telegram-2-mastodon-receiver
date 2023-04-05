package org.receiver.messages;

import java.util.Objects;
import java.util.UUID;

public class Media {

    private static final String MEDIA_ID = UUID.randomUUID().toString();
    private String messageId;
    private String externalId;
    private String pathToFile;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Media media = (Media) o;

        if (!Objects.equals(externalId, media.externalId)) return false;
        return Objects.equals(pathToFile, media.pathToFile);
    }

    @Override
    public int hashCode() {
        int result = externalId != null ? externalId.hashCode() : 0;
        result = 31 * result + (pathToFile != null ? pathToFile.hashCode() : 0);
        return result;
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
