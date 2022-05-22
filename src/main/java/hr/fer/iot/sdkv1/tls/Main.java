package hr.fer.iot.sdkv1.tls;

import hr.fer.iot.mqtt.simpleaws.SimpleAwsMqttExecutor;
import hr.fer.iot.mqtt.simpleaws.client.SimpleAwsMqttClient;
import hr.fer.iot.mqtt.simpleaws.client.SimpleTLSAwsMqttClient;

import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        final ClassLoader classLoader = Main.class.getClassLoader();

        final SimpleTLSAwsMqttClient.SimpleAwsMqttClientBuilder simpleAwsMqttClientBuilder =
            SimpleTLSAwsMqttClient.getBuilder()
                .withClientId("testclient1")
                .withEndpoint("a1rfupjl86n6ia-ats.iot.us-east-1.amazonaws.com")
                .withCertificateFile(classLoader.getResource(
                        "cert_keys/8fe387afe4973c5332c3813fadec43777bde0fc2969d6ef6c9e1a3d9f738bacf-certificate.pem.crt")
                    .getPath())
                .withPrivateKeyFile(classLoader.getResource(
                        "cert_keys/8fe387afe4973c5332c3813fadec43777bde0fc2969d6ef6c9e1a3d9f738bacf-private.pem.key")
                    .getPath());

        try (final SimpleAwsMqttClient simpleTLSAwsMqttClient = simpleAwsMqttClientBuilder.build();) {
            final SimpleAwsMqttExecutor simpleAwsMqttExecutor = new SimpleAwsMqttExecutor(simpleTLSAwsMqttClient);
            simpleAwsMqttExecutor.executeClient();
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }

        LOGGER.info("Complete!");
    }

}