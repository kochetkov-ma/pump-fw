package ru.mk.pump.commons.utils;

import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.exception.VerifyError;
import ru.mk.pump.commons.interfaces.Collator;
import ru.mk.pump.commons.reporter.Reporter;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Objects;

import static java.lang.String.format;

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

    public void notContains(String title, String expected, String actual, String... desc) {
        if (needTrim) {
            expected = Strings.trim(expected);
            actual = Strings.trim(actual);
        }
        final String message = format("Ожидаемая строка '%s' НЕ содержится в актуальной строке '%s'", expected, actual);
        check(!StringUtils.contains(actual, expected), title, ArrayUtils.addAll(desc, message));
    }

    public void contains(String title, String expected, String actual, String... desc) {
        if (needTrim) {
            expected = Strings.trim(expected);
            actual = Strings.trim(actual);
        }

        final String message = format("Ожидаемая строка '%s' содержится в актуальной строке '%s'", expected, actual);
        check(StringUtils.contains(actual, expected), title, ArrayUtils.addAll(desc, message));
    }

    public void match(String title, String regExp, String actual, String... desc) {
        final String message = format("Актуальная строка '%s' соответсвует регулярному выражению '%s'", actual, regExp);
        check(Strings.match(regExp, actual), title, ArrayUtils.addAll(desc, message));
    }

    public void equals(String title, String expected, String actual, String... desc) {
        if (needTrim) {
            expected = Strings.trim(expected);
            actual = Strings.trim(actual);
        }

        final String message = format("Ожидаемая строка '%s' совпадает с актуальной строкой '%s'", expected, actual);
        check(StringUtils.equals(expected, actual), title, ArrayUtils.addAll(desc, message));
    }

    public void notEquals(String title, String expected, String actual, String... desc) {
        if (needTrim) {
            expected = Strings.trim(expected);
            actual = Strings.trim(actual);
        }

        final String message = format("Ожидаемая строка '%s' НЕ совпадает с актуальной строкой '%s'", expected, actual);
        check(!StringUtils.equals(expected, actual), title, ArrayUtils.addAll(desc, message));
    }

    public void equals(String title, @NonNull Object expected, @NonNull Object actual, String... desc) {
        final String extraMessage =
                "Ожидаемое значение : " + Strings.toString(expected) + System.lineSeparator() + "Актуальное значение : " + Strings.toString(actual);
        String message = "Объекты совпадают. Тип объектов : " + expected.getClass().getSimpleName() + " " + actual.getClass().getSimpleName();
        message = Strings.line(extraMessage, message);
        check(Objects.equals(expected, actual), title, ArrayUtils.addAll(desc, message));
    }

    public void notEquals(String title, @NonNull Object expected, @NonNull Object actual, String... desc) {
        final String extraMessage =
                "Ожидаемое значение : " + Strings.toString(expected) + System.lineSeparator() + "Актуальное значение : " + Strings.toString(actual);
        String message = "Объекты НЕ совпадают. Тип объектов : " + expected.getClass().getSimpleName() + " " + actual.getClass().getSimpleName();
        message = Strings.line(extraMessage, message);
        check(!Objects.equals(expected, actual), title, ArrayUtils.addAll(desc, message));
    }

    public void checkFalse(String title, boolean isFalse, String... desc) {
        final String message = format("Ожидается '%b' актуальное значение '%b'", false, isFalse);
        check(!isFalse, title, ArrayUtils.addAll(desc, message));
    }

    public void checkTrue(String title, boolean isTrue, String... desc) {
        final String message = format("Ожидается '%b' актуальное значение '%b'", true, isTrue);
        check(isTrue, title, ArrayUtils.addAll(desc, message));
    }

    public void noExceptions(String title, @NonNull Runnable runnable) {
        final String message = "Ожидается выполнение без Исключений";
        try {
            runnable.run();
        } catch (Exception ex) {
            check(false, title, ex, title, message, ex.toString());
        }
        check(true, title, message);
    }

    public void checkNull(String title, Object isNull, String... desc) {
        final String message = format("Ожидается 'null' актуальное значение '%s'", Objects.isNull(isNull) ? "null" : "НЕ null");
        check(Objects.isNull(isNull), title, ArrayUtils.addAll(desc, message));
    }

    public void notNull(String title, Object isNotNull, String... desc) {
        final String message = format("Ожидается 'НЕ null' актуальное значение '%s'", Objects.isNull(isNotNull) ? "null" : "НЕ null");
        check(!Objects.isNull(isNotNull), title, ArrayUtils.addAll(desc, message));
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