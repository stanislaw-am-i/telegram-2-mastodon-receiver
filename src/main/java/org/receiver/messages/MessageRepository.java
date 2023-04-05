package org.receiver.messages;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class MessageRepository {

    private Set<String> messagesIds;
    private Map<String, Message> messages;
    private int countMessages;

    public MessageRepository(Elements parsedData) {
        messages = new HashMap<>();
        messagesIds = new HashSet<>();
        countMessages = 0;

        for (Element rawMessage : parsedData) {
            if ( rawMessage.classNames().contains("default") ) {
                Message message = new Message();

                String textMessage = rawMessage.getElementsByClass("text").text();
                if ( !textMessage.isBlank() ) {
                    message.setTextMessage(textMessage);
                }

                String stickerPath = rawMessage.getElementsByClass("media_photo").attr("href");
                if ( !stickerPath.isBlank() ) {
                    Media sticker = new Media();
                    sticker.setPathToFile(stickerPath);
                    sticker.setMessageId(message.getMessageId());

                    message.addMediaFileToRepository(sticker);
                }

                String photoPath = rawMessage.getElementsByClass("photo_wrap").attr("href");
                if ( !photoPath.isBlank() ) {
                    Media photo = new Media();
                    photo.setPathToFile(photoPath);
                    photo.setMessageId(message.getMessageId());

                    message.addMediaFileToRepository(photo);
                }

                /*String gifPath = rawMessage.getElementsByClass("animated_wrap").attr("href");
                if ( !gifPath.isBlank() ) {
                    Media gif = new Media();
                    gif.setPathToFile(gifPath);
                    gif.setMessageId(message.getId());

                    message.addMediaFileToRepository(gif);
                }*/

                String videoPath = rawMessage.getElementsByClass("video_file_wrap").attr("href");
                if ( !videoPath.isBlank() ) {
                    Media video = new Media();
                    video.setPathToFile(videoPath);
                    video.setMessageId(message.getMessageId());

                    message.addMediaFileToRepository(video);
                }

                Boolean isMessageEmpty = message.isMessageEmpty();
                if ( !isMessageEmpty ) {
                    addMessage(message);
                    System.out.println( message.getMessageId() );
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

    public void addMessage(Message message) {
        messages.put(message.getMessageId(), message);
        messagesIds.add(message.getMessageId());
        countMessages++;
    }

    public int getCountMessages() {
        return countMessages;
    }
}
