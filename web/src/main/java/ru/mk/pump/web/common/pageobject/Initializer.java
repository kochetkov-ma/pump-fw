package ru.mk.pump.web.common.pageobject;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.mk.pump.commons.utils.ReflectionUtils;
import ru.mk.pump.web.component.ComponentList;
import ru.mk.pump.web.component.api.Component;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementList;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.ElementConfig;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.page.api.Page;

/**
 * [RUS] Инициализация полей компонента (PPage Object).
 * <p>Алгоритм инициализации</p>
 * <ul>
 *     <li>корневой компонент запускает инициализацию своих полей посредством {@link org.openqa.selenium.support.PageFactory}. Корневым компонентом может быть как страница, так и компонент</li>
 *     <li>анализируется каждое <b>НЕ final</b> поле класса и <b>всех его подклассов</li>
 *     <li>(ограничение) среди <b>НЕ final</b> полей инициализируемого компонента не должно быть класса {@link Page} т.к. {@link ElementFactory} используемый для создания экземляров, не поддерживает {@link Page}</li>
 *     <li>если поле является одиночным компонентом, то с помощью componentFactory создается объект компонента и рекурсивно вызывается инициализация его полей</li>
 *     <li>если поле является списком компонентов, то создается {@link ComponentList}, который по вызову метода get(int) создает, рекурсивно инициализирует поля созданного компонента и возвращает его</li>
 *     <li>если поле является одиночным элементом, то с помощью elementFactory создается объект элемента</li>
 *     <li>если поле является списком элементов, то создается {@link ElementList}, который по вызову метода get(int) создает и возвращает элемент</li>
 *     <li>иначе null, т.е. поле пропускается в {@link org.openqa.selenium.support.PageFactory} и остается установленное ранее пользователем значение либо значение по умолчанию</li>
 * </ul>
 *
 * Компонент реализовывает интерфейс {@link Component}
 * Корневой объект для иницализации может быть любым подтипом {@link Component}
 * Есть возможность задать дополнительную фильтрацию по типам полей с помощью {@link #withClassFilter(Class)}
 */
@SuppressWarnings("WeakerAccess")
public class Initializer implements FieldDecorator {

    @Getter
    private final ElementFactory elementFactory;

    @Getter
    private final ElementFactory componentFactory;

    private final BaseElement parent;

    private Class<? extends Element> initClass;

    /**
     * Details in the type description {@link Initializer}
     */
    public Initializer(@NotNull ElementFactory elementFactory, @NotNull ElementFactory componentFactory) {
        this(elementFactory, componentFactory, null);
    }

    /**
     * Details in the type description {@link Initializer}
     */
    public Initializer(@NotNull ElementFactory elementFactory, @NotNull ElementFactory componentFactory, @Nullable BaseElement parent) {
        this.elementFactory = elementFactory;
        this.componentFactory = componentFactory;
        this.parent = parent;
    }

    /**
     * Details in the type description {@link Initializer}
     */
    public Initializer withClassFilter(@Nullable Class<? extends Element> initClass) {
        this.initClass = initClass;
        return this;
    }

    /**
     * Details in the type description {@link Initializer}
     */
    @Override
    public Object decorate(@NotNull ClassLoader loader, @NotNull Field field) {
        final PumpElementAnnotations pumpElementAnnotations = new PumpElementAnnotations(field);
        final ElementConfig elementConfig = annotationsToElementConfig(pumpElementAnnotations);
        final By elementBy = pumpElementAnnotations.buildBy();

        if (isSingleComponent(field)) {
            final Element element;
            if (parent == null) {
                //noinspection unchecked
                element = componentFactory.newElement((Class<? extends Element>) field.getType(), elementBy, elementConfig);
            } else {
                //noinspection unchecked
                element = componentFactory.newElement((Class<? extends Element>) field.getType(), elementBy, parent, elementConfig);
            }
            if (initClass == null) {
                ((Component) element).initAllElements();
            } else {
                ((Component) element).initElementsByClass(initClass);
            }
            return element;
        }

        if (isListComponent(field)) {
            return new ComponentList<>(getGenericElementClass(field), elementBy, parent, componentFactory, elementConfig).withInitFilter(initClass);
        }

        if (isSingleElement(field)) {
            final Element element;
            if (parent == null) {
                //noinspection unchecked
                element = elementFactory.newElement((Class<? extends Element>) field.getType(), elementBy, elementConfig);
            } else {
                //noinspection unchecked
                element = elementFactory.newElement((Class<? extends Element>) field.getType(), elementBy, parent, elementConfig);
            }
            return element;
        }

        if (isListElement(field)) {
            return new ElementList<>(getGenericElementClass(field), elementBy, parent, elementFactory, elementConfig);
        }

        return null;
    }

    /**
     * Convert field annotation to {@link ElementConfig} for using in {@link ElementFactory}
     * @param pumpElementAnnotations filed wrapper for extract rules parameters
     */
    protected ElementConfig annotationsToElementConfig(@NotNull PumpElementAnnotations pumpElementAnnotations) {
        return ElementConfig.of(pumpElementAnnotations.getName(), pumpElementAnnotations.getDescription())
            .withParameters(pumpElementAnnotations.buildParameters())
            .withRequirements(pumpElementAnnotations.getRequirements());
    }

    //region PRIVATE
    private boolean isNotFinal(Field field) {
        return !Modifier.isFinal(field.getModifiers());
    }

    private boolean isSuccessFiltered(Class<?> clazz) {
        return initClass == null || initClass.isAssignableFrom(clazz);
    }

    private boolean isSingleElement(Field field) {
        return Element.class.isAssignableFrom(field.getType())
            && isSuccessFiltered(field.getType())
            && isNotFinal(field);
    }

    private boolean isSingleComponent(Field field) {
        return Component.class.isAssignableFrom(field.getType())
            && isSuccessFiltered(field.getType())
            && isNotFinal(field);
    }

    private boolean isListElement(Field field) {
        return List.class.isAssignableFrom(field.getType())
            && Element.class.isAssignableFrom(ReflectionUtils.getGenericParameterField(field, 0))
            && isSuccessFiltered(field.getType())
            && isNotFinal(field);
    }

    private boolean isListComponent(Field field) {
        return List.class.isAssignableFrom(field.getType())
            && Component.class.isAssignableFrom(ReflectionUtils.getGenericParameterField(field, 0))
            && isSuccessFiltered(field.getType())
            && isNotFinal(field);
    }

    private <T extends Element> Class<T> getGenericElementClass(Field field) {
        try {
            //noinspection unchecked
            return (Class<T>) ReflectionUtils.getGenericParameterField(field, 0);
        } catch (ClassCastException ex) {
            throw new InternalError("Pump-fw internal error. Unexpectedly using private method without required checks in Initializer. Contact to developer",
                ex);
        }
    }
    //endregion
}
