package app.strategy.defaultStrategy;

import app.strategy.utils.EStrategyNames;
import app.strategy.utils.IStrategy;
import app.strategy.utils.dto.Request;
import app.strategy.utils.dto.Response;
import org.springframework.stereotype.Component;

@Component
public class StrategyPlus implements IStrategy {

    @Override
    public Response calculate(Request request) {
        return Response.builder()
                .result(request.getOp1() + request.getOp2())
                .build();
    }

    @Override
    public EStrategyNames getName() {
        return EStrategyNames.PLUS;
    }
}
