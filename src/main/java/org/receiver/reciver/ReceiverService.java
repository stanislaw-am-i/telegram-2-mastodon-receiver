package org.receiver.reciver;

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

            Configuration config = new Configuration();
            config.setPathToExportedData( properties.getProperty("PATH_TO_EXPORTED_DATA") );
            config.setAccessToken( properties.getProperty("ACCESS_TOKEN") );
            config.setBaseUrl( properties.getProperty("BASE_URL") );
            config.setCharacterLimit( properties.getProperty("CHARACTER_LIMITATION") );
            config.setInterval( properties.getProperty("INTERVAL") );

            File input = new File( config.getPathToExportedData() );
            Document doc = Jsoup.parse(input);
            Elements parsedData = doc.getElementsByClass("message");

            if ( parsedData.isEmpty() ) {
                throw new RuntimeException("The Exported Data Is Empty");
            }

            MessageRepository messages = new MessageRepository(parsedData);
            System.out.println(config.getInterval());
            MastodonService mastodonService = new MastodonService(messages, config);
            mastodonService.postStatusesToMastodon();
        } catch (Exception e) {;
            e.printStackTrace();
        }
    }
}
