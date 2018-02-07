package ru.mk.pump.web.interpretator.rules;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Groovy;
import ru.mk.pump.commons.utils.Walker;

@Slf4j
final class PumpkinParser {

    public static void main(String[] args) {

        Map<String, Object> vars = ImmutableMap.of("var1", 1);

        Set<Rule> rules = ImmutableSet
            .of(new EmptyArgumentRule(), new TestVarArgument(vars), new GroovyArgumentRule(Groovy.of()), new StringArgumentRule(),
                new IndexRule(), new TitleRule());

        String exp = "methodOne().fieldOne[1].methodTwo(arg1,arg2)";
        //String exp = "methodOne().fieldOne[1].methodTwo(${var1},$groovy{new Date()})";
        //String exp = "$groovy{new Date()}";
        //String exp = "${var1}";

        PumpkinParser pumpkin = new PumpkinParser(exp, rules);
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
        pumpkin.parseNext();
    }

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
            log.info("Parsed success '{}' , '{}'", accumulator.getResult(), accumulator.getRule().getClass().getSimpleName());
        } else {
            log.info("Non parsed '{}'", walker.getRight());
            if (walker.hasNext()) {
                walker.next();
            } else {
                log.info("Source is empty '{}'", walker);
            }
        }
        return accumulatorOptional;
    }

    boolean isCompleted() {
        return !walker.hasNext();
    }
}
