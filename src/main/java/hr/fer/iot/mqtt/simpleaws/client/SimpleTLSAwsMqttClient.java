package hr.fer.iot.mqtt.simpleaws.client;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import hr.fer.iot.sdkv1.tls.amazon.SampleUtil;

public class SimpleTLSAwsMqttClient extends SimpleAwsMqttClient {

    private String endpoint;

    private String clientId;

    private String certificateFile;

    private String privateKeyFile;

    private SimpleTLSAwsMqttClient() {
    }

    public static SimpleAwsMqttClientBuilder getBuilder() {
        return new SimpleAwsMqttClientBuilder();
    }

    @Override
    protected AWSIotMqttClient initiate() {
        final SampleUtil.KeyStorePasswordPair pair =
            SampleUtil.getKeyStorePasswordPair(this.certificateFile, this.privateKeyFile);

        return new AWSIotMqttClient(endpoint, clientId, pair.keyStore, pair.keyPassword);
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

        public SimpleAwsMqttClientBuilder withCertificateFile(String certificateFile) {
            simpleTLSAwsMqttClient.certificateFile = certificateFile;

            return this;
        }

        public SimpleAwsMqttClientBuilder withPrivateKeyFile(String privateKeyFile) {
            simpleTLSAwsMqttClient.privateKeyFile = privateKeyFile;

            return this;
        }

        public SimpleTLSAwsMqttClient build() {
            return simpleTLSAwsMqttClient;
        }

    }

}
