package org.receiver.mastodon;

import okhttp3.Response;
import org.json.JSONObject;
import org.receiver.messages.Media;
import org.receiver.messages.Message;
import org.receiver.messages.MessageRepository;
import org.receiver.receiver.ReceiverException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MastodonService {
    private static final String STATUS_MEDIA_TYPE = "application/x-www-form-urlencoded";
    private MessageRepository statuses;
    private String baseUrl;
    private String authSign;
    private int interval;

    public MastodonService(MessageRepository statuses, String baseUrl, String authSign, int interval) {
        this.statuses = statuses;
        this.baseUrl = baseUrl + "/api/v1/";
        this.authSign = "access_token=" + authSign;
        this.interval = interval;
    }

    public void postStatusesToMastodon() {
        try {
            processUploadMedia(statuses.getMediaStorage());
            processUploadStatuses(statuses.getMessages(), statuses.getMessagesIds());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void processUploadMedia(List<Media> mediaStorage) throws InterruptedException, IOException {
        for ( Media media : mediaStorage ) {
            MastodonAPI uploadMedia = new MastodonAPI();
            uploadMedia.setBody(media.getId(), media.getPathToFile());

            String url = baseUrl + "media?" + authSign;
            uploadMedia.setRequest(url, "POST");
            uploadMedia.makeCall();
            Response response = uploadMedia.getResponse();

            String stringifyResponse = response.body().string();
            media.setUploaded(response.isSuccessful());
            if ( media.getUploaded() ) {
                JSONObject prettyResponse = new JSONObject(stringifyResponse);
                media.setExternalId( prettyResponse.get("id").toString() );
            } else {
                System.out.println(stringifyResponse);
            }

            TimeUnit.SECONDS.sleep(interval);
        }
    }

    private void processUploadStatuses(
            Map<String, Message> statusesToPost,
            Set<String> statusesLocalIds
    ) throws InterruptedException, IOException, ReceiverException {

        for ( String statusId : statusesLocalIds ) {
            Message status = statusesToPost.get(statusId);
            String body = "status=";
            Media media = status.getMediaRepository().isEmpty() ? null : status.getMediaRepository().get(0);
            boolean isMedia = media != null && media.getUploaded();
            if ( !status.getHaveText() && !isMedia ) {
                continue;
            }
            if ( status.getHaveText() ) {
                body += status.getTextMessage();
            }
            if ( isMedia ) {
                String mediaExternalId = media.getExternalId();
                body += "&media_ids[]=" + mediaExternalId;
            }
            if (status.getParentMessageId() != null) {
                Message parentMessage = statusesToPost.get(status.getParentMessageId());
                if (parentMessage.getExternalId() != null) {
                    body += "&in_reply_to_id=" + parentMessage.getExternalId();
                }
            }

            MastodonAPI postStatus = new MastodonAPI();
            postStatus.setMediaType(STATUS_MEDIA_TYPE);
            postStatus.setBody(body);
            String url = baseUrl + "statuses?" + authSign;
            postStatus.setRequest(url, "POST", STATUS_MEDIA_TYPE);
            postStatus.makeCall();
            Response response = postStatus.getResponse();

            String stringifyResponse = response.body().string();
            if (response.isSuccessful()) {
                JSONObject prettyResponse = new JSONObject(stringifyResponse);
                status.setExternalId(prettyResponse.get("id").toString());
            } else {
                System.out.println(stringifyResponse);
            }

            TimeUnit.SECONDS.sleep(interval);
        }
    }
}
