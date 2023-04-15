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
                String textMessage = rawMessage.getElementsByClass("text").text();
                Boolean isMessageTooLong = !textMessage.isBlank() && textMessage.length() > 500;

                if ( !isMessageTooLong ) {
                    Message message = new Message();
                    message.setMessageId( rawMessage.attr("id") );

                    String photoPath = rawMessage.getElementsByClass("photo_wrap").attr("href");
                    if ( !photoPath.isBlank() ) {
                        Media photo = new Media();
                        photo.setPathToFile( "src/main/resources/ChatExport_2023-04-09/" + photoPath );
                        photo.setMessageId(message.getMessageId());

                        message.addMediaFileToRepository(photo);
                    }

                    Element reply = rawMessage.getElementsByClass("reply_to").first();
                    if ( reply != null ) {
                        String replyHref = reply.getElementsByTag("a").first().attr("href");

                        Pattern pattern = Pattern.compile("(message.*)");
                        Matcher matcher = pattern.matcher(replyHref);
                        if ( matcher.find() ) {
                            message.setParentMessageId( matcher.group(1) );
                        }
                    }

                    if ( !textMessage.isBlank() ) {
                        message.setTextMessage(textMessage);
                    }

                    Boolean isMessageEmpty = message.isMessageEmpty();
                    if ( !isMessageEmpty ) {
                        addMessage(message);
                    }
                } else {
                    splitLongMessage(rawMessage, textMessage);
                }
            }
        }
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

    public void splitLongMessage(Element rawMessage, String messageText) {
        Message mainMessage = new Message();
        mainMessage.setMessageId( rawMessage.attr("id") );

        String photoPath = rawMessage.getElementsByClass("photo_wrap").attr("href");
        if ( !photoPath.isBlank() ) {
            Media photo = new Media();
            photo.setPathToFile( "src/main/resources/ChatExport_2023-04-09/" + photoPath );
            photo.setMessageId(mainMessage.getMessageId());

            mainMessage.addMediaFileToRepository(photo);
        }

        Element reply = rawMessage.getElementsByClass("reply_to").first();
        if ( reply != null ) {
            String replyHref = reply.getElementsByTag("a").first().attr("href");

            Pattern pattern = Pattern.compile("(message.*)");
            Matcher matcher = pattern.matcher(replyHref);
            if ( matcher.find() ) {
                mainMessage.setParentMessageId( matcher.group(1) );
            }
        }

        String[] parts = messageText.split("(?<=[.!?]\\s)");
        List<String> chunks = new ArrayList<>();
        int maxStatusLength = 489;

        String currentChunk = "";
        for (String sentence : parts) {
            if (currentChunk.length() + sentence.length() > maxStatusLength) {
                chunks.add(currentChunk);
                currentChunk = sentence;
            } else {
                currentChunk += " " + sentence;
            }
        }

        chunks.add(currentChunk);

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

    public int getCountMessages() {
        return countMessages;
    }
}
