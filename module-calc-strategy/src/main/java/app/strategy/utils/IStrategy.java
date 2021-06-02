package app.strategy.utils;

import app.strategy.utils.dto.Request;
import app.strategy.utils.dto.Response;

public interface IStrategy {

    Response calculate(Request request);

    EStrategyNames getName();
}
