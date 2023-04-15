package org.receiver;

import org.receiver.reciver.ReceiverService;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        ReceiverService receiver = new ReceiverService();
        receiver.processedExportedDataToPostStatuses();
    }
}