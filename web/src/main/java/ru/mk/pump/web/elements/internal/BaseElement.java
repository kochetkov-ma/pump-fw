package ru.mk.pump.web.elements.internal;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.reporter.ReporterAllure;
import ru.mk.pump.commons.utils.BrowserScreenshoter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.pageobject.Initializer;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.interfaces.ElementInfo;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

/**
 * PUBLIC BASE IMPLEMENTATION InternalElement interface
 */
@SuppressWarnings("unused")
@Slf4j
public class BaseElement extends AbstractElement<BaseElement> implements Element {

    private final Map<String, Parameter<?>> elementParams = Maps.newHashMap();

    private ElementFactory selfElementFactory;

    @Getter(AccessLevel.PROTECTED)
    private Reporter reporter;

    @Getter(AccessLevel.PROTECTED)
    private Verifier verifier;

    private Initializer initializer;

    public BaseElement(By avatarBy, Page page) {
        super(avatarBy, page);
        initLocal();
    }

    public BaseElement(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
        initLocal();
    }

    public BaseElement(By avatarBy, Browser browser) {
        super(avatarBy, browser);
        initLocal();
    }

    public Map<String, Parameter<?>> getParams() {
        return elementParams;
    }

    public BaseElement withParams(Map<String, Parameter<?>> elementConfig) {
        elementParams.putAll(elementConfig);
        initFromParams();
        getActionExecutor().withParameters(elementConfig);
        return this;
    }

    /**
     * Init all parameters:
     * <ul>
     *     <li>init field like : {@code private final By[] fieldParam}</li>
     *     <li>override this method in your subclass</li>
     *     <li>read parameter and write to the field in method body like :
     *     {@code fieldParam = Parameters.getOrDefault(getParams(), ElementParams.PARAMETER_NAME, By[].class, fieldParam)}</li>
     *     <li>do not forget to call super : {@code super.initFromParams()}</li>
     *     <li>do this in all subclass with parameters</li>
     * </ul>
     */
    protected void initFromParams() {
        /*
         * init class fields fom parameter value
         */
    }

    public BaseElement withVerifier(@Nullable Verifier verifier) {
        if (verifier != null) {
            this.verifier = verifier;
        }
        return this;
    }

    public BaseElement withReporter(@Nullable Reporter reporter) {
        if (reporter != null) {
            this.reporter = reporter;
        }
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
    public String getTextHidden() {
        return getActionExecutor().execute(getTextAction().withStrategy(ActionStrategy.SIMPLE).redefineExpectedState(exists()));
    }

    @Override
    public void click() {
        getActionExecutor().execute(getClickAction());
    }

    @Override
    public ElementInfo info() {
        return this;
    }

    @Override
    public InternalElement advanced() {
        return this;
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
    public boolean isDisplayed(int timeoutMs) {
        return getStateResolver().resolve(displayed(), timeoutMs).result().isSuccess();
    }

    @Override
    public boolean isNotDisplayed(int timeoutMs) {
        return getStateResolver().resolve(notDisplayed(), timeoutMs).result().isSuccess();
    }

    @Override
    public boolean isExists(int timeoutMs) {
        return getStateResolver().resolve(exists(), timeoutMs).result().isSuccess();
    }

    @Override
    public boolean isNotExists(int timeoutMs) {
        return getStateResolver().resolve(notExists(), timeoutMs).result().isSuccess();
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

    @Override
    protected ActionExecutor newDelegateActionExecutor(StateResolver stateResolver) {
        return super.newDelegateActionExecutor(stateResolver)
            .addBefore(getFocusAction());
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

    private void initLocal() {
        withReporter(new ReporterAllure(new BrowserScreenshoter(getBrowser())));
        withVerifier(new Verifier(getReporter()));
    }

    protected ElementFactory getSubElementFactory() {
        if (selfElementFactory == null) {
            if (getPage() != null) {
                selfElementFactory = new ElementFactory(new ElementImplDispatcher(), getPage());
            } else {
                selfElementFactory = new ElementFactory(new ElementImplDispatcher(), getBrowser());
            }
        }
        return selfElementFactory;
    }
}
