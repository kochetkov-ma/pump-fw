package ru.mk.pump.commons.utils;

import static org.assertj.core.api.Assertions.*;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.exception.VerifyError;
import ru.mk.pump.commons.reporter.ReporterAllure;
import ru.mk.pump.commons.reporter.ReporterAllure.Type;

public class VerifyTest {

    private Verifier verify;

    @BeforeAll
    public static void setUp() {
        if (System.getProperty("allure.results.directory") == null) {
            System.setProperty("allure.results.directory", "out/allure-result");
        }
    }

    @BeforeEach
    public void before() {
        this.verify = new Verifier(new ReporterAllure(new DesktopScreenshoter(), Type.ALL));
    }

    @Test
    public void checkFalse() {
        assertThatCode(() -> verify.checkFalse("Описание проверки", false)).doesNotThrowAnyException();
        assertThatThrownBy(() -> verify.checkFalse("Описание проверки", true))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание проверки")
            .hasMessageContaining("Ожидается 'false' актуальное значение 'true'");
    }

    @Test
    public void checkTrue() {
        assertThatCode(() -> verify.checkTrue("Описание проверки", true)).doesNotThrowAnyException();
        assertThatThrownBy(() -> verify.checkTrue("Описание проверки", false))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание проверки")
            .hasMessageContaining("Ожидается 'true' актуальное значение 'false'");
    }

    @Test
    public void equalsObject() {
        final Object object = new Object();
        assertThatCode(() -> verify.equals("Описание проверки", object, object)).doesNotThrowAnyException();
        assertThatThrownBy(() -> verify.equals("Описание проверки", object, "тест"))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание проверки")
            .hasMessageContaining("Объекты совпадают. Тип объектов : Object String");
    }

    @Test
    public void equalsString() {
        assertThatCode(() -> verify.equals("Описание проверки", "тест", "тест")).doesNotThrowAnyException();
        assertThatThrownBy(() -> verify.equals("Описание проверки", "тест1", "тест"))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание проверки")
            .hasMessageContaining("Ожидаемая строка 'тест1' совпадает с актуальной строкой 'тест'");
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
            .hasMessageContaining("Ожидаемая строка 'тест2' содержится в актуальной строке 'тест1'");
    }

    @Test
    public void equalsList() {
        Object object = new Object() {
            @Override
            public String toString() {
                return "OBJECT";
            }
        };
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
        List<String> stringList6 = Lists.newArrayList("строка - 2", "ка - 1");
        List<String> stringList7 = Lists.newArrayList("строка - 2", "стрrrока - 7");
        List<String> stringList8 = Lists.newArrayList("    строка - 3    ");
        List<String> stringList9 = Lists.newArrayList("строка - 4");
        List<String> stringList10 = Lists.newArrayList("строка - 3", "строка - 4");

        assertThatCode(() -> verify.listEquals("Описание", list1, list2, Collators.equals(), null)).doesNotThrowAnyException();
        assertThatCode(() -> verify.listEquals("Описание", list1, list3, Collators.equals(), comparator)).doesNotThrowAnyException();

        assertThatThrownBy(() -> verify.listEquals("Описание", list3, list1, Collators.equals(), null))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание")
            .hasMessageContaining(
                "Ожидаемый список '[1, OBJECT, строка]' равен актуальному списку '[OBJECT, строка, 1]'")
            .hasMessageContaining("Актуальное значение 'OBJECT' типа '' равно ожидаемому значению '1' типа 'Long' index 0");

        assertThatThrownBy(() -> verify.listEquals("Описание", list4, list3, Collators.equals(), null))
            .hasNoCause()
            .isInstanceOf(VerifyError.class)
            .hasMessageContaining("Pump verify fail. Описание")
            .hasMessageContaining(
                "Размер ожидаемого списка '[1, OBJECT]' равен размеру актуального списка '[1, OBJECT, строка]'");

        assertThatCode(() -> verify.listStrictContains("Описание", stringList3, stringList1, Collators.equals(), null)).doesNotThrowAnyException();
        assertThatCode(() -> verify.listStrictContains("Описание", stringList4, stringList1, Collators.equals(), null)).doesNotThrowAnyException();

        assertThatThrownBy(() -> verify.listStrictContains("Описание", stringList1, stringList10, Collators.equals(), null));
        assertThatThrownBy(() -> verify.listStrictContains("Описание", stringList5, stringList1, Collators.equals(), null));
        assertThatThrownBy(() -> verify.listStrictContains("Описание", stringList9, stringList1, Collators.equals(), null));
        assertThatThrownBy(() -> verify.listStrictContains("Описание", stringList10, stringList1, Collators.equals(), null));

        assertThatCode(() -> verify.listContains("Описание", stringList2, stringList1, Collators.equals())).doesNotThrowAnyException();
        assertThatCode(() -> verify.listContains("Описание", stringList3, stringList1, Collators.equals())).doesNotThrowAnyException();
        assertThatCode(() -> verify.listContains("Описание", stringList4, stringList1, Collators.equals())).doesNotThrowAnyException();

        assertThatCode(() -> verify.listContains("Описание", stringList6, stringList1, Collators.contains())).doesNotThrowAnyException();
        assertThatCode(() -> verify.listContains("Описание", stringList8, stringList1, Collators.normalizeContains())).doesNotThrowAnyException();

        assertThatThrownBy(() -> verify.listContains("Описание", stringList7, stringList1, Collators.normalizeContains())).isInstanceOf(VerifyError.class);
        assertThatThrownBy(() -> verify.listContains("Описание", stringList1, stringList3, Collators.normalizeContains())).isInstanceOf(VerifyError.class);
    }
}
