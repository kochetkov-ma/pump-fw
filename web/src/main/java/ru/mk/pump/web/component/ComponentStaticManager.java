package ru.mk.pump.web.component;

import java.lang.reflect.Constructor;
import java.util.Map;
import lombok.ToString;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.AbstractItemsManager;
import ru.mk.pump.web.common.api.annotations.PComponent;
import ru.mk.pump.web.utils.WebReporter;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString
public class ComponentStaticManager extends AbstractItemsManager<BaseComponent> {

    //region CONSTRUCTORS
    public ComponentStaticManager(Browser browser, Reporter reporter, String... packagesName) {
        super(browser, reporter, packagesName);
    }

    public ComponentStaticManager(Browser browser, String... packagesName) {
        this(browser, WebReporter.getReporter(), packagesName);
    }
    //endregion

    @Override
    protected BaseComponent newInstance(Constructor<? extends BaseComponent> constructor) throws ReflectiveOperationException {
        return constructor.newInstance(getBrowser(), getReporter());
    }

    @Override
    protected Constructor<? extends BaseComponent> findConstructor(Class<? extends BaseComponent> itemClass) throws ReflectiveOperationException {
        return itemClass.getConstructor(Browser.class, Reporter.class);
    }

    @Override
    protected BaseComponent afterItemCreate(BaseComponent itemInstance) {
        return handleAnnotations(itemInstance);
    }

    @Override
    protected boolean findFilter(String name, Class<? extends BaseComponent> itemClass) {
        return itemClass.isAnnotationPresent(PComponent.class) && itemClass.getAnnotation(PComponent.class).value().equalsIgnoreCase(name) || itemClass
            .getSimpleName().equals(name);
    }

    @Override
    protected Class<BaseComponent> getItemClass() {
        return BaseComponent.class;
    }

    protected BaseComponent handleAnnotations(BaseComponent component) {
        if (component.getClass().isAnnotationPresent(PComponent.class)) {
            PComponent aPage = component.getClass().getAnnotation(PComponent.class);
            if (!Strings.isEmpty(aPage.value())) {
                component.setName(aPage.value());
            }
            if (!Strings.isEmpty(aPage.desc())) {
                component.setName(aPage.desc());
            }
        }
        return component;
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoFromSuper(this, super.getInfo())
            .build();
    }
}