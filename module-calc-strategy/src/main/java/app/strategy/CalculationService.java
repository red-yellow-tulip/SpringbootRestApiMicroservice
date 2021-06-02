package app.strategy;

import app.strategy.utils.CalculationStrategyFactory;
import app.strategy.utils.EStrategyNames;
import app.strategy.utils.dto.Request;
import app.strategy.utils.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    private CalculationStrategyFactory strategyFactory;

    @Autowired
    public void setStrategyFactory(CalculationStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public Response getCalculationByStrategy(Request request, EStrategyNames strategy) {
            return strategyFactory.findStrategy(strategy).calculate(request);

    }

}
