package ru.mk.pump.web.page;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Preconditions;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.exceptions.PageManagerException;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.web.page.api.PageLoader;
import ru.mk.pump.web.utils.WebReporter;

@SuppressWarnings("WeakerAccess")
@ToString
public class PageManager implements StrictInfo {

    private final String[] packages;

    private final Set<Class<? extends BasePage>> pageSet;

    private final Browser browser;

    private final Reporter reporter;

    private final PageLoader pageLoader;

    private BasePage currentPage;

    //region CONSTRUCTORS
    public PageManager(Browser browser, Reporter reporter, PageLoader pageLoader, String... packagesName) {
        Preconditions.checkNotEmpty(packagesName);
        this.browser = browser;
        this.reporter = reporter;
        this.pageLoader = pageLoader;
        this.packages = packagesName;
        pageSet = loadPageClasses(packagesName);
    }

    public PageManager(Browser browser, String... packagesName) {
        this(browser, WebReporter.getReporter(), null, packagesName);
    }
    //endregion

    @Nullable
    public Page currentPage() {
        return currentPage;
    }

    public Page getPage(String pageName) {
        if (currentPage.getName().equals(pageName)) {
            return currentPage;
        }
        Class<? extends BasePage> page = findPageClass(pageName, BasePage.class);
        currentPage = newPage(page);
        return currentPage;
    }

    /**
     *
     * @throws PageManagerException When page is not find
     */
    public <T extends BasePage> Class<T> findPageClass(String pageName, Class<T> pageClass) {
        Set<Class<T>> result = findPageClasses(annotations -> Arrays.stream(annotations)
            .filter(a -> PPage.class.isAssignableFrom(a.annotationType()))
            .anyMatch(a -> ((PPage) a).value().equals(pageName)), pageClass);
        if (result.isEmpty()) {
            throw new PageManagerException(String.format("Cannot find any page with name '%s' and class '%s'", pageName, pageClass.getCanonicalName()))
                .withPageManager(this);
        } else {
            return result.iterator().next();
        }
    }

    public <T extends BasePage> Set<Class<T>> findPageClasses(Class<T> pageClass) {
        //noinspection unchecked
        return pageSet.stream()
            .filter(pageClass::isAssignableFrom)
            .map(i -> (Class<T>) i)
            .collect(Collectors.toSet());
    }

    public <T extends BasePage> Set<Class<T>> findPageClasses(Predicate<Annotation[]> annotationProcessor, Class<T> pageClass) {
        //noinspection unchecked
        return pageSet.stream()
            .filter(pageClass::isAssignableFrom)
            .filter(page -> annotationProcessor.test(page.getClass().getAnnotations()))
            .map(i -> (Class<T>) i)
            .collect(Collectors.toSet());
    }

    private static Set<Class<? extends BasePage>> loadPageClasses(String... packagesName) {
        final Reflections reflections = new Reflections((Object[]) packagesName);
        return reflections.getSubTypesOf(BasePage.class);
    }

    /**
     *
     * @throws PageManagerException When page instance has not created
     */
    protected BasePage newPage(Class<? extends BasePage> pageClass) {
        try {
            final Constructor<? extends BasePage> constructor = pageClass.getConstructor(Browser.class, Reporter.class);
            constructor.setAccessible(true);
            BasePage result = constructor.newInstance(browser, reporter);
            if (pageLoader != null) {
                result.setPageLoader(pageLoader);
            }
            return handleAnnotations(result);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ex) {
            throw new PageManagerException(String.format("Error when try to create page with class '%s'", pageClass.getCanonicalName()), ex)
                .withPageManager(this);
        }
    }

    protected BasePage handleAnnotations(BasePage page) {
        if (page.getClass().isAnnotationPresent(PPage.class)) {
            PPage aPage = page.getClass().getAnnotation(PPage.class);
            if (!Strings.isEmpty(aPage.value())) {
                page.setName(aPage.value());
            }
            if (!Strings.isEmpty(aPage.desc())) {
                page.setName(aPage.desc());
            }
            if (!Strings.isEmpty(aPage.baseUrl())) {
                page.setName(aPage.baseUrl());
            }
            if (!Strings.isEmpty(aPage.resource())) {
                page.setName(aPage.resource());
            }
        }
        return page;
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoBuilder("pageManager")
            .put("browser", Strings.toString(browser))
            .put("current page", Strings.toString(currentPage))
            .put("reporter", Strings.toString(reporter))
            .put("pageLoader", Strings.toString(pageLoader))
            .put("loaded pages", Strings.toPrettyString(pageSet, "loaded pages".length()))
            .put("packages", Strings.toPrettyString(packages, "packages".length()))
            .build();
    }
}