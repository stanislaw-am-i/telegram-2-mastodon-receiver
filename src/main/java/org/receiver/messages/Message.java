package org.receiver.messages;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private String messageId;
    private String externalId;
    private String parentMessageId;
    private String parentMessageExternalId;
    private String textMessage;
    private boolean isHaveText;
    private boolean isForwarded;
    private List<Media> mediaRepository;

    public Message() {
        mediaRepository = new ArrayList<>();
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

    public String getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(String parentMessageId) {
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

    public boolean getHaveText() {
        return isHaveText;
    }

    public void setHaveText(boolean haveText) {
        isHaveText = haveText;
    }

    public boolean getForwarded() {
        return isForwarded;
    }

    public void setForwarded(boolean forwarded) {
        isForwarded = forwarded;
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
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", externalId=" + externalId +
                ", parentMessageId=" + parentMessageId +
                ", parentMessageExternalId='" + parentMessageExternalId + '\'' +
                ", textMessage='" + textMessage + '\'' +
                ", mediaRepository=" + mediaRepository +
                '}';
    }
}
