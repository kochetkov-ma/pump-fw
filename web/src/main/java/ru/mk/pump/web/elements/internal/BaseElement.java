package ru.mk.pump.web.elements.internal;

import java.util.Map;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PAction;
import ru.mk.pump.web.common.pageobject.Initializer;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.interfaces.ElementInfo;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.web.utils.WebReporter;

/**
 * PUBLIC BASE IMPLEMENTATION InternalElement interface
 */
@SuppressWarnings("unused")
@Slf4j
public class BaseElement extends AbstractElement<BaseElement> implements Element {

    private final Parameters elementParams = Parameters.of();

    private ElementFactory selfElementFactory;

    @Getter(AccessLevel.PROTECTED)
    private Reporter reporter;

    @Getter(AccessLevel.PROTECTED)
    private Verifier verifier;

    private Initializer initializer;

    @SuppressWarnings("FieldCanBeLocal")
    private BaseElementHelper helper;

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

    public Parameters getParams() {
        return elementParams;
    }

    public BaseElement withParams(Parameters elementConfig) {
        elementParams.addAll(elementConfig);
        initFromParams();
        getActionExecutor().withParameters(elementConfig);
        return this;
    }

    @Override
    public boolean tryClick() {
        return getActionExecutor().execute(getActionsStore().tryClick());
    }

    public BaseElement withVerifier(@Nullable Verifier verifier) {
        if (verifier != null) {
            this.verifier = verifier;
        }
        return this;
    }

    public BaseElement setReporter(@Nullable Reporter reporter) {
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
    @PAction("виден")
    public State isDisplayed() {
        return getStateResolver().resolve(displayed());
    }

    @Override
    @PAction("не виден")
    public State isNotDisplayed() {
        return getStateResolver().resolve(notDisplayed());
    }

    @Override
    @PAction("присутствует")
    public State isExists() {
        return getStateResolver().resolve(exists());
    }

    @Override
    @PAction("не присутствует")
    public State isNotExists() {
        return getStateResolver().resolve(notExists());
    }

    @Override
    @PAction("включен")
    public State isEnabled() {
        return getStateResolver().resolve(enabled());
    }

    @Override
    @PAction("не включен")
    public State isNotEnabled() {
        return getStateResolver().resolve(notEnabled());
    }

    @Override
    @PAction("виден")
    public State isDisplayed(int timeoutMs) {
        return getStateResolver().resolve(displayed(), timeoutMs);
    }

    @Override
    @PAction("не виден")
    public State isNotDisplayed(int timeoutMs) {
        return getStateResolver().resolve(notDisplayed(), timeoutMs);
    }

    @Override
    @PAction("присутствует")
    public State isExists(int timeoutMs) {
        return getStateResolver().resolve(exists(), timeoutMs);
    }

    @Override
    @PAction("не присутствует")
    public State isNotExists(int timeoutMs) {
        return getStateResolver().resolve(notExists(), timeoutMs);
    }

    @Override
    public <T extends Element> SubElementHelper<T> getSubElements(Class<T> subElementClazz) {
        return new SubElementHelper<>(subElementClazz, this, getSubElementFactory());
    }

    @Override
    public void scroll() {
        getActionExecutor().execute(getFocusAction());
    }

    public boolean isJsReady() {
        final State res = getInternalStateResolver().resolve(jsReady());
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

    /**
     * Init all parameters:
     * <ul>
     * <li>init field like : {@code private final By[] fieldParam}</li>
     * <li>override this method in your subclass</li>
     * <li>read parameter and write to the field in method body like :
     * {@code fieldParam = ParameterUtils.getOrDefault(getParams(), ElementParams.PARAMETER_NAME, By[].class, fieldParam)}</li>
     * <li>do not forget to call super : {@code super.initFromParams()}</li>
     * <li>do this in all subclass with parameters</li>
     * </ul>
     */
    protected void initFromParams() {
        /*
         * init class fields fom parameter value
         */
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

    @Override
    public Map<String, String> getInfo() {
        final Map<String, String> res = super.getInfo();
        res.put("element parameters", Strings.toPrettyString(getParams()));
        return res;
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

    private void initLocal() {
        setReporter(WebReporter.getReporter());
        withVerifier(WebReporter.getVerifier());
        /*helper instance init*/
        this.helper = new BaseElementHelper(this);
        /*helper features init*/
        helper.stateActionReportingEnable();
        helper.windowSizeCheckerEnable();
        helper.stateReportingEnable();
        helper.actionsReportingEnable();
    }


}
