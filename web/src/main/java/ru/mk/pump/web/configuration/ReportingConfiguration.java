package ru.mk.pump.web.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.config.Property;
import ru.mk.pump.commons.reporter.ReporterAllure.Type;
import ru.mk.pump.web.common.WebReporter;

/**
 * [RUS]
 * Конфигурация Репортера.
 * По-умолчанию конфигурация применяется, только для объекта {@link ru.mk.pump.commons.reporter.ReporterAllure} полученного из {@link WebReporter#getReporter()}
 * и только если это реализация ReporterAllure
 */
@SuppressWarnings("WeakerAccess")
@NoArgsConstructor
@Data
public class ReportingConfiguration {

    /**
     * [RUS]
     * Постинг сообщений, чей уровень больше или равен указанному
     */
    @Property(value = "post.level", defaultValue = "INFO")
    private Type postLevel;

    /**
     * [RUS]
     * Постинг сообщений в logback вывод, чей уровень больше или равен указанному
     */
    @Property(value = "post.logback.level", defaultValue = "ALL")
    private Type postLogbackLevel;

    /**
     * [RUS]
     * Добавление авто-скриншотов в сообщения, чей уровень больше или равен указанному.
     */
    @Property(value = "post.screen.level", defaultValue = "OFF")
    private Type postScreenLevel;
}