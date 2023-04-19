package org.receiver.mastodon;

import okhttp3.Response;
import org.json.JSONObject;
import org.receiver.messages.Media;
import org.receiver.messages.Message;
import org.receiver.messages.MessageRepository;
import org.receiver.reciver.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        List<Media> mediaStorage = statuses.getMediaStorage();

        for ( Media media : mediaStorage ) {
            MastodonAPI uploadMedia = new MastodonAPI();
            uploadMedia.setBody(media.getId(), media.getPathToFile());

            String url = baseUrl + "media?" + authSign;
            uploadMedia.setRequest(url, "POST");
            uploadMedia.makeCall();
            Response response = uploadMedia.getResponse();

            if ( response.code() == 200 ) {
                String stringifyResponse = response.body().string();
                System.out.println(stringifyResponse);
                JSONObject prettyResponse = new JSONObject(stringifyResponse);

                media.setExternalId( prettyResponse.get("id").toString() );
            } else {
                String stringifyResponse = response.body().string();
                System.out.println(stringifyResponse);
                //System.out.println("Failed to Upload Media");
            }

            //TimeUnit.MINUTES.sleep(1);
        }

        for ( String statusId : statusesLocalIds ) {

            Message status = statusesToPost.get(statusId);
            System.out.println(status);
            //System.out.println(status.getTextMessage());
            String body = "status=";

            if ( !status.isMessageEmpty() ) {
                body += status.getTextMessage();
            }

            if ( !status.getMediaRepository().isEmpty() ) {
                String mediaExternalId = status.getMediaRepository().get(0).getExternalId();
                body += "&media_ids[]=" + mediaExternalId;
            }

            if ( status.getParentMessageId() != null ) {
                Message parentMessage = statusesToPost.get(status.getParentMessageId());

                if ( parentMessage.getExternalId() != null ) {
                    body += "&in_reply_to_id=" + parentMessage.getExternalId();
                }
            }

            MastodonAPI postStatus = new MastodonAPI();
            postStatus.setMediaType(STATUS_MEDIA_TYPE);
            postStatus.setBody(body);
            //System.out.println(body);
            String url = baseUrl + "statuses?" + authSign;
            postStatus.setRequest(url, "POST", STATUS_MEDIA_TYPE);
            postStatus.makeCall();
            Response response = postStatus.getResponse();

            if ( response.code() == 200 ) {
                String stringifyResponse = response.body().string();
                System.out.println(stringifyResponse);
                JSONObject prettyResponse = new JSONObject(stringifyResponse);

                status.setExternalId( prettyResponse.get("id").toString() );
            } else {
                System.out.println("Failed to Post Status");
            }

            //TimeUnit.MINUTES.sleep(1);
        }

    }
}
