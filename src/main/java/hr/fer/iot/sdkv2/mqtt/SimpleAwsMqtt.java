package hr.fer.iot.sdkv2.mqtt;

import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

public class SimpleAwsMqtt {

    private MqttClientConnectionEvents mqttClientConnectionEvents;

    private String clientId;

    private String endpoint;

    private String certificatePath;

    private String privateKeyPath;

    private String rootCAPath;

    private SimpleAwsMqtt() {
    }

    public SimpleAwsMqtt(String clientId, String endpoint, String certificatePath, String privateKeyPath,
        String rootCAPath) {
        this.clientId = clientId;
        this.endpoint = endpoint;
        this.certificatePath = certificatePath;
        this.privateKeyPath = privateKeyPath;
        this.rootCAPath = rootCAPath;
    }

    public static SimpleAwsMqttBuilder getBuilder() {
        return new SimpleAwsMqttBuilder();
    }

    public MqttClientConnection createConnection() {
        try (AwsIotMqttConnectionBuilder builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(
                this.certificatePath, this.privateKeyPath)
            .withCertificateAuthorityFromPath(null, this.rootCAPath)
            .withConnectionEventCallbacks(this.mqttClientConnectionEvents)
            .withClientId(this.clientId)
            .withEndpoint(this.endpoint)
            .withPort((short) 8883)
            .withCleanSession(true)
            .withProtocolOperationTimeoutMs(10000)) {
            return builder.build();
        }
    }

    public static class SimpleAwsMqttBuilder {

        private final SimpleAwsMqtt simpleAwsMqttClient;

        private SimpleAwsMqttBuilder() {
            simpleAwsMqttClient = new SimpleAwsMqtt();
        }

        public SimpleAwsMqttBuilder withClientId(String clientId) {
            simpleAwsMqttClient.clientId = clientId;

            return this;
        }

        public SimpleAwsMqttBuilder withEndpoint(String endpoint) {
            simpleAwsMqttClient.endpoint = endpoint;

            return this;
        }

        public SimpleAwsMqttBuilder withCertificatePath(String certificatePath) {
            simpleAwsMqttClient.certificatePath = certificatePath;

            return this;
        }

        public SimpleAwsMqttBuilder withPrivateKeyPath(String privateKeyPath) {
            simpleAwsMqttClient.privateKeyPath = privateKeyPath;

            return this;
        }

        public SimpleAwsMqttBuilder withRootCAPath(String rootCAPath) {
            simpleAwsMqttClient.rootCAPath = rootCAPath;

            return this;
        }

        public SimpleAwsMqttBuilder withCallbacks(MqttClientConnectionEvents mqttClientConnectionEvents) {
            simpleAwsMqttClient.mqttClientConnectionEvents = mqttClientConnectionEvents;

            return this;
        }

        public SimpleAwsMqtt build() {
            return simpleAwsMqttClient;
        }

    }

}
