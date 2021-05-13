package base.web;

import base.strategy.CalculationService;
import base.strategy.utils.EStrategyNames;
import base.strategy.utils.dto.Request;
import base.strategy.utils.dto.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class StrategyController {

    private CalculationService calculationService;

    @Autowired
    public void setStrategyFactory(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @ApiOperation(value = "Расчет значений согласно стратегии", notes = "method: StrategyController.execute")
    @RequestMapping(value = "/calc/byStrategy/{strategy}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Response> execute(@RequestBody Request requestTO, @PathVariable("strategy") EStrategyNames strategy) {

        Response response = calculationService.getCalculationByStrategy(requestTO, strategy);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }


}
