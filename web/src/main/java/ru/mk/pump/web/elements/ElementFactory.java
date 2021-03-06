package ru.mk.pump.web.elements;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.openqa.selenium.By;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.common.api.PageItemImplDispatcher;
import ru.mk.pump.web.elements.ElementImplDispatcher.ElementImpl;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.listeners.ActionListener;
import ru.mk.pump.web.elements.api.listeners.StateListener;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementFactoryException;
import ru.mk.pump.web.page.api.Page;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static ru.mk.pump.commons.utils.Str.format;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
@ToString(exclude = {"elementImplDispatcher"})
public class ElementFactory implements StrictInfo {

    @Getter
    private final Page page;

    @Getter
    private final PageItemImplDispatcher elementImplDispatcher;

    @Getter
    private final Browser browser;

    private Set<Class<? extends Annotation>> requirements = Sets.newHashSet();

    private List<ActionListener> actionListeners = Lists.newArrayList();

    private List<StateListener> stateListeners = Lists.newArrayList();

    //region constructors
    public ElementFactory(@NonNull PageItemImplDispatcher elementImplDispatcher, @NonNull Page page) {
        this.elementImplDispatcher = elementImplDispatcher;
        this.page = page;
        this.browser = page.getBrowser();
    }

    public ElementFactory(@NonNull PageItemImplDispatcher elementImplDispatcher, @NonNull Browser browser) {
        this.elementImplDispatcher = elementImplDispatcher;
        this.browser = browser;
        this.page = null;
    }
    //endregion

    //region type
    public ElementFactory addMainRequirements(@NonNull Class<? extends Annotation>[] requirements) {
        this.requirements.addAll(Arrays.asList(requirements));
        return this;
    }

    public ElementFactory addActionListener(@NonNull ActionListener actionListener) {
        this.actionListeners.add(actionListener);
        return this;
    }

    public ElementFactory withActionListener(@NonNull Collection<ActionListener> actionListener) {
        this.actionListeners.addAll(actionListener);
        return this;
    }

    public ElementFactory addStateListener(@NonNull StateListener stateListener) {
        this.stateListeners.add(stateListener);
        return this;
    }

    public ElementFactory withStateListener(@NonNull Collection<StateListener> stateListeners) {
        this.stateListeners.addAll(stateListeners);
        return this;
    }
    //endregion

    /**
     * Реализация элемента должна обладать всеми public конструкторами от {@link BaseElement}
     *
     * @param interfaceClass Интерфейс элемента на выходе
     * @param by             Локатор
     * @param elementConfig  Конфигурация элемента
     *
     * @return Инстанс найденной реализации элемента
     */
    public <R extends Element> R newElement(@NonNull Class<R> interfaceClass, @NonNull By by, @NonNull ElementConfig elementConfig) {
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
            throw new ElementFactoryException()
                    .withFactory(this)
                    .withTitle(format("Error when try to creating element with interface '{}'", interfaceClass.getCanonicalName()))
                    .withExtra("by", Str.toString(by))
                    .withExtra("elementConfig", Str.toString(elementConfig))
                    .withCause(ex);
        }
    }

    /**
     * Реализация элемента должна обладать всеми public конструкторами от {@link BaseElement}
     *
     * @param interfaceClass Интерфейс элемента на выходе
     * @param by             Локатор
     * @param parent         Инстанс родительского элемента
     * @param elementConfig  Конфигурация элемента
     *
     * @return Инстанс найденной реализации элемента
     */
    public <R extends Element> R newElement(@NonNull Class<R> interfaceClass, @NonNull By by, @NonNull Element parent,
            @NonNull ElementConfig elementConfig) {
        checkParent(parent);
        final ElementImpl<? extends BaseElement> elementImplClass = elementImplDispatcher.findImplementation(interfaceClass, getRequirements(elementConfig));
        try {
            final Constructor<? extends BaseElement> constructor = elementImplClass.getImplementation().getConstructor(By.class, InternalElement.class);
            constructor.setAccessible(true);
            return (R) fillElement(constructor.newInstance(by, (InternalElement) parent), elementConfig);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ex) {
            throw new ElementFactoryException()
                    .withFactory(this)
                    .withParent(parent)
                    .withTitle(format("Error when try to creating element with interface '{}'", interfaceClass.getCanonicalName()))
                    .withExtra("by", Str.toString(by))
                    .withExtra("elementConfig", Str.toString(elementConfig))
                    .withCause(ex);
        }
    }

    @Override
    public Map<String, String> getInfo() {
        final LinkedHashMap<String, String> res = Maps.newLinkedHashMap();
        if (browser != null) {
            res.put("browser", browser.getId());
        }
        if (page != null) {
            res.put("page", page.getName());
        }
        if (!requirements.isEmpty()) {
            res.put("requirements", Str.toPrettyString(requirements));
        }
        res.put("actionListeners", String.valueOf(actionListeners.size()));
        res.put("stateListeners", String.valueOf(stateListeners.size()));
        res.put("impl dispatcher", Str.toPrettyString(elementImplDispatcher.getInfo()));
        return res;
    }

    private void checkParent(Element parent) throws ElementFactoryException {
        if (!(parent instanceof InternalElement)) {
            throw new ElementFactoryException(format("Parent element '{}' is not accessible expected interface '{}'",
                    parent.getClass().getSimpleName(),
                    InternalElement.class.getSimpleName()), this);
        }
    }

    private Set<Class<? extends Annotation>> getRequirements(ElementConfig elementConfig) {
        return ImmutableSet.<Class<? extends Annotation>>builder().addAll(elementConfig.getRequirements()).addAll(requirements).build();
    }

    private <R extends BaseElement> R fillElement(R element, ElementConfig elementConfig) {
        element.setSelfFactory(this)
                .withParams(elementConfig.getParameters())
                .setName(elementConfig.getName())
                .setDescription(elementConfig.getDescription())
                .setReporter(elementConfig.getReporter())
                .withVerifier(elementConfig.getVerifier())
                .addActionListener(actionListeners);
        element.addStateListener(stateListeners);
        if (elementConfig.getIndex() != -1) {
            element.setIndex(elementConfig.getIndex());
        }
        return element;
    }
}
