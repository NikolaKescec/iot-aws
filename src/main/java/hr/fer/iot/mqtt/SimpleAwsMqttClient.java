package hr.fer.iot.mqtt;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import hr.fer.iot.sdkv1.tls.amazon.SampleUtil;

import java.util.logging.Logger;

public class SimpleAwsMqttClient implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(SimpleAwsMqttClient.class.getName());

    private static final Integer TIMEOUT = 10000;

    private String endpoint;

    private String clientId;

    private String certificateFile;

    private String privateKeyFile;

    private AWSIotMqttClient client;

    private SimpleAwsMqttClient() {
    }

    public static SimpleAwsMqttClientBuilder getBuilder() {
        return new SimpleAwsMqttClientBuilder();
    }

    public AWSIotMqttClient createConnection() throws AWSIotException, AWSIotTimeoutException {
        if (client != null && client.getConnectionStatus() == AWSIotConnectionStatus.CONNECTED) {
            LOGGER.warning("Client is already connected");

            return client;
        }

        final SampleUtil.KeyStorePasswordPair pair =
            SampleUtil.getKeyStorePasswordPair(this.certificateFile, this.privateKeyFile);
        client = new AWSIotMqttClient(endpoint, clientId, pair.keyStore, pair.keyPassword);

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

        public SimpleAwsMqttClientBuilder withCertificateFile(String certificateFile) {
            simpleAwsMqttClient.certificateFile = certificateFile;

            return this;
        }

        public SimpleAwsMqttClientBuilder withPrivateKeyFile(String privateKeyFile) {
            simpleAwsMqttClient.privateKeyFile = privateKeyFile;

            return this;
        }

        public SimpleAwsMqttClient build() {
            return simpleAwsMqttClient;
        }

    }

}
