package app.controller;

import app.controller.mapper.Mapper;
import app.controller.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.controller.dto.*;
import app.controller.utils.*;

@RestController
@RequestMapping("/data")
public class DataController {

    private Mapper mapper;

    private DataService dataService;

    @Autowired
    public void setStrategyFactory(DataService dataService) {
        this.dataService = dataService;
    }

    @Autowired
    public void setStrategyFactory(Mapper mapper) {
        this.mapper = mapper;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<ResponseDTO> getData(@PathVariable  String id) {


        Response response = dataService.execute(id);

        ResponseDTO responseDTO = mapper.toResponseDTO(response);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<ResponseDTO> postData(@RequestBody RequestDTO requestTO,  @PathVariable String id) {

        Request request = mapper.toRequest(requestTO);

        Response response = dataService.execute(request);

        ResponseDTO responseDTO = mapper.toResponseDTO(response);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseDTO);
    }
}
