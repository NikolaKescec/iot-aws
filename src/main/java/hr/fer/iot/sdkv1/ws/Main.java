package hr.fer.iot.sdkv1.ws;

import hr.fer.iot.mqtt.simpleaws.SimpleAwsMqttExecutor;
import hr.fer.iot.mqtt.simpleaws.client.SimpleAwsMqttClient;
import hr.fer.iot.mqtt.simpleaws.client.SimpleWSAwsMqttClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        final ClassLoader classLoader = Main.class.getClassLoader();

        final String awsKey =
            new String(Files.readAllBytes(Paths.get(classLoader.getResource("secrets/accesskey").getPath())));
        final String awsSecret =
            new String(Files.readAllBytes(Paths.get(classLoader.getResource("secrets/awssecret").getPath())));

        final SimpleWSAwsMqttClient.SimpleAwsMqttClientBuilder simpleAwsMqttClientBuilder =
            SimpleWSAwsMqttClient.getBuilder()
                .withClientId("test-thing")
                .withEndpoint("a1rfupjl86n6ia-ats.iot.us-east-1.amazonaws.com")
                .withAwsAccessKeyId(awsKey)
                .withAwsSecretAccessKey(awsSecret);

        try (final SimpleAwsMqttClient simpleWSAwsMqttClient = simpleAwsMqttClientBuilder.build();) {
            final SimpleAwsMqttExecutor simpleAwsMqttExecutor = new SimpleAwsMqttExecutor(simpleWSAwsMqttClient);
            simpleAwsMqttExecutor.executeClient();
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }

        LOGGER.info("Complete!");
    }

}