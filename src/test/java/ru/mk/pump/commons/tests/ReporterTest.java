package ru.mk.pump.commons.tests;

import io.qameta.allure.Step;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.reporter.ReporterAllure;
import ru.mk.pump.commons.reporter.ReporterAllure.Type;
import ru.mk.pump.commons.utils.DesktopScreenshoter;

@RunWith(AllureRunner.class)
public class ReporterTest {

    private Reporter reporter;

    @Before
    public void before() {
        this.reporter = new ReporterAllure(new DesktopScreenshoter(), Type.ALL);
    }

    @Test
    public void testReporter() {
        stepToTest();
    }

    @Step("Annotation test step")
    private void stepToTest() {
        reporter.info("Заголовок шага", "Описание шага");
        reporter.info("Заголовок шага c null описанием", null);
        reporter.info("Заголовок шага", "Описание шага", reporter.attachments().screen("Скриншот экрана"));
        reporter.pass("Проверка заголовок", "Описание проверки");
        try {
            reporter.fail("Проверка заголовок", "Описание проверки", reporter.attachments().screen("Скриншот экрана"), new AssertionError());
        } catch (Throwable ignore) {
        }
        reporter.error("Заголовок ошибки", "Описание ошибки", reporter.attachments().screen("Скриншот экрана"), new PumpException("Тестовое исключение"));
    }
}
