package ru.mk.pump.web.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.config.Property;
import ru.mk.pump.web.common.WebReporter;

/**
 * [RUS] Конфигурация Проверщика.
 * По-умолчанию конфигурация применяется, только для объекта {@link ru.mk.pump.commons.utils.Verifier} полученного из {@link WebReporter#getVerifier()}
 */
@SuppressWarnings("WeakerAccess")
@Data
@NoArgsConstructor
public class VerifyConfiguration {

    /**
     * [RUS]
     * Постинг сообщений с удачными проверками. Неудачные проверки всегда постятся
     */
    @Property(value = "post.passed.check", defaultValue = "false")
    private boolean postPassedCheck;

    /**
     * [RUS]
     * Если <code>{@link #isPostPassedCheck()} = true<code/>.
     * Добавление скриншотов к удачным проверкам.
     * Для неудачных проверок скриншоты всегда добавляются
     */
    @Property(value = "post.passed.screen", defaultValue = "false")
    private boolean screenOnSuccessCheck;
}
