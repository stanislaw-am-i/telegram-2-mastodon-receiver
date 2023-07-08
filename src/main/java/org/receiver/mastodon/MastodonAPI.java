package org.receiver.mastodon;

import okhttp3.*;
import org.receiver.receiver.ReceiverException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MastodonAPI {

    private OkHttpClient client;
    private MediaType mediaType;
    private RequestBody body;
    private Request request;
    private Response response;

    private static volatile MastodonAPI instance;

    public static MastodonAPI getInstance() {
        MastodonAPI localInstance = instance;
        if (localInstance == null) {
            synchronized (MastodonAPI.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MastodonAPI();
                }
            }
        }
        return localInstance;
    }

    private MastodonAPI() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        client = builder.build();
    }

    public void makeCall() throws ReceiverException, IOException {
        if ( request == null ) {
            throw new ReceiverException("Please, Create The Body Of Request First");
        }

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = MediaType.parse(mediaType);;
    }

    public void setBody(String status) throws ReceiverException {
        if ( mediaType == null ) {
            throw new ReceiverException("Please, Specify The Media Type First");
        }

        if ( status.isBlank() ) {
            throw new ReceiverException("The Status Can't Be Empty");
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
        return response;
    }
}
