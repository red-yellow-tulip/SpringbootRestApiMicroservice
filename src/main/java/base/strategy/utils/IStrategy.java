package base.strategy.utils;

import base.strategy.utils.dto.Request;
import base.strategy.utils.dto.Response;

public interface IStrategy {

    Response calculate(Request request);

    EStrategyNames getName();
}
