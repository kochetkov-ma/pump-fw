package ru.mk.pump.commons.reporter;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.reporter.ReporterAllure.Type;
import ru.mk.pump.commons.utils.DesktopScreenshoter;
import ru.mk.pump.commons.utils.Strings;

@Slf4j
class ReporterTest {

    private Reporter reporter;

    @BeforeEach
    void before() {
        this.reporter = new ReporterAllure(new DesktopScreenshoter(), Type.ALL);
    }

    @Test
    void testReporter() {
        reporter.testStart("Тестовый тест", "Сбор данных");
        reporter.info("Новый тест", "Описание");
        stepToTest();
        reporter.blockStart("Главный блок", "Описание блока");
        reporter.blockStart("Зависимый блок", "Описание зависимого блока");
        reporter.info("Шаг1", Strings.empty());
        reporter.info("Шаг2", Strings.empty());
        reporter.blockStart("Зависимый блок", "Описание зависимого блока");
        reporter.info("Шаг3", Strings.empty());
        reporter.info("Шаг4", Strings.empty());
        reporter.blockStop();
        reporter.info("Шаг5", Strings.empty());
        reporter.blockStop();
        reporter.info("Шаг6", Strings.empty());
        reporter.blockStop();
        reporter.info("Шаг7", Strings.empty());
        reporter.blockStop();
        reporter.info("Шаг8", Strings.empty());
        reporter.testStop();
    }

    @Step("Annotation test step")
    private void stepToTest() {
        reporter.info("Заголовок шага",
            "Длинный текст текс текст текс текст текс \n текст текс текст текс \n текст текс текст текс");
        reporter.info("Заголовок шага c null описанием", null);
        reporter.info("Заголовок шага", "Описание шага", reporter.attachments().screen("Скриншот экрана"));
        reporter.pass("Проверка заголовок", "Описание проверки");
        try {
            reporter.fail("Проверка заголовок", "Описание проверки", reporter.attachments().screen("Скриншот экрана"), new AssertionError());
        } catch (Throwable ignore) {
        }
        try {
            reporter.error("Заголовок ошибки", "Описание ошибки", reporter.attachments().screen("Скриншот экрана"), new PumpException("Тестовое исключение"));
        } catch (Throwable ignore) {
        }
    }
}
