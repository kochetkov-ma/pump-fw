package ru.mk.pump.commons.utils;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.exception.VerifyError;
import ru.mk.pump.commons.interfaces.Collator;
import ru.mk.pump.commons.reporter.Reporter;

@SuppressWarnings("ALL")
public class Verifier {

    private final Reporter reporter;

    private AssertionError lastError = null;

    private boolean postPassedScreen = false;

    private boolean postPassedCheck = false;

    private boolean failOnError = true;

    private boolean needTrim = true;

    public Verifier(Reporter reporter) {

        this.reporter = reporter;
    }

    public Verifier setPostPassedScreen(boolean postPassedScreen) {
        this.postPassedScreen = postPassedScreen;
        return this;
    }

    public Verifier setPostPassedCheck(boolean postPassedCheck) {
        this.postPassedCheck = postPassedCheck;
        return this;
    }

    public Verifier setFailOnError(boolean failOnError) {
        failOnError = failOnError;
        return this;
    }

    public Verifier setNeedTrim(boolean needTrim) {
        this.needTrim = needTrim;
        return this;
    }

    public Verifier checkErrors() {
        if (hasErrors()) {
            throw lastError;
        }
        return this;
    }

    public boolean hasErrors() {
        return Objects.nonNull(lastError);
    }

    public void notContains(String description, String expected, String actual) {
        if (needTrim) {
            expected = Strings.trim(expected);
            actual = Strings.trim(actual);
        }

        final String message = format("Ожидаемая строка '%s' НЕ содержится в актуальной строке '%s'", expected, actual);
        check(!StringUtils.contains(actual, expected), description, message);
    }

    public void contains(String description, String expected, String actual) {
        if (needTrim) {
            expected = Strings.trim(expected);
            actual = Strings.trim(actual);
        }

        final String message = format("Ожидаемая строка '%s' содержится в актуальной строке '%s'", expected, actual);
        check(StringUtils.contains(actual, expected), description, message);
    }

    public void match(String description, String regExp, String actual) {
        final String message = format("Актуальная строка '%s' соответсвует регулярному выражению '%s'", actual, regExp);
        check(Strings.match(regExp, actual), description, message);
    }

    public void equals(String description, String expected, String actual) {
        if (needTrim) {
            expected = Strings.trim(expected);
            actual = Strings.trim(actual);
        }

        final String message = format("Ожидаемая строка '%s' совпадает с актуальной строкой '%s'", expected, actual);
        check(StringUtils.equals(expected, actual), description, message);
    }

    public void notEquals(String description, String expected, String actual) {
        if (needTrim) {
            expected = Strings.trim(expected);
            actual = Strings.trim(actual);
        }

        final String message = format("Ожидаемая строка '%s' НЕ совпадает с актуальной строкой '%s'", expected, actual);
        check(!StringUtils.equals(expected, actual), description, message);
    }

    public void equals(String description, @NonNull Object expected, @NonNull Object actual) {
        String message = "";
        final String extraMessage =
            "Ожидаемое значение : " + expected.toString() + System.lineSeparator() + "Актуальное значение : " + actual.toString() + System.lineSeparator()
                + message;
        message = "Объекты совпадают. Тип объектов : " + expected.getClass().getSimpleName() + " " + actual.getClass().getSimpleName();
        check(Objects.equals(expected, actual), description, message);
    }

    public void notEquals(String description, @NonNull Object expected, @NonNull Object actual) {
        String message = "";
        final String extraMessage =
            "Ожидаемое значение : " + expected.toString() + System.lineSeparator() + "Актуальное значение : " + actual.toString() + System.lineSeparator()
                + message;
        message = "Объекты НЕ совпадают. Тип объектов : " + expected.getClass().getSimpleName() + " " + actual.getClass().getSimpleName();
        check(!Objects.equals(expected, actual), description, extraMessage);
    }

    public void checkFalse(String description, boolean isFalse) {
        final String message = format("Ожидается '%b' актуальное значение '%b'", false, isFalse);
        check(!isFalse, description, message);
    }

    public void checkTrue(String description, boolean isTrue) {
        final String message = format("Ожидается '%b' актуальное значение '%b'", true, isTrue);
        check(isTrue, description, message);
    }

    public void noExceptions(String description, @NonNull Runnable runnable) {
        final String message = "Ожидается выполнение без Исключений";
        try {
            runnable.run();
        } catch (Exception ex) {
            check(false, description, ex, description, message, ex.toString());
        }
        check(true, description, message);
    }

    public void checkNull(String description, Object isNull) {
        final String message = format("Ожидается 'null' актуальное значение '%s'", Objects.isNull(isNull) ? "null" : "НЕ null");
        check(Objects.isNull(isNull), description, message);
    }

    public void notNull(String description, Object isNotNull) {
        final String message = format("Ожидается 'НЕ null' актуальное значение '%s'", Objects.isNull(isNotNull) ? "null" : "НЕ null");
        check(!Objects.isNull(isNotNull), description, message);
    }

    /**
     * @param comporatorToCompareItems {@link ru.mk.pump.commons.utils.Collators}
     */
    public <T> void listEquals(String description, @NonNull List<T> expected, @NonNull List<T> actual, @NonNull Collator<T> comporatorToCompareItems,
        @Nullable Comparator<T> tComparatorToSortActual) {
        actual = sortIfNeed(actual, tComparatorToSortActual);
        checkSize(description, null, expected, actual, false);
        final String message = format("Ожидаемый список '%s' равен актуальному списку '%s'", expected, actual);
        for (int index = 0; index < expected.size(); index++) {
            final T actualItem = actual.get(index);
            final T expectedItem = expected.get(index);
            check(comporatorToCompareItems.collate(expectedItem, actualItem), description, message,
                Strings.space(comporatorToCompareItems.getMessage(), "index", String.valueOf(index)));
        }
    }

    public <T> void listStrictContains(String description, @NonNull List<T> expected, @NonNull List<T> actual, @NonNull Collator<T> comporatorToCompareItems,
        @Nullable Comparator<T> tComparatorToSortActual) {
        actual = sortIfNeed(actual, tComparatorToSortActual);

        checkSize(description, null, expected, actual, true);

        int actualStartIndex = -1;
        for (int index = 0; index < actual.size(); index++) {
            if (comporatorToCompareItems.collate(expected.get(0), actual.get(index))) {
                actualStartIndex = index;
            }
        }
        final String message = format("Ожидаемый список '%s' строго (с учетом позиции элементов) содержится в списке '%s'", expected, actual);

        check(actualStartIndex != -1, description, message,
            "Найдено вхождение первого элемента ожидаемого списка в актуальном списке");

        final List<T> actualListToCompare = actual.subList(actualStartIndex, actual.size());

        checkSize(Strings.space(description,
            ". Сравнение части актуального списка начиная с позиции совпадения " + actualStartIndex + " с первым элементов ожидаемого списка"), message,
            expected, actualListToCompare, true);

        for (int index = 0; index < expected.size(); index++) {
            final T actualItem = actualListToCompare.get(index);
            final T expectedItem = expected.get(index);
            check(comporatorToCompareItems.collate(expectedItem, actualItem), description, message,
                Strings.space(comporatorToCompareItems.getMessage(), "index", String.valueOf(index)));
        }

    }

    public <T> void listContains(String description, @NonNull List<T> expected, @NonNull List<T> actual, @NonNull Collator<T> comporatorToCompareItems) {
        checkSize(description, null, expected, actual, true);
        final String message = format("Ожидаемый список '%s' не строго (без учета позиции элементов) содержится в списке '%s'", expected, actual);
        for (int index = 0; index < expected.size(); index++) {
            final T expectedItem = expected.get(index);
            check(actual.stream().anyMatch(actualItem -> comporatorToCompareItems.collate(expectedItem, actualItem)), description, message,
                Strings
                    .space(comporatorToCompareItems.info(expectedItem), "содержится в списке (представлен без обработки элементов)",
                        StringConstants.AP + actual.toString() + StringConstants.AP, ", index",
                        String.valueOf(index)));
        }
    }

    private <T> List<T> sortIfNeed(@NonNull List<T> listToSort, @Nullable Comparator<T> tComparator) {
        listToSort = new ArrayList<>(listToSort);
        if (tComparator != null) {
            Collections.sort(listToSort, tComparator);
        }
        return listToSort;
    }

    private <T> void checkSize(String description, @Nullable String additionalMessage, @NonNull List<T> expected, @NonNull List<T> actual,
        boolean isActualMoreExpected) {
        if (isActualMoreExpected) {
            final String message = format("Размер ожидаемого списка '%s' равен или меньше размера актуального списка '%s'", expected, actual);
            check(actual.size() >= expected.size(), description, additionalMessage, message);
        } else {
            final String message = format("Размер ожидаемого списка '%s' равен размеру актуального списка '%s'", expected, actual);
            check(actual.size() == expected.size(), description, additionalMessage, message);
        }
    }

    private void check(boolean isTrue, String checkDescription, Throwable throwable, String... additionalDescription) {
        final String extraMessage = Strings.concat(StringConstants.LINE, additionalDescription);
        final String message;
        if (checkDescription == null || checkDescription.isEmpty()) {
            message = extraMessage;
        } else {
            message = checkDescription;
        }
        if (isTrue) {
            if (postPassedCheck) {
                if (postPassedScreen) {
                    reporter.pass(checkDescription, extraMessage, reporter.attachments().screen("Screen on pass"));
                } else {
                    reporter.pass(checkDescription, extraMessage);
                }
            }
            return;
        } else {
            try {
                lastError = new VerifyError(new PumpMessage(checkDescription, extraMessage), throwable);
                reporter.fail(checkDescription, extraMessage, reporter.attachments().screen("Screen on fail"), lastError);
            } catch (AssertionError assertionError) {
                if (failOnError) {
                    checkErrors();
                }
            }
        }
    }

    private void check(boolean isTrue, String checkDescription, String... additionalDescription) {
        check(isTrue, checkDescription, null, additionalDescription);
    }
}