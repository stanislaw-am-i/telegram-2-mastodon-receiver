package org.receiver.reciver;

public class Configuration {

    private String pathToExportedData;
    private String accessToken;
    private String baseUrl;
    private String characterLimit;
    private String interval;

    public String getPathToExportedData() {
        return pathToExportedData;
    }

    public void setPathToExportedData(String pathToExportedData) {
        this.pathToExportedData = pathToExportedData;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCharacterLimit() {
        return characterLimit;
    }

    public void setCharacterLimit(String characterLimit) {
        this.characterLimit = characterLimit;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
