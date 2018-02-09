package ru.mk.pump.web.interpretator.rules;

import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Walker;

@Slf4j
final class PumpkinParser {

    private final Set<Rule> rules;

    @Getter
    private final String expression;

    @Getter
    private Walker walker;

    @Getter
    private Accumulator accumulator;

    PumpkinParser(String expression, Set<Rule> rules) {
        this.expression = expression;
        this.walker = new Walker(expression);
        this.rules = rules;
    }

    Optional<Accumulator> parseNext() {
        Optional<Accumulator> accumulatorOptional = Accumulator.newAccumulator(walker, rules);
        if (accumulatorOptional.isPresent()) {
            accumulator = accumulatorOptional.get();
            while (true) {
                if (!(accumulator.next())) {
                    break;
                }
            }
            log.debug("Parsed success '{}' , '{}'", accumulator.getResult(), accumulator.getRule().getClass().getSimpleName());
        } else {
            log.debug("Non parsed '{}'", walker.getRight());
            if (walker.hasNext()) {
                walker.next();
            } else {
                log.debug("Source is empty '{}'", walker);
            }
        }
        return accumulatorOptional;
    }

    boolean isCompleted() {
        return !walker.hasNext();
    }
}
