package hr.fer.iot.sdkv1.mqtt;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

import java.util.logging.Logger;

public class SimpleAwsMqttTopic extends AWSIotTopic {

    private static final Logger LOGGER = Logger.getLogger(SimpleAwsMqttTopic.class.getName());

    public SimpleAwsMqttTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        LOGGER.info("Message received: " + message.getStringPayload());
    }

}

