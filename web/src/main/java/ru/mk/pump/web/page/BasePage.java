package ru.mk.pump.web.page;

import static java.lang.String.format;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.CallableExt;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.WebReporter;
import ru.mk.pump.web.common.api.WebListenersConfiguration;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.common.pageobject.Initializer;
import ru.mk.pump.web.component.api.Component;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.web.page.api.PageListener;
import ru.mk.pump.web.page.api.PageLoader;
import ru.mk.pump.web.utils.UrlUtils;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings( {"WeakerAccess", "unused"})
@ToString(of = {"baseUrl", "resourcePath", "name", "description", "url"})
public class BasePage extends PageNotifier implements Page {

    @PElement("Тело страницы")
    @FindBy(tagName = "body")
    private Element pageBody;

    @Getter
    @Setter
    private String baseUrl;

    @Getter
    @Setter
    private String resourcePath;

    @Getter
    private final Browser browser;

    @Setter
    private Reporter reporter;

    @Getter
    @Setter
    private String name = Strings.empty();

    @Setter
    @Getter
    private String description = StringConstants.UNDEFINED;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private String[] extraUrls;

    @Setter
    private String url;

    @Setter
    private Initializer initializer;

    @Setter
    private PageLoader pageLoader;

    public BasePage(@NonNull Browser browser) {
        super();
        this.browser = browser;
        afterConstruct();
    }

    public BasePage(@NonNull Browser browser, Reporter reporter) {
        super();
        this.browser = browser;
        this.reporter = reporter;
        afterConstruct();
    }

    public Reporter getReporter() {
        if (reporter == null) {
            reporter = WebReporter.getReporter();
        }
        return reporter;
    }

    protected void afterConstruct() {
        initAllElements();
        getPageLoader().addAdditionalCondition(CallableExt.of(this::jsReady).withDescription("Waiting js on page"));
        getPageLoader().addDisplayedElements(pageBody);
        getTitle().ifPresent(el -> getPageLoader().addTextContainsElement(el, getName()));
        addListener(newDefaultListener());
        final WebListenersConfiguration configuration = WebReporter.getListenersConfiguration();
        if (configuration != null) {
            if (configuration.erasePageListener()) {
                clearListeners();
            }
            addListeners(configuration.getPageListener(this));
        }
    }

    protected void afterOpen() {
        check();
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoBuilder("page")
                .put("name", name)
                .put("url", url)
                .put("browser", browser.getId())
                .build();
    }

    @Override
    public Initializer getInitializer() {
        if (initializer == null) {
            initializer = new Initializer(new ElementFactory(new ElementImplDispatcher(), this), new ElementFactory(Component.getImplDispatcher(), this));
        }
        return initializer;
    }

    @Override
    public String getText() {
        return pageBody.getText();
    }

    public String getUrl() {
        if (UrlUtils.isUrl(url)) {
            return url;
        } else if (getBaseUrl() != null) {
            return UrlUtils.concatWithPath(getBaseUrl(), getResourcePath());
        } else {
            return Strings.empty();
        }
    }

    @Override
    public void open() {
        notifyOnBeforeLoad(this);
        try {
            getBrowser().open(getUrl());
            //afterOpen();
        } catch (Throwable throwable) {
            notifyOnLoadFail(this, throwable);
            throw throwable;
        }
        notifyOnLoadSuccess(this);
    }

    protected boolean jsReady() {
        final String js = "return document.readyState";
        return "complete".equals(Strings.toString(getBrowser().actions().executeScript(js)));
    }

    @Override
    public Optional<Element> getTitle() {
        return Optional.empty();
    }

    @Override
    public PageLoader getPageLoader() {
        if (pageLoader == null) {
            if (WebReporter.getReporter() == getReporter()) {
                pageLoader = new PageLoaderPump(this, WebReporter.getVerifier());
            } else {
                pageLoader = new PageLoaderPump(this, new Verifier(getReporter()));
            }
        }
        return pageLoader;
    }

    private PageListener newDefaultListener() {
        return new PageListener() {
            @Override
            public void onLoadSuccess(Page page) {
                getReporter().info(format("Page '%s' load success", name), page.toString(), getReporter().attachments().dummy());
            }

            @Override
            public void onLoadFail(Page page, Throwable fromArgsOrNull) {
                getReporter().warn(format("Page '%s' load failed", name), page.toString(), fromArgsOrNull);
            }

            @Override
            public void onBeforeLoad(Page page) {
                getReporter().info(format("Page '%s' is opening", name), page.toString(), getReporter().attachments().dummy());
            }
        };
    }

    public void check() {
        getPageLoader().checkAdditionalCondition();
        getPageLoader().checkElements();
        getPageLoader().checkUrl();
    }
}