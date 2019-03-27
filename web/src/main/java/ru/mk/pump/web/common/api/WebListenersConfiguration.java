package ru.mk.pump.web.common.api;

import lombok.NonNull;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.browsers.api.BrowserListener;
import ru.mk.pump.web.elements.api.listeners.ActionListener;
import ru.mk.pump.web.elements.api.listeners.StateListener;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.page.BasePage;
import ru.mk.pump.web.page.api.PageListener;
import ru.mk.pump.web.utils.TestVars;
import ru.mk.pump.web.utils.TestVars.TestVarListener;

import java.util.Set;

/**
 * [RUS] Настройка слушателей.
 * Релизуется необходимый слушатель, если нужно почистить дефолиные слушатели используется метод с преффиксом erase.
 * Если добавлять ничего не нужно то вернуть пустое множество.
 * Метод {@link ru.mk.pump.web.common.WebReporter#setListenersConfiguration(WebListenersConfiguration)} - для применения настроек.
 */
public interface WebListenersConfiguration {

    /**
     * [RUS] Нужно ли очистить от слушателей по-умолчанию. Для {@link #getBrowserListener}.
     */
    boolean eraseBrowserListener();

    /**
     * [RUS] Добавить слушателей для действий с браузером.
     * Либо пустое множество.
     * Метод {@link ru.mk.pump.web.common.WebReporter#setListenersConfiguration(WebListenersConfiguration)} - для применения настроек.
     */
    @NonNull
    Set<BrowserListener> getBrowserListener(Browser browser);

    /**
     * [RUS] Нужно ли очистить от слушателей по-умолчанию. Для {@link #getPageListener}.
     */
    boolean erasePageListener();

    /**
     * [RUS] Добавить слушателей для действий со страницей.
     * Либо пустое множество.
     * Метод {@link ru.mk.pump.web.common.WebReporter#setListenersConfiguration(WebListenersConfiguration)} - для применения настроек.
     */
    @NonNull
    Set<PageListener> getPageListener(BasePage page);

    /**
     * [RUS] Нужно ли очистить от слушателей по-умолчанию. Для {@link #getTestVarsListener}.
     */
    boolean eraseTestVarsListener();

    /**
     * [RUS] Добавить слушателей для изменения глобальных тестовых переменных.
     * Либо пустое множество.
     * Метод {@link ru.mk.pump.web.common.WebReporter#setListenersConfiguration(WebListenersConfiguration)} - для применения настроек.
     */
    @NonNull
    Set<TestVarListener> getTestVarsListener(TestVars globalVars);

    /**
     * [RUS] Нужно ли очистить от слушателей по-умолчанию.Для {@link #getActionListener}.
     */
    boolean eraseActionListener();

    /**
     * [RUS] Добавить слушателей для выполнения действия над элементом.
     * Либо пустое множество.
     * Метод {@link ru.mk.pump.web.common.WebReporter#setListenersConfiguration(WebListenersConfiguration)} - для применения настроек.
     */
    @NonNull
    Set<ActionListener> getActionListener(BaseElement element);

    /**
     * [RUS] Нужно ли очистить от слушателей по-умолчанию. Для {@link #getActionStateListener}.
     */
    boolean eraseActionStateListener();

    /**
     * [RUS] Добавить слушателей для проверки состояния во время выполнения действия.
     * Либо пустое множество.
     * Метод {@link ru.mk.pump.web.common.WebReporter#setListenersConfiguration(WebListenersConfiguration)} - для применения настроек.
     */
    @NonNull
    Set<StateListener> getActionStateListener(BaseElement element);

    /**
     * [RUS] Нужно ли очистить от слушателей по-умолчанию. Для {@link #getStateListener}.
     */
    boolean eraseStateListener();

    /**
     * [RUS] Добавить слушателей для проямой проверки состояния.
     * Либо пустое множество.
     * Метод {@link ru.mk.pump.web.common.WebReporter#setListenersConfiguration(WebListenersConfiguration)} - для применения настроек.
     */
    @NonNull
    Set<StateListener> getStateListener(BaseElement element);
}
