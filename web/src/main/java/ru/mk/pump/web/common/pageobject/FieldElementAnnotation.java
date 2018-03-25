package ru.mk.pump.web.common.pageobject;

import lombok.Getter;

import java.lang.reflect.Field;

public class FieldElementAnnotation extends PumpElementAnnotations {

    @Getter
    private Field field;

    public FieldElementAnnotation(Field field) {
        super(field);
        this.field = field;
    }
}
