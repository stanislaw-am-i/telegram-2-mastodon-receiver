package org.receiver.messages;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageRepository {

    private Set<String> messagesIds;
    private Map<String, Message> messages;
    private List<Media> mediaStorage;
    private int countMessages;

    public MessageRepository(Elements parsedData) {
        messages = new HashMap<>();
        messagesIds = new LinkedHashSet<>();
        mediaStorage = new ArrayList<>();
        countMessages = 0;

        for (Element rawMessage : parsedData) {
            if ( rawMessage.classNames().contains("default") ) {
                processMessages(rawMessage);
            }
        }
    }

    /*private void processMessages(Element rawMessage) {
        Message message = new Message();
        message.setMessageId( rawMessage.attr("id") );

        Element reply = rawMessage.getElementsByClass("reply_to").first();
        String replyTo = getParentMessageId(reply);
        if ( replyTo!= null && !replyTo.isBlank() ) {
            message.setParentMessageId(replyTo);
        }

        String photoPath = rawMessage.getElementsByClass("photo_wrap").attr("href");
        if ( !photoPath.isBlank() ) {
            Media photo = new Media("src/main/resources/ChatExport_2023-04-09/" + photoPath, message.getMessageId());
            message.addMediaFileToRepository(photo);
        }

        String videoPath = rawMessage.getElementsByClass("video_file_wrap").attr("href");
        if ( !videoPath.isBlank() ) {
            Media video = new Media("src/main/resources/ChatExport_2023-04-09/" + videoPath, message.getMessageId());
            message.addMediaFileToRepository(video);
        }

        String voicePath = rawMessage.getElementsByClass("media_voice_message").attr("href");
        if ( !voicePath.isBlank() ) {
            Media voice = new Media("src/main/resources/ChatExport_2023-04-09/" + voicePath, message.getMessageId());
            message.addMediaFileToRepository(voice);
        }

        String gifPath = rawMessage.getElementsByClass("animated_wrap").attr("href");
        if ( !gifPath.isBlank() ) {
            Media gif = new Media("src/main/resources/ChatExport_2023-04-09/" + gifPath, message.getMessageId());
            message.addMediaFileToRepository(gif);
        }

        String audioPath = rawMessage.getElementsByClass("media_audio_file").attr("href");
        if ( !audioPath.isBlank() ) {
            Media audio = new Media("src/main/resources/ChatExport_2023-04-09/" + audioPath, message.getMessageId());
            message.addMediaFileToRepository(audio);
        }

        String filePath = rawMessage.getElementsByClass("media_file").attr("href");
        if ( !filePath.isBlank() ) {
            Media file = new Media("src/main/resources/ChatExport_2023-04-09/" + filePath, message.getMessageId());
            message.addMediaFileToRepository(file);
        }

        String textMessage = rawMessage.getElementsByClass("text").text();
        if (textMessage.length() > 500) {
            splitLongMessage(message, textMessage);
        } else {
            message.setTextMessage(textMessage);
            addMessage(message);
        }
    }*/

    private void processMessages(Element rawMessage) {
        Message message = new Message();
        message.setMessageId(rawMessage.attr("id"));

        processMedia(rawMessage, message);
        processText(rawMessage, message);

        addMessage(message);
    }

    private void processMedia(Element rawMessage, Message message) {
        addMediaIfPresent(rawMessage, message, "photo_wrap");
        addMediaIfPresent(rawMessage, message, "video_file_wrap");
        addMediaIfPresent(rawMessage, message, "media_voice_message");
        addMediaIfPresent(rawMessage, message, "animated_wrap");
        addMediaIfPresent(rawMessage, message, "media_audio_file");
        addMediaIfPresent(rawMessage, message, "media_file");
    }

    private void addMediaIfPresent(Element rawMessage, Message message, String className) {
        String path = rawMessage.getElementsByClass(className).attr("href");
        if (!path.isBlank()) {
            Media media = new Media("src/main/resources/ChatExport_2023-04-09/" + path, message.getMessageId());
            message.addMediaFileToRepository(media);
        }
    }

    private void processText(Element rawMessage, Message message) {
        Element forwardedBody = rawMessage.getElementsByClass("forwarded body").first();
        String forwardedTitle = getForwardedTitle(forwardedBody);
        String textMessage = rawMessage.getElementsByClass("text").text();

        if ( forwardedTitle != null && !forwardedTitle.isBlank() ) {
            textMessage = forwardedTitle + "\n" + textMessage;
        }

        if (textMessage.length() > 500) {
            splitLongMessage(message, textMessage);
        } else {
            message.setTextMessage(textMessage);
        }

        String replyTo = getParentMessageId(rawMessage.getElementsByClass("reply_to").first());
        if (replyTo != null && !replyTo.isBlank()) {
            message.setParentMessageId(replyTo);
        }
    }

    private void splitLongMessage(Message mainMessage, String messageText) {
        String[] parts = messageText.split("(?<=[.!?]\\s)");
        List<String> chunks = new ArrayList<>();
        int maxStatusLength = 489;

        StringBuilder currentChunk = new StringBuilder();
        for (String sentence : parts) {
            if (currentChunk.length() + sentence.length() > maxStatusLength) {
                chunks.add(currentChunk.toString());
                currentChunk = new StringBuilder(sentence);
            } else {
                currentChunk.append(" ").append(sentence);
            }
        }
        chunks.add(currentChunk.toString());

        int totalCount = chunks.size();
        String parentMessageId = mainMessage.getMessageId();
        for (int i = 0; i < totalCount; i++) {
            String chunk = chunks.get(i);
            int chunkNumber = i + 1;
            String formattedChunk = "(" + chunkNumber + "/" + totalCount + ") " + chunk;

            if ( i != 0 ) {
                Message splittedMessage = new Message();
                splittedMessage.setTextMessage(formattedChunk);
                splittedMessage.setMessageId(parentMessageId + "_" + chunkNumber);
                splittedMessage.setParentMessageId(parentMessageId);

                addMessage(splittedMessage);
            } else {
                mainMessage.setTextMessage(formattedChunk);
                addMessage(mainMessage);
            }
        }
    }

    private String getParentMessageId(Element reply) {
        if (reply == null) {
            return null;
        }

        String replyHref = reply.getElementsByTag("a").first().attr("href");
        Pattern pattern = Pattern.compile("(message.*)");
        Matcher matcher = pattern.matcher(replyHref);

        return matcher.find() ? matcher.group(1) : null;
    }

    private String getForwardedTitle(Element forwardedBody) {
        if (forwardedBody == null) {
            return null;
        }

        return forwardedBody.getElementsByClass("from_name").text();
    }

    public Map<String, Message> getMessages() {
        return messages;
    }

    public Set<String> getMessagesIds() {
        return messagesIds;
    }

    public List<Media> getMediaStorage() {
        return mediaStorage;
    }

    public void addMessage(Message message) {
        messages.put(message.getMessageId(), message);
        messagesIds.add(message.getMessageId());
        mediaStorage.addAll(message.getMediaRepository());
        System.out.println( message.getMessageId() );
        countMessages++;
    }

    public int getCountMessages() {
        return countMessages;
    }
}
