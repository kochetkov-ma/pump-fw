package ru.mk.pump.web.component;

import lombok.ToString;
import org.openqa.selenium.By;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.common.AbstractItemsManager;
import ru.mk.pump.web.common.api.annotations.PComponent;
import ru.mk.pump.web.common.pageobject.PumpElementAnnotations;
import ru.mk.pump.web.common.WebReporter;

import java.lang.reflect.Constructor;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString(callSuper = true)
public class ComponentStaticManager extends AbstractItemsManager<BaseComponent> {

    //region CONSTRUCTORS
    public ComponentStaticManager(Browsers browsers, Reporter reporter, String... packagesName) {
        super(browsers, reporter, packagesName);
    }

    public ComponentStaticManager(Browsers browsers, String... packagesName) {
        this(browsers, WebReporter.getReporter(), packagesName);
    }
    //endregion

    @Override
    protected BaseComponent newInstance(Constructor<? extends BaseComponent> constructor, Class<? extends BaseComponent> itemClass) throws ReflectiveOperationException {
        PumpElementAnnotations pumpElementAnnotations = new PumpElementAnnotations(itemClass);
        return constructor.newInstance(pumpElementAnnotations.buildBy(), getBrowsers().get());
    }

    @Override
    protected Constructor<? extends BaseComponent> findConstructor(Class<? extends BaseComponent> itemClass) throws ReflectiveOperationException {
        return itemClass.getConstructor(By.class, Browser.class);
    }

    @Override
    protected BaseComponent afterItemCreate(BaseComponent itemInstance) {
        itemInstance.setReporter(getReporter());
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
        PumpElementAnnotations pumpElementAnnotations = new PumpElementAnnotations(component.getClass());
        Str.ifNotEmptyOrBlank(pumpElementAnnotations.getComponentName(), component::setName);
        Str.ifNotEmptyOrBlank(pumpElementAnnotations.getComponentDescription(), component::setDescription);
        return component;
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoFromSuper(this, super.getInfo())
                .build();
    }
}