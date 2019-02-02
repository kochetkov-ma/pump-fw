package ru.mk.pump.web.elements.internal;

import static java.lang.String.format;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.ParameterUtils;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.enums.ClearType;
import ru.mk.pump.web.elements.enums.FocusType;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

import java.util.List;

/**
 * [RUS]
 * Полностью package-private хранилище действий для {@link AbstractElement}
 * Исключительно для удобства
 */
@SuppressWarnings("unused")
public class ActionsStore {

    private final AbstractElement<?> element;

    private final ActionFactory actions;

    ActionsStore(AbstractElement abstractElement, ActionFactory actions) {
        this.element = abstractElement;
        this.actions = actions;
    }

    public Action<Boolean> selected() {
        return actions.newAction(WebElement::isSelected, "Is selected")
                .withStrategy(ActionStrategy.SIMPLE, ActionStrategy.NO_STATE_CHECK);
    }

    Action<String> clickAction() {
        return actions.newVoidAction(WebElement::click, "Click");
    }

    Action<String> textAction() {
        return actions.newAction(webElement -> {
            return element.getBrowser().actions().getText(webElement);
        }, "Get text");
    }

    Action clear() {
        return actions.newVoidAction((webElement, param) -> {
            final ClearType clearType = ParameterUtils.getOrDefault(param, ElementParams.CLEAR_TYPE.getName(), ClearType.class, ClearType.ADVANCED);
            //noinspection ConstantConditions
            switch (clearType) {
                case BASIC:
                    webElement.clear();
                    return;
                case KEYBOARD:
                    webElement.clear();
                    webElement.sendKeys(Keys.CONTROL, "a");
                    webElement.sendKeys(Keys.BACK_SPACE);
                    return;
                case ADVANCED:
                    webElement.clear();
                    State state = element.getInternalStateResolver().resolveFast(element.clearState());
                    if (state.isResolved() && state.result().isSuccess()) {
                        return;
                    }
                    webElement.sendKeys(Keys.CONTROL, "a");
                    webElement.sendKeys(Keys.BACK_SPACE);
                    element.getInternalStateResolver().resolveFast(element.clearState()).result().throwExceptionOnFail();
                    return;
                default:
                    throw new UnsupportedOperationException("ClearType has been modified. Add new type of ClearType in ActionStore");
            }
        }, "Clear element text");
    }

    Action<String> inputAction(CharSequence... keysToSend) {
        return actions.newAction(webElement -> {
            webElement.sendKeys(keysToSend);
            //TODO: Добавить возможность отключения возврата введенного текста для ускорения
            return element.getActionExecutor().execute(element.getTextAction().withStrategy(ActionStrategy.SIMPLE));
        }, format("Set text '%s'", String.join("_", keysToSend)));
    }

    Action focusAction() {
        final String SCROLL_TOP = "arguments[0].scrollIntoView(true);";
        final String SCROLL_BOTTOM = "arguments[0].scrollIntoView(false);";
        final String SCROLL_CENTER = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

        return actions.newVoidAction((webElement, param) -> {
            String scrollScript = SCROLL_TOP;
            if (param.has(ElementParams.FOCUS_CUSTOM_SCRIPT.getName())) {
                //noinspection ConstantConditions
                scrollScript = param.get(ElementParams.FOCUS_CUSTOM_SCRIPT.getName()).getStringValue();
            } else {
                if (param.has(ElementParams.FOCUS_TYPE.getName())) {
                    //noinspection ConstantConditions
                    final FocusType focusType = param.get(ElementParams.FOCUS_TYPE.getName()).getValue(FocusType.class);
                    //noinspection ConstantConditions
                    switch (focusType) {
                        case BOTTOM:
                            scrollScript = SCROLL_BOTTOM;
                            break;
                        case TOP:
                            scrollScript = SCROLL_TOP;
                            break;
                        case CENTER:
                            scrollScript = SCROLL_CENTER;
                            break;
                        default:
                            scrollScript = SCROLL_TOP;
                    }
                }
            }
            if (!Strings.isEmpty(scrollScript)) {
                element.getBrowser().actions().executeScript(scrollScript, webElement);
            }
        }, "Focus on element");
    }

    Action<Boolean> tryClick() {
        return actions.newAction(element -> {
            try {
                element.click();
                return true;
            } catch (Exception ignore) {
                return false;
            }
        }, "Is clickable");
    }

    Action<String> tagName() {
        return actions.newAction(WebElement::getTagName, "Get Attribute")
                .withStrategy(ActionStrategy.SIMPLE)
                .redefineExpectedState(element.exists());
    }

    Action<String> attribute(String name) {
        return actions.newAction(e -> e.getAttribute(name), "Get Attribute")
                .withStrategy(ActionStrategy.NO_FINALLY, ActionStrategy.NO_AFTER).redefineExpectedState(element.exists());
    }

    <T extends InternalElement> Action<List<T>> subItemsAction(By by, Class<T> elementClass) {
        return null;
    }


    <T extends InternalElement> Action subItemAction(By by, Class<T> elementClass) {
        return null;
    }

}
