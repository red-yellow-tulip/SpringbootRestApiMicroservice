package app.controller;

import app.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "topic1")
    public void receive(String msg) throws JsonProcessingException {

        UserDto userDto = objectMapper.readValue(msg, UserDto.class);
        LOGGER.info("received object UsedDto='{}'", userDto);
        handleMessage(userDto);
    }

    @KafkaListener(topics = "topic2", containerFactory = "kafkaListenerContainerFactory")
    public void receive(ConsumerRecord<Long, String> record) throws JsonProcessingException {

        UserDto userDto = objectMapper.readValue(record.value(), UserDto.class);
        LOGGER.info("received partition='{}, key='{}', 'UserDto='{}'", record.partition(),record.key(),userDto);
        handleMessage(userDto);
    }

    private void handleMessage(UserDto userDto) {

    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("topic1")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("topic2")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
