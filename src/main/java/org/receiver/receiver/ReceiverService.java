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

            File input = new File( pathToData );
            Document doc = Jsoup.parse(input);
            Elements parsedData = doc.getElementsByClass("message");

            if ( parsedData.isEmpty() ) {
                throw new RuntimeException("The Exported Data Is Empty");
            }

            MessageRepository messages = new MessageRepository(parsedData, pathToData, charactersLimit);
            MastodonService mastodonService = new MastodonService(messages, baseUrl, authSign, interval);
            mastodonService.postStatusesToMastodon();
        } catch (Exception e) {;
            e.printStackTrace();
        }
    }
}
