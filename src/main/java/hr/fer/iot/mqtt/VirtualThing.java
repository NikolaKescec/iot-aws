package hr.fer.iot.mqtt;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import hr.fer.iot.mqtt.properties.MqttProperties;

import java.util.Random;
import java.util.TimerTask;
import java.util.logging.Logger;

public class VirtualThing extends TimerTask {

    private static final Logger LOGGER = Logger.getLogger(VirtualThing.class.getName());

    private final AWSIotMqttClient client;

    private final Random random;

    private boolean turnedOn;

    public VirtualThing(AWSIotMqttClient client) {
        this.client = client;
        this.random = new Random();
        this.turnedOn = true;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    @Override
    public void run() {
        if (!turnedOn) {
            return;
        }

        final int topic = random.nextInt(MqttProperties.NUMBER_OF_ROOMS + 1) + 1;
        final String virtual_humidity = String.valueOf(Math.random() * 30);
        final AWSIotMessage message =
            new AWSIotMessage(String.format("room/%d/hum", topic), AWSIotQos.QOS0, virtual_humidity);
        try {
            if (client.getConnectionStatus() == AWSIotConnectionStatus.CONNECTED) {
                client.publish(message);
                LOGGER.info(
                    String.format("Published message %s on topic %s", message.getStringPayload(), message.getTopic()));
            }
        } catch (AWSIotException e) {
            throw new RuntimeException(e);
        }
    }

}