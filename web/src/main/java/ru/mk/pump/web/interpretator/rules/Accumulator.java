package ru.mk.pump.web.interpretator.rules;

import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Walker;

@SuppressWarnings("WeakerAccess")
@Slf4j
@ToString
public final class Accumulator {

    @Getter
    private final Rule rule;

    private final Walker walker;

    private final StringBuilder result = new StringBuilder();

    public Accumulator(Rule rule, Walker walker) {
        this.rule = rule;
        this.walker = walker;
    }

    public boolean next() {
        if (!rule.parseEnd(walker.getLeft(), walker.getRight()) || result.length() < rule.minSize()) {
            result.append(walker.next());
            return true;
        }
        if (result.length() == 0){
            walker.next();
        }
        return false;
    }

    public String getResult() {
        return result.toString();
    }

    public static Optional<Accumulator> newAccumulator(Walker walker, Set<Rule> ruleSet) {
        Optional<Rule> ruleOptional = ruleSet.stream().filter(r -> r.parseStart(walker.getLeft(), walker.getRight())).findFirst();
        return ruleOptional.map(rule1 -> new Accumulator(rule1, walker));
    }
}