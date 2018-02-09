package ru.mk.pump.web.page;

import java.lang.reflect.Constructor;
import java.util.Map;
import lombok.ToString;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.AbstractItemsManager;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.page.api.PageLoader;
import ru.mk.pump.web.utils.WebReporter;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString
public class PageManager extends AbstractItemsManager<BasePage> {

    private final PageLoader pageLoader;

    //region CONSTRUCTORS
    public PageManager(Browser browser, Reporter reporter, PageLoader pageLoader, String... packagesName) {
        super(browser, reporter, packagesName);
        this.pageLoader = pageLoader;
    }

    public PageManager(Browser browser, String... packagesName) {
        this(browser, WebReporter.getReporter(), null, packagesName);
    }
    //endregion

    @Override
    protected BasePage newInstance(Constructor<? extends BasePage> constructor) throws ReflectiveOperationException {
        return constructor.newInstance(getBrowser(), getReporter());
    }

    @Override
    protected Constructor<? extends BasePage> findConstructor(Class<? extends BasePage> itemClass) throws ReflectiveOperationException {
        return itemClass.getConstructor(Browser.class, Reporter.class);
    }

    @Override
    protected BasePage afterItemCreate(BasePage itemInstance) {
        BasePage res = handleAnnotations(itemInstance);
        res.setPageLoader(pageLoader);
        return res;
    }

    @Override
    protected boolean findFilter(String pageName, Class<? extends BasePage> itemClass) {
        return itemClass.isAnnotationPresent(PPage.class) && itemClass.getAnnotation(PPage.class).value().equalsIgnoreCase(pageName) || itemClass
            .getSimpleName().equals(pageName);
    }

    @Override
    protected Class<BasePage> getItemClass() {
        return BasePage.class;
    }

    protected BasePage handleAnnotations(BasePage page) {
        if (page.getClass().isAnnotationPresent(PPage.class)) {
            PPage aPage = page.getClass().getAnnotation(PPage.class);
            if (!Strings.isEmpty(aPage.value())) {
                page.setName(aPage.value());
            }
            if (!Strings.isEmpty(aPage.desc())) {
                page.setDescription(aPage.desc());
            }
            if (!Strings.isEmpty(aPage.baseUrl())) {
                page.setBaseUrl(aPage.baseUrl());
            }
            if (!Strings.isEmpty(aPage.resource())) {
                page.setResourcePath(aPage.resource());
            }
        }
        return page;
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoFromSuper(this, super.getInfo())
            .put("pageLoader", Strings.toString(pageLoader))
            .build();
    }
}