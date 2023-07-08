package org.receiver;

import org.receiver.receiver.ReceiverService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Started...");
        new ReceiverService().processedExportedDataToPostStatuses();
    }
}