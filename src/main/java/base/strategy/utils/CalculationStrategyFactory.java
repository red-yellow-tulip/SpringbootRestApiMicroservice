package base.strategy.utils;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class CalculationStrategyFactory {

    private Map<EStrategyNames, IStrategy> strategies;

    public CalculationStrategyFactory(Set<IStrategy> strategySet) {
        strategies = new EnumMap<>(EStrategyNames.class);
        strategySet.forEach(
                strategy ->strategies.put( strategy.getName(), strategy)
        );
    }
    public IStrategy findStrategy(EStrategyNames name) {
        return strategies.get(name);
    }


}
