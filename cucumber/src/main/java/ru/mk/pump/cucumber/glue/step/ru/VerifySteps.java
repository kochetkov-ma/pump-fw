package ru.mk.pump.cucumber.glue.step.ru;

import static java.lang.String.format;

import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Collators;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.cucumber.glue.AbstractSteps;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class VerifySteps extends AbstractSteps {

    private static final String MSG = "Cucumber шаг проверки значений";

    private final Verifier verifier;

    @Inject
    public VerifySteps(Verifier verifier) {
        this.verifier = verifier;
    }

    @Given("^проверено, что ожидаемый объект (.+?)( не|) равен актуальному (.+?)$")
    public void equalsObject(Object expect, boolean not, Object actual) {
        if (not) {
            verifier.notEquals(MSG, expect, actual);
        } else {
            verifier.equals(MSG, expect, actual);
        }
    }

    @Given("^проверено, что ожидаемая строка '(.+?)'( не|) (равна|содержится в) актаульной '(.+?)'( используя нормализацию|)$")
    public void equalsString(String expect, boolean not, String type, String actual, boolean normalize) {
        if (normalize) {
            expect = Str.normalize(expect);
            actual = Str.normalize(actual);
        }
        switch (type) {
            case "равна":
                if (not) {
                    verifier.notEquals(MSG, expect, actual);
                } else {
                    verifier.equals(MSG, expect, actual);
                }
                break;
            case "содержится в":
                if (not) {
                    verifier.notContains(MSG, expect, actual);
                } else {
                    verifier.contains(MSG, expect, actual);
                }
                break;
            default:
                operationTypeError(type);
        }
    }

    @Given("^проверено, что ожидаемое число (.+?)( не|) (равно|больше|меньше|больше или равно|меньше или равно) актуальн(?:ому|ого) (.+?)$")
    public void equalsNumber(long expect, boolean not, String type, long actual) {
        switch (type) {
            case "равно":
                if (not) {
                    verifier.notEquals(MSG, expect, actual);
                } else {
                    verifier.equals(MSG, expect, actual);
                }
                break;
            case "больше":
                if (not) {
                    verifier.checkFalse(MSG + type, expect > actual);
                } else {
                    verifier.checkTrue(MSG + type, expect > actual);
                }
                break;
            case "меньше":
                if (not) {
                    verifier.checkFalse(MSG + type, expect < actual);
                } else {
                    verifier.checkTrue(MSG + type, expect < actual);
                }
                break;
            case "больше или равно":
                if (not) {
                    verifier.checkFalse(MSG + type, expect >= actual);
                } else {
                    verifier.checkTrue(MSG + type, expect >= actual);
                }
                break;
            case "меньше или равно":
                if (not) {
                    verifier.checkFalse(MSG + type, expect <= actual);
                } else {
                    verifier.checkTrue(MSG + type, expect <= actual);
                }
                break;
            default:
                operationTypeError(type);
        }
    }

    @Given("^проверено, что актуальное значение (.+?) (истина|ложь|null|не null)$")
    public void isTrue(Object object, String type) {
        switch (type) {
            case "истина":
                if (object instanceof Boolean) {
                    verifier.checkTrue(MSG, (boolean) object);
                } else {
                    verifier.notNull(MSG, object);
                }
                break;
            case "ложь":
                if (object instanceof Boolean) {
                    verifier.checkFalse(MSG, (boolean) object);
                } else {
                    verifier.checkNull(MSG, object);
                }
                break;
            case "null":
                verifier.checkNull(MSG, object);
                break;
            case "не null":
                verifier.notNull(MSG, object);
                break;
            default:
                operationTypeError(type);
        }
    }

    @Given("^проверено, что ожидаемый список значений '(.+?)' (равен списку|содержится в списке) '(.+?)'( с учетом позиции|)$")
    public void equalsList(List<String> expect, String type, List<String> actual, boolean strict) {
        switch (type) {
            case "равен списку":
                if (strict) {
                    verifier.listEquals(MSG, expect, actual, Collators.equals(), null);
                } else {
                    expect.sort(Comparator.naturalOrder());
                    verifier.listEquals(MSG, expect, actual, Collators.equals(), Comparator.naturalOrder());
                }
                break;
            case "содержится в списке":
                if (strict) {
                    verifier.listStrictContains(MSG, expect, actual, Collators.liteNormalizeContains(), null);
                } else {
                    verifier.listContains(MSG, expect, actual, Collators.liteNormalizeContains());
                }
                break;
            default:
                operationTypeError(type);
        }

    }
}