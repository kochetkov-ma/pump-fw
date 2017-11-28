package ru.mk.pump.web.elements.internal;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.Page;

/**
 * PUBLIC BASE IMPLEMENTATION InternalElement interface
 */
@SuppressWarnings("unused")
@Slf4j
public class BaseElement extends AbstractElement<BaseElement> implements Element {

    private Map<String, Parameter<?>> elementParams;

    private ElementFactory selfElementFactory;

    public BaseElement(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public BaseElement(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public BaseElement(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    public Map<String, Parameter<?>> getParams() {
        return elementParams;
    }

    public BaseElement withParams(Map<String, Parameter<?>> elementConfig) {
        this.elementParams = elementConfig;
        return this;
    }


    @Override
    public String getText() {
        return getActionExecutor().execute(getTextAction());
    }

    @Override
    public void clear() {
        getActionExecutor().execute(getClearAction());
    }

    @Override
    public String getTextSilent() {
        return getActionExecutor().execute(getTextAction().redefineExpectedState(exists()));
    }

    @Override
    public void click() {
        getActionExecutor().execute(getClickAction());
    }


    @Override
    public boolean isDisplayed() {
        final SetState res = getStateResolver().resolve(displayed());
        log.info(StringConstants.LINE + Strings.mapToPrettyString(res.getInfo()));
        return res.result().isSuccess();
    }

    @Override
    public boolean isNotDisplayed() {
        final SetState res = getStateResolver().resolve(notDisplayed());
        log.info(StringConstants.LINE + Strings.mapToPrettyString(res.getInfo()));
        return res.result().isSuccess();
    }

    @Override
    public <T extends Element> SubElementHelper<T> getSubElements(Class<T> subElementClazz) {
        return new SubElementHelper<>(subElementClazz, this, getSubElementFactory());
    }

    @Override
    public String toPrettyString() {
        return Strings.mapToPrettyString(getInfo());
    }

    @Override
    public Map<String, String> getInfo() {
        final Map<String, String> result = Maps.newLinkedHashMap();
        result.put("type", getClass().getSimpleName());
        result.put("name", getName());
        result.put("description", getElementDescription());
        result.put("browser", getBrowser().getId());
        result.put("elementParams", Strings.mapToPrettyString(elementParams, "elementParams".length() + 3));
        return result;
    }

    @Override
    protected ActionExecutor newDelegateActionExecutor(StateResolver stateResolver) {
        return super.newDelegateActionExecutor(stateResolver)
            .addBefore(getFocusAction());
    }

    @Override
    public BaseElement setIndex(int index) {
        super.setIndex(index);
        return this;
    }

    public BaseElement setSelfFactory(ElementFactory selfElementFactory) {
        this.selfElementFactory = selfElementFactory;
        return this;
    }

    private ElementFactory getSubElementFactory() {
        if (selfElementFactory == null) {
            if (getPage().isPresent()) {
                return new ElementFactory(new ElementImplDispatcher(), getPage().get());
            } else {
                return new ElementFactory(new ElementImplDispatcher(), getBrowser());
            }
        } else {
            return selfElementFactory;
        }
    }
}
