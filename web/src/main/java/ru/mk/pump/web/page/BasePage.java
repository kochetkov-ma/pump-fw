package ru.mk.pump.web.page;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.pageobject.Initializer;
import ru.mk.pump.web.component.api.Component;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.web.utils.UrlUtils;

@SuppressWarnings("WeakerAccess")
@ToString
public class BasePage implements Page {

    @Getter
    @Setter
    private String baseUrl;

    @Getter
    @Setter
    private String resourcePath;

    @Getter
    private final Browser browser;

    @Getter
    @Setter
    private String name;

    @Setter
    private String url;

    private Initializer initializer;

    public BasePage(@NotNull Browser browser) {
        this.browser = browser;
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
        getBrowser().getDriver().get(getUrl());
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}