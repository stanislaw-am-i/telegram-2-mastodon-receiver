package org.receiver.mastodon;

import org.receiver.messages.Message;
import org.receiver.messages.MessageRepository;
import org.receiver.reciver.Configuration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

public class MastodonService {

    private static final String STATUS_MEDIA_TYPE = "application/x-www-form-urlencoded";
    private MessageRepository statuses;
    private Configuration config;
    private String baseUrl;
    private String authSign;
    private long interval;

    public MastodonService(MessageRepository statuses, Configuration config) {
        this.statuses = statuses;
        this.config = config;

        this.baseUrl = this.config.getBaseUrl() + "/api/v1/";
        this.authSign = "access_token=" + this.config.getAccessToken();
        this.interval = Long.valueOf(this.config.getInterval());
    }

    public void postStatusesToMastodon() throws IOException, InterruptedException {
        Map<String, Message> statusesToPost = statuses.getMessages();
        Set<String> statusesLocalIds = statuses.getMessagesIds();

        MastodonAPI postStatus = new MastodonAPI();
        postStatus.setMediaType(STATUS_MEDIA_TYPE);
        
        for ( String statusId : statusesLocalIds ) {
            Message status = statusesToPost.get(statusId);

            String body = "";

            if ( !status.isMessageEmpty() ) {
                body = "status=" + status.getTextMessage();
            }

            postStatus.setBody(body);

            String url = baseUrl + "statuses?" + authSign;
            postStatus.setRequest(url, "POST", STATUS_MEDIA_TYPE);

            postStatus.makeCall();
            Response response = postStatus.getResponse();

            TimeUnit.MINUTES.sleep(1);
        }
    }
}
