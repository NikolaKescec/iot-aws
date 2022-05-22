package hr.fer.iot.sdkv1.ws;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import hr.fer.iot.mqtt.SimpleTLSAwsMqttClient;
import hr.fer.iot.mqtt.SimpleWSAwsMqttTopic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String[] TOPIC = new String[] { "topic1", "topic2" };

    public static void main(String[] args) throws IOException {
        final ClassLoader classLoader = Main.class.getClassLoader();

        final String awsKey =
            new String(Files.readAllBytes(Paths.get(classLoader.getResource("secrets/accesskey").getPath())));
        final String awsSecret =
            new String(Files.readAllBytes(Paths.get(classLoader.getResource("secrets/awssecret").getPath())));

        final SimpleTLSAwsMqttClient.SimpleAwsMqttClientBuilder simpleAwsMqttClientBuilder =
            SimpleTLSAwsMqttClient.getBuilder()
                .withClientId("test-thing")
                .withEndpoint("a1rfupjl86n6ia-ats.iot.us-east-1.amazonaws.com")
                .withAwsAccessKeyId(awsKey)
                .withAwsSecretAccessKey(awsSecret);

        try (final SimpleTLSAwsMqttClient simpleTLSAwsMqttClient = simpleAwsMqttClientBuilder.build()) {
            final AWSIotMqttClient client = simpleTLSAwsMqttClient.createConnection();

            final String payload = "{\"message\": \"HELLO WORLD\"}";
            final AWSIotMessage message = new AWSIotMessage(TOPIC[0], AWSIotQos.QOS0, payload);
            client.publish(message);

            final SimpleWSAwsMqttTopic awsIotTopic = new SimpleWSAwsMqttTopic(TOPIC[1], AWSIotQos.QOS0);
            client.subscribe(awsIotTopic);

            final Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            scanner.close();
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }

        LOGGER.info("Complete!");
    }

}