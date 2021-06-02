package app.config;

import app.controller.Producer;
import app.dto.UserDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;

     @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServer);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer/*StringSerializer*/.class);

        return props;
    }

    @Bean
    public ProducerFactory<Long, UserDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, UserDto> kafkaTemplate() {
        KafkaTemplate<Long, UserDto> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        //kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
        return kafkaTemplate;
    }

    @Bean
    public Producer getProducerKafka(){
        return new Producer();
    }

}