package app.controller;

import app.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.util.concurrent.ListenableFuture;
import javax.annotation.Resource;

@Resource
public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<Long, UserDto> kafkaTemplate;

    public boolean send(String topic, Long key, @Nullable UserDto data) {

        LOGGER.info("sending topic='{}' to key='{}' to data='{}'", topic, key,data);

        ListenableFuture<SendResult<Long, UserDto>> future = kafkaTemplate.send(topic, key, data);
        future.addCallback(System.out::println, System.err::println);
        kafkaTemplate.flush();
        return future.isDone();
    }
}