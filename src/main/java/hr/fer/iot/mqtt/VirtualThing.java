package hr.fer.iot.mqtt;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import hr.fer.iot.mqtt.properties.MqttProperties;
import hr.fer.iot.mqtt.simpleaws.client.SimpleAwsMqttClient;

import java.util.Random;
import java.util.TimerTask;
import java.util.logging.Logger;

public class VirtualThing extends TimerTask {

    private static final Logger LOGGER = Logger.getLogger(VirtualThing.class.getName());

    private static final Integer INITIAL_HUMIDITY_MAXIMUM = 50;

    private static final Integer INITIAL_HUMIDITY_MINIMUM = 30;

    private static final Integer HUMIDITY_DIFF = 3;

    private final SimpleAwsMqttClient simpleAwsMqttClient;

    private final Random random;

    private double prevHumidity;

    private boolean turnedOn;

    public VirtualThing(SimpleAwsMqttClient simpleAwsMqttClient) {
        this.simpleAwsMqttClient = simpleAwsMqttClient;
        this.random = new Random();
        this.turnedOn = true;
        this.prevHumidity = random.nextDouble() * INITIAL_HUMIDITY_MAXIMUM + INITIAL_HUMIDITY_MINIMUM;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    @Override
    public void run() {
        if (!turnedOn) {
            return;
        }

        final int topic = random.nextInt(MqttProperties.NUMBER_OF_VIRTUAL_ROOMS) + 1;
        prevHumidity = this.prevHumidity + random.nextDouble() * HUMIDITY_DIFF * (random.nextBoolean() ? -1 : 1);
        if(prevHumidity < 0) {
            prevHumidity = 0;
        }
        final AWSIotMessage message =
            new AWSIotMessage(String.format("room/%d/hum", topic), AWSIotQos.QOS0, String.valueOf(prevHumidity));
        try {
            simpleAwsMqttClient.publish(message);
            LOGGER.info(
                String.format("Published message %s on topic %s", message.getStringPayload(), message.getTopic()));
        } catch (AWSIotException | AWSIotTimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}