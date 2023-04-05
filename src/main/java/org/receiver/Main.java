package org.receiver;


import org.receiver.reciver.ReceiverService;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        ReceiverService receiver = new ReceiverService();
        receiver.processedExportedDataToPostStatuses();

        /*File input = new File("src/main/resources/ChatExport_2023-02-19/messages.html");
        Document doc = Jsoup.parse(input);
        Elements messages = doc.getElementsByClass("message");
        Elements contentMessages = new Elements();

        MessageRepository mr = new MessageRepository(messages);*/

        /*Map<String, String> parameters = new HashMap<>();
        parameters.put("status", "Snaps are proprietary and take ages to open.");

        String form = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://mastodon.world/api/v1/statuses?access_token=9q5rGtdB5lKkt9dAmdqsrb3LiTLYTEjUAOp_zDkehVI"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());

        System.out.println(postResponse);*/

        /*OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("image/jpeg");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file","linux_mem2.jpeg",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File("src/main/resources/ChatExport_2023-02-19/photos/linux_mem2.jpeg")))
                .build();
        Request request = new Request.Builder()
                .url("https://mastodon.world/api/v1/media?access_token=9q5rGtdB5lKkt9dAmdqsrb3LiTLYTEjUAOp_zDkehVI")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();

        System.out.println("response statusCode = " + response.code());
        System.out.println("response body = " + response.body().string());*/

        //110007005713306888


        //text = text message
        //media_voice_message = voice msg
        //photo_wrap = image
        //animated_wrap = gif ?? animated
        //reply_to = reply
        //media_file = file(pdf)
        //video_file_wrap = video
        //media_photo = sticker

        /*for (Element message : messages) {
            if ( message.classNames().contains("default") ) {
                //contentMessages.add(message);
                String text = message.getElementsByClass("text").text();
                System.out.println(text);
            }
        }*/

    }
}