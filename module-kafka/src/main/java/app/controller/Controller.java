package app.controller;

import app.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/msg")
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private Producer producer;

    @RequestMapping(value = "", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public void send( @RequestParam(value="topic") String topic, @RequestParam(value="msgId") long msgId, @RequestBody UserDto userDto)   {

        LOGGER.info("sending msg='{}' to topic='{}', msgId='{}'",userDto, topic, msgId );
        producer.send(topic,msgId, userDto);
    }





}
