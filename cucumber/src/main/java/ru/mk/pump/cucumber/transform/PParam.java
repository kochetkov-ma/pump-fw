package ru.mk.pump.cucumber.transform;

import cucumber.api.Transform;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [RUS] Помечается параметер в шаге Cucumber, который будет обрабатываться как выражение Pumpkin с правилами для Grrovy скриптов и тестовых параметров
 * Т.е. будут подставляться параметры из Groovy либо из тестовых параметров.
 * <p/>Не поддерживает коллекции или массивы
 */
@Transform(PumpkinParamTransformer.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface PParam {

}
