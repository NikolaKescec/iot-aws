package hr.fer.iot.mqtt.simpleaws.client;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import hr.fer.iot.mqtt.properties.MqttProperties;
import hr.fer.iot.mqtt.simpleaws.SimpleAwsMqttTopic;

import java.util.logging.Logger;

public abstract class SimpleAwsMqttClient implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(SimpleAwsMqttClient.class.getName());

    protected AWSIotMqttClient client;

    public void prepareClient() {
        client = this.initiate();
        client.setCleanSession(true);
    }

    public void publish(AWSIotMessage message) throws AWSIotException, AWSIotTimeoutException {
        if (client.getConnectionStatus() == AWSIotConnectionStatus.DISCONNECTED) {
            client.connect(MqttProperties.TIMEOUT);
            LOGGER.info("Not connected to AWS. Connection established.");
        }

        client.publish(message);
    }

    public void subscribe(SimpleAwsMqttTopic topic) throws AWSIotException, AWSIotTimeoutException {
        if (client.getConnectionStatus() == AWSIotConnectionStatus.DISCONNECTED) {
            client.connect(MqttProperties.TIMEOUT, true);
            LOGGER.info("Not connected to AWS. Connection established.");
        }

        client.subscribe(topic);
    }

    protected abstract AWSIotMqttClient initiate();

    public void close() throws Exception {
        if (client.getConnectionStatus() == AWSIotConnectionStatus.DISCONNECTED) {
            LOGGER.warning("Client is not connected, no need to disconnect");
        } else {
            client.disconnect();
        }
    }

}
