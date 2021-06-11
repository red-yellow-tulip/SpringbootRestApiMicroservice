package app.controller;

import app.client.service.DataClient;
import app.controller.mapper.Mapper;
import app.controller.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.controller.dto.*;
import app.controller.utils.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/data")
public class DataController {

    private static final Logger log = LoggerFactory.getLogger(DataController.class);

    private Mapper mapper = null;

    private DataService dataService;

    @Autowired
    public void setStrategyFactory(DataService dataService) {
        this.dataService = dataService;
    }

    @Autowired
    public void setStrategyFactory(Mapper mapper) {
        this.mapper = mapper;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public Flux<ResponseDTO> getAllData() {

        Flux<ResponseDTO>  res = dataService.getAll();

        return  res;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public Mono<ResponseDTO> getData(@PathVariable  String id) {

        if (id == null || id.isEmpty())
            return Mono.error(new RuntimeException("id is bad"));

        Response response = dataService.execute(id);

        ResponseDTO responseDTO = mapper.toResponseDTO(response);

        return Mono.just(responseDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public Mono<ResponseDTO> postData(@RequestBody RequestDTO requestTO, @PathVariable String id) {

        if (id == null || id.isEmpty())
            return Mono.error(new RuntimeException("id is bad"));

        if (requestTO == null )
            return Mono.error(new RuntimeException("RequestDTO is bad"));

        Request request = mapper.toRequest(requestTO);

        Response response = dataService.execute(request);

        ResponseDTO responseDTO = mapper.toResponseDTO(response);

        return Mono.just(responseDTO);
    }
}
