package hr.fer.iot.sdkv2;

import hr.fer.iot.sdkv2.mqtt.SimpleAwsMqtt;
import software.amazon.awssdk.crt.CRT;
import software.amazon.awssdk.crt.CrtResource;
import software.amazon.awssdk.crt.CrtRuntimeException;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    private final static String topic = "topic1";

    public static void main(String[] args) {
        final ClassLoader classLoader = Main.class.getClassLoader();

        final MqttClientConnectionEvents mqttClientConnectionEvents = new MqttClientConnectionEvents() {
            @Override
            public void onConnectionInterrupted(int errorCode) {
                if (errorCode != 0) {
                    System.out.println("Connection interrupted: " + errorCode + ": " + CRT.awsErrorString(errorCode));
                }
            }

            @Override
            public void onConnectionResumed(boolean sessionPresent) {
                System.out.println("Connection resumed: " + (sessionPresent ? "existing session" : "clean session"));
            }
        };

        final SimpleAwsMqtt simpleAwsMqttClient = SimpleAwsMqtt.getBuilder()
            .withClientId("test-thing")
            .withEndpoint("a1rfupjl86n6ia-ats.iot.us-east-1.amazonaws.com")
            .withCertificatePath(classLoader.getResource(
                    "cert_keys/8fe387afe4973c5332c3813fadec43777bde0fc2969d6ef6c9e1a3d9f738bacf-certificate.pem.crt")
                .getPath())
            .withPrivateKeyPath(classLoader.getResource(
                "cert_keys/8fe387afe4973c5332c3813fadec43777bde0fc2969d6ef6c9e1a3d9f738bacf-private.pem.key").getPath())
            .withRootCAPath(classLoader.getResource("cert_keys/root-CA.pem").getPath())
            .withCallbacks(mqttClientConnectionEvents)
            .build();

        try (MqttClientConnection connection = simpleAwsMqttClient.createConnection()) {
            CompletableFuture<Boolean> connected = connection.connect();

            boolean sessionPresent = connected.get();
            System.out.println("Connected to " + (!sessionPresent ? "new" : "existing") + " session!");

            CompletableFuture<Integer> subscribed =
                connection.subscribe(topic, QualityOfService.AT_LEAST_ONCE, (message) -> {
                    String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                    System.out.println("MESSAGE: " + payload);
                });

            subscribed.get();

            CompletableFuture<Integer> published = connection.publish(new MqttMessage(topic,
                "HELLO PEOPLE".getBytes(StandardCharsets.UTF_8), QualityOfService.AT_LEAST_ONCE, false));

            published.get();

            CompletableFuture<Void> disconnected = connection.disconnect();

            disconnected.get();
        } catch (CrtRuntimeException | InterruptedException | ExecutionException ex) {
            System.err.println(ex.getMessage());
        }

        CrtResource.waitForNoResources();
        System.out.println("Complete!");
    }

}