package hr.fer.iot.mqtt.simpleaws.client;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import hr.fer.iot.mqtt.properties.MqttProperties;

import java.util.logging.Logger;

public abstract class SimpleAwsMqttClient implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(SimpleAwsMqttClient.class.getName());

    protected AWSIotMqttClient client;

    public AWSIotMqttClient createConnection() throws AWSIotException, AWSIotTimeoutException {
        if (client != null && client.getConnectionStatus() == AWSIotConnectionStatus.CONNECTED) {
            LOGGER.warning("Client is already connected");

            return client;
        }

        client = this.initiate();

        client.setCleanSession(true);
        client.connect(MqttProperties.TIMEOUT);

        return client;
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
