package ru.mk.pump.web.common.pageobject;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.web.common.api.ParameterTransformer;
import ru.mk.pump.web.common.api.annotations.PComponent;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.elements.api.annotations.Requirements;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class PumpElementAnnotations extends Annotations {

    public PumpElementAnnotations(AnnotatedElement annotatedElement) {
        super(annotatedElement);
    }

    @NonNull
    Parameters buildParameters() {
        final Parameters parameters = Parameters.of();

        for (Annotation annotation : getAnnotatedElement().getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(ParameterTransformerAnnotation.class)) {
                try {
                    final ParameterTransformer parameterBuilder = annotation.annotationType()
                            .getAnnotation(ParameterTransformerAnnotation.class).value()
                            .newInstance();
                    if (parameterBuilder != null) {
                        //noinspection unchecked
                        parameters.addAll(parameterBuilder.transform(annotation));
                    } else {
                        log.error("Cannot find parameter transformer for annotation '{}' and annotated element '{}'", annotation, getAnnotatedElement());
                    }
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException(
                            String.format("Error when try to create transformer '%s' for annotation '%s' and annotated element '%s'", annotation.annotationType()
                                    .getAnnotation(ParameterTransformerAnnotation.class).value().getClass().getSimpleName(), annotation, getAnnotatedElement()));
                }
            }
        }
        return parameters;
    }

    public @NonNull String getComponentName() {
        if (getAnnotatedElement().isAnnotationPresent(PComponent.class)) {
            return getAnnotatedElement().getAnnotation(PComponent.class).value();
        }
        return "";
    }

    public @NonNull String getComponentDescription() {
        if (getAnnotatedElement().isAnnotationPresent(PComponent.class)) {
            return getAnnotatedElement().getAnnotation(PComponent.class).desc();
        }
        return "";
    }

    public @NonNull
    String getPageName() {
        if (getAnnotatedElement().isAnnotationPresent(PPage.class)) {
            return getAnnotatedElement().getAnnotation(PPage.class).value();
        }
        return "";
    }

    public @NonNull String getPageDescription() {
        if (getAnnotatedElement().isAnnotationPresent(PPage.class)) {
            return getAnnotatedElement().getAnnotation(PPage.class).desc();
        }
        return "";
    }

    public @NonNull String getPageBaseUrl() {
        if (getAnnotatedElement().isAnnotationPresent(PPage.class)) {
            return getAnnotatedElement().getAnnotation(PPage.class).baseUrl();
        }
        return "";
    }

    public @NonNull String getPageResource() {
        if (getAnnotatedElement().isAnnotationPresent(PPage.class)) {
            return getAnnotatedElement().getAnnotation(PPage.class).resource();
        }
        return "";
    }

    public @NonNull String[] getPageExtraUrls() {
        if (getAnnotatedElement().isAnnotationPresent(PPage.class)) {
            return getAnnotatedElement().getAnnotation(PPage.class).extraUrls();
        }
        return new String[]{};
    }

    public @NonNull String getElementName() {
        if (getAnnotatedElement().isAnnotationPresent(PElement.class)) {
            return getAnnotatedElement().getAnnotation(PElement.class).value();
        }
        return "";
    }

    public @NonNull String getElementDescription() {
        if (getAnnotatedElement().isAnnotationPresent(PElement.class)) {
            return getAnnotatedElement().getAnnotation(PElement.class).desc();
        }
        return "";
    }

    public @NonNull Collection<Class<? extends Annotation>> getRequirements() {
        return Arrays.stream(getAnnotatedElement().getAnnotations())
                .map(Annotation::getClass)
                .filter(a -> a.isAnnotationPresent(Requirements.class))
                .collect(Collectors.toList());

    }
}