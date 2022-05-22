package hr.fer.iot.mqtt;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

import java.util.logging.Logger;

public class SimpleWSAwsMqttTopic extends AWSIotTopic {

    private static final Logger LOGGER = Logger.getLogger(SimpleWSAwsMqttTopic.class.getName());

    public SimpleWSAwsMqttTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        LOGGER.info("Message received: " + message.getStringPayload());
    }

}

