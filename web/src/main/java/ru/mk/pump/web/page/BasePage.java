package ru.mk.pump.web.page;

import static java.lang.String.format;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.pageobject.Initializer;
import ru.mk.pump.web.component.api.Component;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.web.page.api.PageListener;
import ru.mk.pump.web.page.api.PageLoader;
import ru.mk.pump.web.utils.UrlUtils;
import ru.mk.pump.web.utils.WebReporter;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString(exclude = {"browser", "reporter", "initializer", "pageLoader"})
public class BasePage extends PageNotifier implements Page {

    public static final By DEFAULT_BODY_BY = By.tagName("body");

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
    private String name;

    @Setter
    @Getter
    private String description = StringConstants.UNDEFINED;

    @Setter
    private String url;

    @Setter
    private Initializer initializer;

    @Setter
    private PageLoader pageLoader;

    public BasePage(@NotNull Browser browser) {
        super();
        this.browser = browser;
        afterConstruct();
    }

    public BasePage(@NotNull Browser browser, Reporter reporter) {
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
        getPageLoader().addAdditionalCondition(this::jsReady);
        addListener(newDefaultListener());
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

    public String getUrl() {
        if (UrlUtils.isUrl(url)) {
            return url;
        } else {
            return UrlUtils.concatWithPath(getBaseUrl(), getResourcePath());
        }
    }

    @Override
    public void open() {
        notifyOnBeforeLoad(this);
        try {
            getBrowser().open(getUrl());
            afterOpen();
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
    public String getTitle() {
        return StringConstants.UNDEFINED;
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
                getReporter().info(format("Page '%s' load success", name), page.toString());
            }

            @Override
            public void onLoadFail(Page page, Throwable fromArgsOrNull) {
                getReporter().warn(format("Page '%s' load failed", name), page.toString(), fromArgsOrNull);
            }

            @Override
            public void onBeforeLoad(Page page) {
                getReporter().info(format("Page '%s' is opening", name), page.toString());
            }
        };
    }
}