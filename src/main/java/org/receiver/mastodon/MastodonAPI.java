package org.receiver.mastodon;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class MastodonAPI {

    private OkHttpClient client;
    private MediaType mediaType;
    private RequestBody body;
    private Request request;
    private Response response;

    public MastodonAPI() {
        client = new OkHttpClient().newBuilder().build();
    }

    public void makeCall() throws IOException {
        if ( request == null ) {
            throw new RuntimeException("Please, Create The Body Of Request First");
        }

        response = client.newCall(request).execute();
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = MediaType.parse(mediaType);;
    }

    public void setBody(String status) {
        if ( mediaType == null ) {
            throw new RuntimeException("Please, Specify The Media Type First");
        }

        if ( status.isBlank() ) {
            throw new RuntimeException("The Status Can't Be Empty");
        }

        this.body = RequestBody.create(mediaType, status);
    }

    public void setBody(String fileName, String filePath) {
        if ( fileName.isBlank() ) {
            throw new RuntimeException("Please, Specify The File's Name");
        }

        if ( filePath.isBlank() ) {
            throw new RuntimeException("Please, Specify The File's Path");
        }

        this.body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file",fileName,
                        RequestBody.create(
                                MediaType.parse("application/octet-stream"),
                                new File(filePath)
                        )
                )
                .build();
    }

    public void setRequest(String url, String methodType) {
        if ( body == null ) {
            throw new RuntimeException("Please, Create The Body Of Request First");
        }

        if ( url.isBlank() ) {
            throw new RuntimeException("Please, Provide The Url Of Request");
        }

        if ( methodType.isBlank() ) {
            throw new RuntimeException("Please, Specify The Method's Type");
        }

        this.request = new Request.Builder()
                .url(url)
                .method(methodType, body)
                .build();
    }

    public void setRequest(String url, String methodType, String contentType) {
        if ( body == null ) {
            throw new RuntimeException("Please, Create The Body Of Request First");
        }

        if ( url.isBlank() ) {
            throw new RuntimeException("Please, Provide The Url Of Request");
        }

        if ( methodType.isBlank() ) {
            throw new RuntimeException("Please, Specify The Method's Type");
        }

        if ( contentType.isBlank() ) {
            throw new RuntimeException("Please, Specify The Content's Type");
        }

        this.request = new Request.Builder()
                .url(url)
                .method(methodType, body)
                .addHeader("Content-Type", contentType)
                .build();
    }

    public Response getResponse() {
        //TODO response class
        return response;
    }
}
