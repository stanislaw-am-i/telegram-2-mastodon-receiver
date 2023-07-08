package org.receiver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.receiver.receiver.ReceiverService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceiverServiceTest {

    private Properties originalProperties;
    private File configFile;

    @BeforeEach
    public void setUp() throws Exception {
        // Save the original properties in the config file
        configFile = new File("config");
        originalProperties = new Properties();
        originalProperties.load(new java.io.FileInputStream(configFile));

        // Override the properties to use a mock file
        Properties mockProperties = new Properties();
        mockProperties.setProperty("PATH_TO_EXPORTED_DATA", "path/to/data/");
        mockProperties.setProperty("CHARACTER_LIMITATION", "100");
        mockProperties.setProperty("BASE_URL", "http://example.com");
        mockProperties.setProperty("ACCESS_TOKEN", "secret");
        mockProperties.setProperty("INTERVAL", "10");
        mockProperties.store(new java.io.FileOutputStream(configFile), "Test properties");
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Reset the properties in the config file to their original values
        originalProperties.store(new java.io.FileOutputStream(configFile), "Original properties");
    }

    @Test
    void testProcessedExportedDataToPostStatuses() throws Exception {
        // Redirect console output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ReceiverService receiverService = new ReceiverService();

        // Call the method and assert that it prints the expected message
        receiverService.processedExportedDataToPostStatuses();
        String expectedOutput = "class java.io.FileNotFoundException path/to/data/messages.html (No such file or directory) null\n" +
                "   Root cause: main com.intellij.rt.junit.JUnitStarter\n" +
                "   Penultimate cause: method=prepareStreamsAndStart class=com.intellij.rt.junit.JUnitStarter line=232";

        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }
}
