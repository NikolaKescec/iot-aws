package hr.fer.iot.sdkv1.mqtt;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.amazonaws.services.iot.client.auth.Credentials;
import com.amazonaws.services.iot.client.auth.StaticCredentialsProvider;

import java.util.logging.Logger;

public class SimpleAwsMqttClient implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(SimpleAwsMqttClient.class.getName());

    private static final Integer TIMEOUT = 10000;

    private String endpoint;

    private String clientId;

    private String awsAccessKeyId;

    private String awsSecretAccessKey;

    private AWSIotMqttClient client;

    private SimpleAwsMqttClient() {
    }

    public SimpleAwsMqttClient(String clientId, String endpoint) {
        this.clientId = clientId;
        this.endpoint = endpoint;
    }

    public static SimpleAwsMqttClientBuilder getBuilder() {
        return new SimpleAwsMqttClientBuilder();
    }

    public AWSIotMqttClient createConnection() throws AWSIotException, AWSIotTimeoutException {
        if (client != null && client.getConnectionStatus() == AWSIotConnectionStatus.CONNECTED) {
            throw new AWSIotException("Client is already connected");
        }

        final Credentials credentials = new Credentials(awsAccessKeyId, awsSecretAccessKey);
        client = new AWSIotMqttClient(endpoint, clientId, new StaticCredentialsProvider(credentials), "us-east-1");

        client.setCleanSession(true);
        client.connect(TIMEOUT);

        return client;
    }

    @Override
    public void close() throws Exception {
        if (client.getConnectionStatus() == AWSIotConnectionStatus.DISCONNECTED) {
            LOGGER.warning("Client is not connected, no need to disconnect");
        } else {
            client.disconnect();
        }
    }

    public static class SimpleAwsMqttClientBuilder {

        private final SimpleAwsMqttClient simpleAwsMqttClient;

        private SimpleAwsMqttClientBuilder() {
            simpleAwsMqttClient = new SimpleAwsMqttClient();
        }

        public SimpleAwsMqttClientBuilder withClientId(String clientId) {
            simpleAwsMqttClient.clientId = clientId;

            return this;
        }

        public SimpleAwsMqttClientBuilder withEndpoint(String endpoint) {
            simpleAwsMqttClient.endpoint = endpoint;

            return this;
        }

        public SimpleAwsMqttClientBuilder withAwsAccessKeyId(String awsAccessKeyId) {
            simpleAwsMqttClient.awsAccessKeyId = awsAccessKeyId;

            return this;
        }

        public SimpleAwsMqttClientBuilder withAwsSecretAccessKey(String awsSecretAccessKey) {
            simpleAwsMqttClient.awsSecretAccessKey = awsSecretAccessKey;

            return this;
        }

        public SimpleAwsMqttClient build() {
            return simpleAwsMqttClient;
        }

    }

}
