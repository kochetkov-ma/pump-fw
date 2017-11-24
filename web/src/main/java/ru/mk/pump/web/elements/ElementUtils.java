package ru.mk.pump.web.elements;

import java.util.Optional;
import java.util.function.Predicate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.Finder;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementException;

public class ElementUtils {

    public Optional<By> xpathSmartDiscovery(InternalElement element, Predicate<WebElement> webElementPredicate, String ... postfixXpaths){
        final By old = element.getBy();
        By byResult = element.getBy();
        if (byResult instanceof ByXPath){
            WaitResult<WebElement> waitResult = element.getFinder().findFast();
            if (!waitResult.isSuccess() || !webElementPredicate.test(waitResult.getResult())){
                try {
                    final String xpath = (String)FieldUtils.readField(byResult, "xpathExpression", true);
                    for (String newPath : postfixXpaths){
                        element. By.xpath(xpath + newPath);
                        waitResult = element.getFinder().findFast();
                        if (!waitResult.isSuccess() || !webElementPredicate.test(waitResult.getResult())){

                        }
                    }

                } catch (IllegalAccessException e) {
                    throw new ElementException("Error when reading 'xpathExpression' field", element);
                }
            }
        }
        return byResult;

    }

}
