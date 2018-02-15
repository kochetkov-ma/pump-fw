package ru.mk.pump.web.common.pageobject;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import org.openqa.selenium.By;
import org.openqa.selenium.support.AbstractFindByBuilder;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactoryFinder;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Annotations extends AbstractAnnotations {
    private AnnotatedElement annotatedElement;

    public Annotations(AnnotatedElement annotatedElement) {
        this.annotatedElement = annotatedElement;
    }

    /**
     * {@inheritDoc}
     *
     * @return true if @CacheLookup annotation exists on a annotatedElement
     */
    public boolean isLookupCached() {
        return (annotatedElement.getAnnotation(CacheLookup.class) != null);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Looks for one of {@link org.openqa.selenium.support.FindBy},
     * {@link org.openqa.selenium.support.FindBys} or
     * {@link org.openqa.selenium.support.FindAll} annotatedElement annotations. In case
     * no annotations provided for annotatedElement, uses annotatedElement name as 'id' or 'name'.
     *
     * @throws IllegalArgumentException when more than one annotation on a annotatedElement provided
     */
    public By buildBy() {
        assertValidAnnotations();

        By ans = null;

        for (Annotation annotation : annotatedElement.getDeclaredAnnotations()) {
            AbstractFindByBuilder builder = null;
            if (annotation.annotationType().isAnnotationPresent(PageFactoryFinder.class)) {
                try {
                    builder = annotation.annotationType()
                            .getAnnotation(PageFactoryFinder.class).value()
                            .newInstance();
                } catch (ReflectiveOperationException e) {
                    // Fall through.
                }
            }
            if (builder != null) {
                if (getAnnotatedElement() instanceof Field) {
                    ans = builder.buildIt(annotation, ((Field) annotatedElement));
                } else {
                    ans = builder.buildIt(annotation, null);
                }
                break;
            }
        }

        if (ans == null) {
            ans = buildByFromDefault();
        }

        if (ans == null) {
            throw new IllegalArgumentException("Cannot determine how to locate element " + annotatedElement);
        }

        return ans;
    }

    protected AnnotatedElement getAnnotatedElement() {
        return annotatedElement;
    }

    protected By buildByFromDefault() {
        if (getAnnotatedElement() instanceof Member) {
            return new ByIdOrName(((Member) annotatedElement).getName());
        } else {
            return null;
        }
    }

    protected void assertValidAnnotations() {
        FindBys findBys = annotatedElement.getAnnotation(FindBys.class);
        FindAll findAll = annotatedElement.getAnnotation(FindAll.class);
        FindBy findBy = annotatedElement.getAnnotation(FindBy.class);
        if (findBys != null && findBy != null) {
            throw new IllegalArgumentException("If you use a '@FindBys' annotation, " +
                    "you must not also use a '@FindBy' annotation");
        }
        if (findAll != null && findBy != null) {
            throw new IllegalArgumentException("If you use a '@FindAll' annotation, " +
                    "you must not also use a '@FindBy' annotation");
        }
        if (findAll != null && findBys != null) {
            throw new IllegalArgumentException("If you use a '@FindAll' annotation, " +
                    "you must not also use a '@FindBys' annotation");
        }
    }
}