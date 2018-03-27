package ru.mk.pump.web.page;

import lombok.ToString;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.common.AbstractItemsManager;
import ru.mk.pump.web.common.pageobject.PumpElementAnnotations;
import ru.mk.pump.web.page.api.PageLoader;
import ru.mk.pump.web.utils.WebReporter;

import java.lang.reflect.Constructor;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString(callSuper = true)
public class PageManager extends AbstractItemsManager<BasePage> {

    private final PageLoader pageLoader;

    //region CONSTRUCTORS
    public PageManager(Browsers browsers, Reporter reporter, PageLoader pageLoader, String... packagesName) {
        super(browsers, reporter, packagesName);
        this.pageLoader = pageLoader;
    }

    public PageManager(Browsers browsers, String... packagesName) {
        this(browsers, WebReporter.getReporter(), null, packagesName);
    }
    //endregion

    @Override
    protected BasePage newInstance(Constructor<? extends BasePage> constructor, Class<? extends BasePage> itemClass) throws ReflectiveOperationException {
        if (constructor.getParameterCount() == 2) {
            return constructor.newInstance(getBrowsers().get(), getReporter());
        } else {
            return constructor.newInstance(getBrowsers().get());
        }
    }

    @Override
    protected Constructor<? extends BasePage> findConstructor(Class<? extends BasePage> itemClass) throws ReflectiveOperationException {
        Constructor<? extends BasePage> res = ConstructorUtils.getAccessibleConstructor(itemClass, Browsers.class, Reporter.class);
        if (res == null) {
            return itemClass.getConstructor(Browser.class);
        } else {
            return res;
        }
    }

    @Override
    protected BasePage afterItemCreate(BasePage itemInstance) {
        BasePage res = handleAnnotations(itemInstance);
        if (pageLoader != null) {
            res.setPageLoader(pageLoader);
        }
        return res;
    }

    @Override
    protected boolean findFilter(String pageName, Class<? extends BasePage> itemClass) {
        PumpElementAnnotations annotations = new PumpElementAnnotations(itemClass);
        return annotations.getPageName().equalsIgnoreCase(pageName)
                || itemClass.getSimpleName().equals(pageName);
    }

    @Override
    protected Class<BasePage> getItemClass() {
        return BasePage.class;
    }

    protected BasePage handleAnnotations(BasePage page) {
        PumpElementAnnotations annotations = new PumpElementAnnotations(page.getClass());
        Strings.ifNotEmptyOrBlank(annotations.getPageName(), page::setName);
        Strings.ifNotEmptyOrBlank(annotations.getPageDescription(), page::setDescription);
        Strings.ifNotEmptyOrBlank(annotations.getPageBaseUrl(), page::setBaseUrl);
        Strings.ifNotEmptyOrBlank(annotations.getPageResource(), page::setResourcePath);
        return page;
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoFromSuper(this, super.getInfo())
                .put("pageLoader", Strings.toString(pageLoader))
                .build();
    }
}