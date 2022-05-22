package hr.fer.iot.mqtt.simpleaws.client;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.auth.Credentials;
import com.amazonaws.services.iot.client.auth.StaticCredentialsProvider;
import hr.fer.iot.mqtt.properties.MqttProperties;

public class SimpleWSAwsMqttClient extends SimpleAwsMqttClient {

    private String endpoint;

    private String clientId;

    private String awsAccessKeyId;

    private String awsSecretAccessKey;

    private AWSIotMqttClient awsIotMqttClient;

    private SimpleWSAwsMqttClient() {
    }

    public static SimpleAwsMqttClientBuilder getBuilder() {
        return new SimpleAwsMqttClientBuilder();
    }

    @Override
    protected AWSIotMqttClient initiate() {
        final Credentials credentials = new Credentials(awsAccessKeyId, awsSecretAccessKey);

        return new AWSIotMqttClient(endpoint, clientId, new StaticCredentialsProvider(credentials),
            MqttProperties.REGION);
    }

    public static class SimpleAwsMqttClientBuilder {

        private final SimpleWSAwsMqttClient simpleWSAwsMqttClient;

        private SimpleAwsMqttClientBuilder() {
            simpleWSAwsMqttClient = new SimpleWSAwsMqttClient();
        }

        public SimpleAwsMqttClientBuilder withClientId(String clientId) {
            simpleWSAwsMqttClient.clientId = clientId;

            return this;
        }

        public SimpleAwsMqttClientBuilder withEndpoint(String endpoint) {
            simpleWSAwsMqttClient.endpoint = endpoint;

            return this;
        }

        public SimpleAwsMqttClientBuilder withAwsAccessKeyId(String awsAccessKeyId) {
            simpleWSAwsMqttClient.awsAccessKeyId = awsAccessKeyId;

            return this;
        }

        public SimpleAwsMqttClientBuilder withAwsSecretAccessKey(String awsSecretAccessKey) {
            simpleWSAwsMqttClient.awsSecretAccessKey = awsSecretAccessKey;

            return this;
        }

        public SimpleWSAwsMqttClient build() {
            return simpleWSAwsMqttClient;
        }

    }

}
