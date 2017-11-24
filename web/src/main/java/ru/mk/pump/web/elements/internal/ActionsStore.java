package ru.mk.pump.web.elements.internal;

import static java.lang.String.format;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.enums.ClearType;
import ru.mk.pump.web.elements.enums.FocusType;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

/**
 * [RUS]
 * Польностью package-private хранилище действий для {@link AbstractElement}
 * Исключительно для удобства
 */
@SuppressWarnings("unused")
class ActionsStore {

    private final AbstractElement<?> element;

    private final ActionFactory actions;

    ActionsStore(AbstractElement abstractElement, ActionFactory actions) {
        this.element = abstractElement;
        this.actions = actions;
    }

    Action<String> clickAction() {
        return actions.newAction(WebElement::click, "Click");
    }

    Action<String> textAction() {
        return actions.newAction(webElement -> {
            return element.getBrowser().actions().getText(webElement);
        }, "Get text");
    }

    Action clear() {
        return actions.newAction((webElement, param) -> {
            if (param.containsKey("clearType")) {
                final ClearType clearType = param.get("clearType").getValue(ClearType.class);
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
                        SetState state = element.getStateResolver().resolve(element.clear());
                        if (state.isResolved() && state.result().isSuccess()) {
                            return;
                        }
                        webElement.sendKeys(Keys.CONTROL, "a");
                        webElement.sendKeys(Keys.BACK_SPACE);
                        element.getStateResolver().resolve(element.clear()).result().throwExceptionOnFail();
                        break;
                    default:
                        throw new UnsupportedOperationException("ClearType has been modified. Add new type of ClearType in ActionStore");
                }
            }
        }, "Clear element text");
    }

    Action<String> inputAction(CharSequence... keysToSend) {
        return actions.newAction(webElement -> {
            webElement.sendKeys(keysToSend);
            return element.getActionExecutor().execute(element.getTextAction().withStrategy(ActionStrategy.SIMPLE));
        }, format("Set text '%s'", String.join("_", keysToSend)));
    }

    Action focusAction() {
        final String SCROLL_TOP = "arguments[0].scrollIntoView(true);";
        final String SCROLL_BOTTOM = "arguments[0].scrollIntoView(false);";
        final String SCROLL_CENTER = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
            + "var elementTop = arguments[0].getBoundingClientRect().top;"
            + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

        return actions.newAction((webElement, param) -> {
            String scrollScript = SCROLL_CENTER;
            if (param.containsKey("focusType")) {
                final FocusType focusType = param.get("focusType").getValue(FocusType.class);
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
                        scrollScript = SCROLL_CENTER;
                }
            }
            element.getBrowser().actions().executeScript(scrollScript);
        }, "Focus on element");
    }

    <T extends InternalElement> Action<List<T>> subItemsAction(By by, Class<T> elementClass) {
        return null;
    }


    <T extends InternalElement> Action subItemAction(By by, Class<T> elementClass) {
        return null;
    }

}