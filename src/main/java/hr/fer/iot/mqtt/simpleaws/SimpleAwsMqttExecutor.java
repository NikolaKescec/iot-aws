package hr.fer.iot.mqtt.simpleaws;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import hr.fer.iot.mqtt.VirtualThing;
import hr.fer.iot.mqtt.properties.MqttProperties;
import hr.fer.iot.mqtt.simpleaws.client.SimpleAwsMqttClient;

import java.util.Scanner;
import java.util.Timer;
import java.util.logging.Logger;

public class SimpleAwsMqttExecutor {

    private static final Logger LOGGER = Logger.getLogger(SimpleAwsMqttExecutor.class.getName());

    private final SimpleAwsMqttClient simpleAwsMqttClient;

    public SimpleAwsMqttExecutor(SimpleAwsMqttClient simpleAwsMqttClient) {
        this.simpleAwsMqttClient = simpleAwsMqttClient;
    }

    public void executeClient() {
        final Timer timer = new Timer();
        try (final Scanner scanner = new Scanner(System.in)) {
            final AWSIotMqttClient client = simpleAwsMqttClient.createConnection();

            final VirtualThing virtualThing = new VirtualThing(client);
            virtualThing.setTurnedOn(false);
            timer.schedule(virtualThing, 5000, 5000);

            for (int i = 1; i <= MqttProperties.NUMBER_OF_ROOMS; i++) {
                final SimpleAwsMqttTopic simpleAwsMqttTopic =
                    new SimpleAwsMqttTopic(String.format("room/%d/act", i), AWSIotQos.QOS0);
                client.subscribe(simpleAwsMqttTopic);

                LOGGER.info(String.format("Subscribed to topic: %s", simpleAwsMqttTopic.getTopic()));
            }

            scanner.nextLine();
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        } finally {
            timer.cancel();
            timer.purge();
        }
    }

}
