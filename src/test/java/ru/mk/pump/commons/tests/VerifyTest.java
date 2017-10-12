package ru.mk.pump.commons.tests;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.mk.pump.commons.reporter.ReporterAllure;
import ru.mk.pump.commons.reporter.ReporterAllure.Type;
import ru.mk.pump.commons.utils.Collators;
import ru.mk.pump.commons.utils.DesktopScreenshoter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.Verify;

@RunWith(AllureRunner.class)
public class VerifyTest {

    private Verify verify;

    @Before
    public void before() {
        this.verify = new Verify(new ReporterAllure(new DesktopScreenshoter(), Type.ALL));
    }

    @Test
    public void checkFalse() {
        verify.checkFalse("Описание проверки", false);
        verify.checkFalse("Описание проверки", true);
    }

    @Test
    public void checkTrue() {
        verify.checkTrue("Описание проверки", true);
        verify.checkTrue("Описание проверки", false);
    }

    @Test
    public void equalsObject() {
        final Object object = new Object();
        verify.equals("Описание проверки", object, object);
        verify.equals("Описание проверки", object, "тест");
    }

    @Test
    public void equalsString() {
        verify.equals("Описание проверки", "тест", "тест");
        verify.equals("Описание проверки", "тест1", "тест");
    }

    @Test
    public void equalsContains() {
        verify.contains("Описание проверки", Strings.normalize("тест   \n те ст "), Strings.normalize("тест тест тест гггг"));
        verify.contains("Описание проверки", " тест  ", "   тест1 ");
        verify.contains("Описание проверки", "тест2", "тест1");
    }

    @Test
    public void equalsList() {
        Object object = new Object();
        Comparator<Object> comparator = Comparator.nullsLast((o1, o2) -> {
            if (o2 instanceof String) {
                return -2;
            }
            if (o2 instanceof Long) {
                return -1;
            }
            if (o2 != null) {
                if (o1 != null) {
                    return o1.hashCode() - o2.hashCode();
                } else {
                    return 1;
                }
            }
            return 0;
        });

        List<Object> actual1 = Lists.newArrayList(object, "строка", 1L);
        List<Object> expect1 = Lists.newArrayList(object, "строка", 1L);
        List<Object> expect10 = Lists.newArrayList(1L, object, "строка");
        List<Object> expect11 = Lists.newArrayList(1L, object);
        List<Object> expect12 = Lists.newArrayList(object, 1L, "строка");

        List<String> actual2 = Lists.newArrayList("строка - 1", "строка - 2", "строка - 3");
        List<String> expect2 = Lists.newArrayList("строка - 3", "строка - 1", "строка - 2");
        List<String> expect3 = Lists.newArrayList("строка - 1", "строка - 2");
        List<String> expect4 = Lists.newArrayList("строка - 1", "строка - 1");
        List<String> expect5 = Lists.newArrayList("строка - 2", "   строка   -   1  ");
        List<String> expect6 = Lists.newArrayList("строка - 2", "стрrrока - 7");

        //verify.listEquals("Описание", expect10, actual1, Collators.equals(), comparator);
        //verify.listStrictContains("Описание", expect12, actual1, Collators.equals(), comparator);
        verify.listContains("Описание", expect6, actual2, Collators.normalizeContains());
    }

}
