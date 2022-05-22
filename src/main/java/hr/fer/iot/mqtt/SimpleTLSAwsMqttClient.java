package hr.fer.iot.mqtt;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.amazonaws.services.iot.client.auth.Credentials;
import com.amazonaws.services.iot.client.auth.StaticCredentialsProvider;

import java.util.logging.Logger;

public class SimpleTLSAwsMqttClient implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(SimpleTLSAwsMqttClient.class.getName());

    private static final Integer TIMEOUT = 10000;

    private String endpoint;

    private String clientId;

    private String awsAccessKeyId;

    private String awsSecretAccessKey;

    private AWSIotMqttClient client;

    private SimpleTLSAwsMqttClient() {
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

        private final SimpleTLSAwsMqttClient simpleTLSAwsMqttClient;

        private SimpleAwsMqttClientBuilder() {
            simpleTLSAwsMqttClient = new SimpleTLSAwsMqttClient();
        }

        public SimpleAwsMqttClientBuilder withClientId(String clientId) {
            simpleTLSAwsMqttClient.clientId = clientId;

            return this;
        }

        public SimpleAwsMqttClientBuilder withEndpoint(String endpoint) {
            simpleTLSAwsMqttClient.endpoint = endpoint;

            return this;
        }

        public SimpleAwsMqttClientBuilder withAwsAccessKeyId(String awsAccessKeyId) {
            simpleTLSAwsMqttClient.awsAccessKeyId = awsAccessKeyId;

            return this;
        }

        public SimpleAwsMqttClientBuilder withAwsSecretAccessKey(String awsSecretAccessKey) {
            simpleTLSAwsMqttClient.awsSecretAccessKey = awsSecretAccessKey;

            return this;
        }

        public SimpleTLSAwsMqttClient build() {
            return simpleTLSAwsMqttClient;
        }

    }

}
