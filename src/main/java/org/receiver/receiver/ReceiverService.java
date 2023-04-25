package org.receiver.receiver;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.receiver.mastodon.MastodonService;
import org.receiver.messages.MessageRepository;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class ReceiverService {

    public void processedExportedDataToPostStatuses() {
        try ( FileReader reader =  new FileReader("config") ) {
            Properties properties = new Properties();
            properties.load(reader);

            String pathToData = properties.getProperty("PATH_TO_EXPORTED_DATA");
            int charactersLimit = Integer.valueOf(properties.getProperty("CHARACTER_LIMITATION"));
            String baseUrl = properties.getProperty("BASE_URL");
            String authSign = properties.getProperty("ACCESS_TOKEN");
            int interval = Integer.valueOf(properties.getProperty("INTERVAL"));

            File input = new File( pathToData + "messages.html" );
            Document doc = Jsoup.parse(input);
            Elements parsedData = doc.getElementsByClass("message");

            if ( parsedData.isEmpty() ) {
                throw new ReceiverException("The Exported Data Is Empty");
            }

            MessageRepository messages = new MessageRepository(parsedData, pathToData, charactersLimit);
            MastodonService mastodonService = new MastodonService(messages, baseUrl, authSign, interval);
            mastodonService.postStatusesToMastodon();
        } catch (Exception e) {;
            System.out.println(e.getClass() + " " + e.getMessage() + " " + e.getCause());
            int size = e.getStackTrace().length - 1;
            System.out.println("   Root cause: " + e.getStackTrace()[size].getMethodName() + " " + e.getStackTrace()[size].getClassName());
            if (size>1) {
                System.out.println("   Penultimate cause: method=" + e.getStackTrace()[size-1].getMethodName() + " class=" + e.getStackTrace()[size-1].getClassName() +
                        " line=" + e.getStackTrace()[size-1].getLineNumber());
            }
        }
    }
}
