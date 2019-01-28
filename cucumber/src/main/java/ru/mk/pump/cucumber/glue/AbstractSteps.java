package ru.mk.pump.cucumber.glue;

import static java.lang.String.format;

import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.cucumber.CucumberCore;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractSteps {

    private final CucumberCore core;

    public AbstractSteps() {
        core = CucumberCore.instance();
    }

    public CucumberCore core() {
        return core;
    }

    public Object transform(String pumpkinExpression) {
        return core.getWebController().execute(pumpkinExpression);
    }

    protected void operationTypeError(String operationType) {
        throw new UnsupportedOperationException(format("Операция '%s' не поддерживается", Strings.toString(operationType)));
    }
}