package base.strategy.defaultStrategy;

import base.strategy.utils.EStrategyNames;
import base.strategy.utils.IStrategy;
import base.strategy.utils.dto.Request;
import base.strategy.utils.dto.Response;
import org.springframework.stereotype.Component;

@Component
public class StrategyMinus implements IStrategy {

    @Override
    public Response calculate(Request request) {
        return Response.builder()
                .result(request.getOp1() - request.getOp2())
                .build();
    }

    @Override
    public EStrategyNames getName() {
        return EStrategyNames.MINUS;
    }
}
