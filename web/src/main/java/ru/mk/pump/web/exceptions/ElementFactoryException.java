package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.api.Element;

import javax.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor
public class ElementFactoryException extends WebException {

    static final String PARENT = "parent_element";

    static final String FACTORY = "element_factory";

    public ElementFactoryException(@Nullable String title, @Nullable ElementFactory elementFactory) {
        this(title, elementFactory, null);
    }

    public ElementFactoryException(@Nullable String title, @Nullable ElementFactory elementFactory, @Nullable Throwable cause) {
        super(title, cause);
        withFactory(elementFactory);
    }

    @Override
    public ElementFactoryException withFactory(@Nullable ElementFactory elementFactory) {
        return (ElementFactoryException) super.withFactory(elementFactory);
    }

    @Override
    public ElementFactoryException withParent(@Nullable Element parent) {
        return (ElementFactoryException) super.withParent(parent);
    }
}