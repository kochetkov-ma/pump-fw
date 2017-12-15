package ru.mk.pump.web.elements.internal;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.elements.utils.Parameters;
import ru.mk.pump.web.page.Page;

/**
 * PUBLIC BASE IMPLEMENTATION InternalElement interface
 */
@SuppressWarnings("unused")
@Slf4j
public class BaseElement extends AbstractElement<BaseElement> implements Element {

    private final Map<String, Parameter<?>> elementParams = Maps.newHashMap();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private By[] extraBy = new By[0];

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
        elementParams.putAll(elementConfig);
        initFromParams();
        return this;
    }

    /**
     * init extra by field from params or null. param name is {@link ElementParams#BASE_EXTRA_BY}
     */
    protected void initFromParams() {
        extraBy = Parameters.getOrDefault(getParams(), ElementParams.BASE_EXTRA_BY, By[].class, extraBy);
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
    public String getTextHidden() {
        return getActionExecutor().execute(getTextAction().withStrategy(ActionStrategy.SIMPLE).redefineExpectedState(exists()));
    }

    @Override
    public void click() {
        getActionExecutor().execute(getClickAction());
    }

    @Override
    public boolean isDisplayed() {
        final SetState res = getStateResolver().resolve(displayed());
        return res.result().isSuccess();
    }

    @Override
    public boolean isNotDisplayed() {
        final SetState res = getStateResolver().resolve(notDisplayed());
        return res.result().isSuccess();
    }

    @Override
    public boolean isExists() {
        final SetState res = getStateResolver().resolve(exists());
        return res.result().isSuccess();
    }

    @Override
    public boolean isNotExists() {
        final State res = getStateResolver().resolve(notExists());
        return res.result().isSuccess();
    }

    @Override
    public boolean isEnabled() {
        final SetState res = getStateResolver().resolve(enabled());
        return res.result().isSuccess();
    }

    @Override
    public boolean isNotEnabled() {
        final SetState res = getStateResolver().resolve(notEnabled());
        return res.result().isSuccess();
    }

    @Override
    public <T extends Element> SubElementHelper<T> getSubElements(Class<T> subElementClazz) {
        return new SubElementHelper<>(subElementClazz, this, getSubElementFactory());
    }

    public boolean isJsReady() {
        final State res = getStateResolver().resolve(jsReady());
        return res.result().isSuccess();
    }

    @Override
    public String toPrettyString() {
        return Strings.toPrettyString(getInfo());
    }

    public BaseElement setSelfFactory(ElementFactory selfElementFactory) {
        this.selfElementFactory = selfElementFactory;
        return this;
    }

    @Override
    public String getDescription() {
        return super.getElementDescription();
    }

    /* for test */
    /*
    @Override
    protected StateResolver newDelegateStateResolver() {

        return (StateResolver) super.newDelegateStateResolver().addListener(new StateListener() {
            @Override
            public void onBefore(State state) {
                log.info("BEFORE STATE");
                log.info(Strings.toPrettyString(state.getInfo()));
            }

            @Override
            public void onFinish(State state) {
                log.info("FINISH STATE");
                log.info(Strings.toPrettyString(state.getInfo()));
            }
        });
    }
    */

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

    @Override
    public Map<String, String> getInfo() {
        final Map<String, String> res = super.getInfo();
        res.put("element parameters", Strings.toPrettyString(getParams()));
        return res;
    }

    private ElementFactory getSubElementFactory() {
        if (selfElementFactory == null) {
            if (getPage() != null) {
                return new ElementFactory(new ElementImplDispatcher(), getPage());
            } else {
                return new ElementFactory(new ElementImplDispatcher(), getBrowser());
            }
        } else {
            return selfElementFactory;
        }
    }
}
