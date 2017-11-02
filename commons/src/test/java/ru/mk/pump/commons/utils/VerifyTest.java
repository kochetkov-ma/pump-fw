package ru.mk.pump.commons.utils;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.mk.pump.commons.exception.VerifyError;
import ru.mk.pump.commons.reporter.AllureRunner;
import ru.mk.pump.commons.reporter.ReporterAllure;
import ru.mk.pump.commons.reporter.ReporterAllure.Type;

@RunWith(AllureRunner.class)
public class VerifyTest {

    private Verify verify;

    @Before
    public void before() {
        this.verify = new Verify(new ReporterAllure(new DesktopScreenshoter(), Type.ALL));
    }

    @Test
    public void checkFalse() {
        assertThatCode(() -> verify.checkFalse("Описание проверки", false)).doesNotThrowAnyException();
        assertThatThrownBy(() -> verify.checkFalse("Описание проверки", true))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание проверки")
            .hasMessageContaining("Description : Ожидается 'false' актуальное значение 'true'");
    }

    @Test
    public void checkTrue() {
        assertThatCode(() -> verify.checkTrue("Описание проверки", true)).doesNotThrowAnyException();
        assertThatThrownBy(() -> verify.checkTrue("Описание проверки", false))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание проверки")
            .hasMessageContaining("Description : Ожидается 'true' актуальное значение 'false'");
    }

    @Test
    public void equalsObject() {
        final Object object = new Object();
        assertThatCode(() -> verify.equals("Описание проверки", object, object)).doesNotThrowAnyException();
        assertThatThrownBy(() -> verify.equals("Описание проверки", object, "тест"))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание проверки")
            .hasMessageContaining("Description : Объекты совпадают. Тип объектов : Object String");
    }

    @Test
    public void equalsString() {
        assertThatCode(() -> verify.equals("Описание проверки", "тест", "тест")).doesNotThrowAnyException();
        assertThatThrownBy(() -> verify.equals("Описание проверки", "тест1", "тест"))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание проверки")
            .hasMessageContaining("Description : Ожидаемая строка 'тест1' совпадает с актуальной строкой 'тест'");
    }

    @Test
    public void equalsContains() {
        assertThatCode(() -> verify.contains("Описание проверки", Strings.normalize("тест   \n те ст "), Strings.normalize("тест тест тест гггг")))
            .doesNotThrowAnyException();
        assertThatCode(() -> verify.contains("Описание проверки", " тест  ", "   тест1 ")).doesNotThrowAnyException();
        assertThatThrownBy(() -> verify.contains("Описание проверки", "тест2", "тест1"))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание проверки")
            .hasMessageContaining("Description : Ожидаемая строка 'тест2' содержится в актуальной строке 'тест1'");
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

        List<Object> list1 = Lists.newArrayList(object, "строка", 1L);
        List<Object> list2 = Lists.newArrayList(object, "строка", 1L);

        List<Object> list3 = Lists.newArrayList(1L, object, "строка");
        List<Object> list4 = Lists.newArrayList(1L, object);
        List<Object> list5 = Lists.newArrayList(object, 1L, "строка");
        List<Object> list6 = Lists.newArrayList(object, "стро", 1L);

        List<String> stringList1 = Lists.newArrayList("строка - 1", "строка - 2", "строка - 3");
        List<String> stringList2 = Lists.newArrayList("строка - 3", "строка - 1", "строка - 2");
        List<String> stringList3 = Lists.newArrayList("строка - 1", "строка - 2");
        List<String> stringList4 = Lists.newArrayList("строка - 2", "строка - 3");
        List<String> stringList5 = Lists.newArrayList("строка - 2", "строка - 4");
        List<String> stringList6 = Lists.newArrayList("строка - 2", "   строка   -   1  ");
        List<String> stringList7 = Lists.newArrayList("строка - 2", "стрrrока - 7");

        verify.listStrictContains("Описание", stringList3, stringList1, Collators.equals(), null);

        /*
        verify.listEquals("Описание", expect1, actual1, Collators.equals(), null);
        assertThatThrownBy(() -> verify.listEquals("Описание", expect10, actual1, Collators.equals(), null))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание")
            .hasMessageContaining(
                "Description : Ожидаемый список '[1, java.lang.Object@78aab498, строка]' равен актуальному списку '[java.lang.Object@78aab498, строка, 1]'")
            .hasMessageContaining("Актуальное значение 'java.lang.Object@78aab498' типа 'Object' равно ожидаемому значению '1' типа 'Long' index 0");

        assertThatThrownBy(() -> verify.listEquals("Описание", expect11, expect10, Collators.equals(), null))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание")
            .hasMessageContaining(
                "Description : Размер ожидаемого списка '[1, java.lang.Object@78aab498]' равен размеру актуального списка '[1, java.lang.Object@78aab498, строка]'");

        verify.listEquals("Описание", expect10, actual1, Collators.equals(), comparator);


        verify.listContains("Описание", expect6, actual2, Collators.normalizeContains());
        */

    }

}
