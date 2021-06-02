package app.controller;

import app.strategy.CalculationService;
import app.strategy.utils.EStrategyNames;
import app.strategy.utils.dto.Request;
import app.strategy.utils.dto.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calc")
public class StrategyController {

    private CalculationService calculationService;

    @Autowired
    public void setStrategyFactory(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @ApiOperation(value = "Расчет значений согласно стратегии", notes = "method: StrategyController.execute")
    @RequestMapping(value = "/byStrategy/{strategy}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Response> execute(@RequestBody Request requestTO, @PathVariable("strategy") EStrategyNames strategy) {

        Response response = calculationService.getCalculationByStrategy(requestTO, strategy);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }


}
