package hr.fer.iot.mqtt.simpleaws;

import com.amazonaws.services.iot.client.AWSIotQos;
import hr.fer.iot.mqtt.VirtualThing;
import hr.fer.iot.mqtt.properties.MqttProperties;
import hr.fer.iot.mqtt.simpleaws.client.SimpleAwsMqttClient;

import java.util.Scanner;
import java.util.Timer;
import java.util.logging.Logger;

public final class SimpleAwsMqttExecutor {

    private static final Logger LOGGER = Logger.getLogger(SimpleAwsMqttExecutor.class.getName());

    private SimpleAwsMqttExecutor() {
    }

    public static void executeClient(SimpleAwsMqttClient simpleAwsMqttClient) {
        final Timer timer = new Timer();
        try (final Scanner scanner = new Scanner(System.in)) {
            simpleAwsMqttClient.prepareClient();

            final VirtualThing virtualThing = new VirtualThing(simpleAwsMqttClient);
            virtualThing.setTurnedOn(true);
            timer.schedule(virtualThing, 5000, 15000);

            for (int i = 1; i <= MqttProperties.NUMBER_OF_VIRTUAL_ROOMS; i++) {
                final SimpleAwsMqttTopic simpleAwsMqttTopic =
                    new SimpleAwsMqttTopic(String.format("room/%d/act", i), AWSIotQos.QOS0);
                simpleAwsMqttClient.subscribe(simpleAwsMqttTopic);

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
