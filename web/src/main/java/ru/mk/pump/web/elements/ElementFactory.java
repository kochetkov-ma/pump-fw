package ru.mk.pump.web.elements;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.ElementImplDispatcher.ElementImpl;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.ElementConfig;
import ru.mk.pump.web.elements.api.listeners.ActionListener;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementFactoryException;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
@ToString(exclude = {"elementImplDispatcher"})
public class ElementFactory implements StrictInfo {

    @Getter
    private final Page page;

    @Getter
    private final ElementImplDispatcher elementImplDispatcher;

    @Getter
    private final Browser browser;

    private Set<Class<? extends Annotation>> requirements = Sets.newHashSet();

    private List<ActionListener> actionListeners = Lists.newArrayList();

    //region constructors
    public ElementFactory(@NotNull ElementImplDispatcher elementImplDispatcher, @NotNull Page page) {
        this.elementImplDispatcher = elementImplDispatcher;
        this.page = page;
        this.browser = page.getBrowser();
    }

    public ElementFactory(@NotNull ElementImplDispatcher elementImplDispatcher, @NotNull Browser browser) {
        this.elementImplDispatcher = elementImplDispatcher;
        this.browser = browser;
        this.page = null;
    }
    //endregion

    //region type
    public ElementFactory withMainRequirements(@NotNull Class<? extends Annotation>[] requirements) {
        this.requirements.addAll(Arrays.asList(requirements));
        return this;
    }

    public ElementFactory addActionListener(@NotNull ActionListener actionListener) {
        this.actionListeners.add(actionListener);
        return this;
    }

    public ElementFactory withActionListener(@NotNull Collection<ActionListener> actionListener) {
        this.actionListeners.addAll(actionListener);
        return this;
    }
    //endregion

    public <R extends Element> R newElement(@NotNull Class<R> interfaceClass, @NotNull By by, @NotNull ElementConfig elementConfig) {
        final ElementImpl<? extends BaseElement> elementImplClass = elementImplDispatcher.findImplementation(interfaceClass, getRequirements(elementConfig));
        try {
            if (page == null) {
                final Constructor<? extends BaseElement> constructor = elementImplClass.getImplementation().getConstructor(By.class, Browser.class);
                constructor.setAccessible(true);
                return (R) fillElement(constructor.newInstance(by, browser), elementConfig);
            } else {
                final Constructor<? extends BaseElement> constructor = elementImplClass.getImplementation().getConstructor(By.class, Page.class);
                constructor.setAccessible(true);
                return (R) fillElement(constructor.newInstance(by, page), elementConfig);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ex) {
            throw new ElementFactoryException(new PumpMessage("Error element builder").addExtraInfo(getInfo()), ex);
        }
    }

    public <R extends Element> R newElement(@NotNull Class<R> interfaceClass, @NotNull By by, @NotNull Element parent,
        @NotNull ElementConfig elementConfig) {
        checkParent(parent);
        final ElementImpl<? extends BaseElement> elementImplClass = elementImplDispatcher.findImplementation(interfaceClass, getRequirements(elementConfig));
        try {
            final Constructor<? extends BaseElement> constructor = elementImplClass.getImplementation().getConstructor(By.class, InternalElement.class);
            constructor.setAccessible(true);
            return (R) fillElement(constructor.newInstance(by, (InternalElement) parent), elementConfig);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ex) {
            throw new ElementFactoryException(new PumpMessage("Error element builder").addExtraInfo(getInfo()), ex);
        }
    }

    @Override
    public Map<String, String> getInfo() {
        final LinkedHashMap<String, String> res = Maps.newLinkedHashMap();
        if (browser != null) {
            res.put("browser", browser.toString());
        }
        if (page != null) {
            res.put("page", page.toString());
        }
        if (!requirements.isEmpty()) {
            res.put("requirements", Strings.toPrettyString(requirements));
        }
        res.put("actionListeners", String.valueOf(actionListeners.size()));
        res.putAll(elementImplDispatcher.getInfo());
        return res;
    }

    private void checkParent(Element parent) throws ElementFactoryException {
        if (!(parent instanceof InternalElement)) {
            throw new ElementFactoryException(new PumpMessage(String
                .format("Parent element '%s' is not accessible expected interface '%s'", parent.getClass().getSimpleName(),
                    InternalElement.class.getSimpleName())).addExtraInfo(getInfo()));
        }
    }

    private Set<Class<? extends Annotation>> getRequirements(ElementConfig elementConfig) {
        return ImmutableSet.<Class<? extends Annotation>>builder().addAll(elementConfig.getRequirements()).addAll(requirements).build();
    }

    private <R extends BaseElement> R fillElement(R element, ElementConfig elementConfig) {
        element.setSelfFactory(this)
            .withParams(elementConfig.getParameters())
            .withName(elementConfig.getName())
            .withDescription(elementConfig.getDescription())
            .withReporter(elementConfig.getReporter())
            .withVerifier(elementConfig.getVerifier())
            .addActionListener(actionListeners);
        if (elementConfig.getIndex() != -1) {
            element.setIndex(elementConfig.getIndex());
        }
        return element;
    }
}
