package org.receiver.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Message {

    private String messageId;
    private String externalId;
    private UUID parentMessageId;
    private String parentMessageExternalId;
    private String textMessage;
    private List<Media> mediaRepository;

    public Message() {
        mediaRepository = new ArrayList<>();
        messageId = UUID.randomUUID().toString();
    }

    public String getMessageId() {
        return messageId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public UUID getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(UUID parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public String getParentMessageExternalId() {
        return parentMessageExternalId;
    }

    public void setParentMessageExternalId(String parentMessageExternalId) {
        this.parentMessageExternalId = parentMessageExternalId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public List<Media> getMediaRepository() {
        return mediaRepository;
    }

    public void setMediaRepository(List<Media> mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public void addMediaFileToRepository(Media media) {
        this.mediaRepository.add(media);
    }

    public boolean isMessageEmpty() {
        boolean isMessageEmpty = ( textMessage == null || textMessage.isBlank() ) && mediaRepository.isEmpty();
        return isMessageEmpty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!Objects.equals(externalId, message.externalId)) return false;
        if (!Objects.equals(parentMessageId, message.parentMessageId))
            return false;
        if (!Objects.equals(parentMessageExternalId, message.parentMessageExternalId))
            return false;
        if (!Objects.equals(textMessage, message.textMessage)) return false;
        return Objects.equals(mediaRepository, message.mediaRepository);
    }

    @Override
    public int hashCode() {
        int result = externalId != null ? externalId.hashCode() : 0;
        result = 31 * result + (parentMessageId != null ? parentMessageId.hashCode() : 0);
        result = 31 * result + (parentMessageExternalId != null ? parentMessageExternalId.hashCode() : 0);
        result = 31 * result + (textMessage != null ? textMessage.hashCode() : 0);
        result = 31 * result + (mediaRepository != null ? mediaRepository.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "externalId='" + externalId + '\'' +
                ", parentMessageId=" + parentMessageId +
                ", parentMessageExternalId='" + parentMessageExternalId + '\'' +
                ", textMessage='" + textMessage + '\'' +
                ", mediaRepository=" + mediaRepository +
                '}';
    }
}
