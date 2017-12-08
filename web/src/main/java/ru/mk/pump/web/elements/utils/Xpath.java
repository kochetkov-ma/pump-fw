package ru.mk.pump.web.elements.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import ru.mk.pump.commons.utils.Strings;

@SuppressWarnings("WeakerAccess")
@UtilityClass
@Slf4j
public class Xpath {


    public String fixXpath(String xpath) {
        log.debug("[XPATH] source '{}'", xpath);

        if (xpath == null || xpath.matches("[./]*")) {
            return ".";
        }
        /*более двух слэшей*/
        xpath = xpath.replaceAll("/{3,}", "//");
        /*более двух точек*/
        xpath = xpath.replaceAll("\\.{3,}", "\\.\\.");
        /*более одного '/.'*/
        xpath = xpath.replaceAll("(/\\.){2,}", "/.");
        /*более одного './'*/
        xpath = xpath.replaceAll("(/\\.){2,}", "./");



        /*одинокий '/./'*/
        xpath = xpath.replaceAll("(?<![./])/\\./(?![./])", "/");

        /*одинокий '/.'*/
        xpath = xpath.replaceAll("(?<!/)/\\.(?!\\.)", "");
        /*одинокий './'*/
        xpath = xpath.replaceAll("(?<=[\\w/])\\./(?!/)", "");
        /*одинокая точка в конце*/
        xpath = xpath.replaceAll("(?<![./])\\.(?![./])", "");
        /*две одинокие точки в конце*/
        xpath = xpath.replaceAll("(?<!/)(\\.\\.)(?!/)", "/..");
        /*точка в начале слова*/
        xpath = xpath.replaceAll("(?<=[./])\\.(?=\\w)", "./");

        /*одинокий слэш в самом конце*/
        xpath = xpath.replaceAll("(?<!/)/$", "");
        /*одинокие два слэша вконце*/
        xpath = xpath.replaceAll("//$", "");
        /*одинокий './'*/
        xpath = xpath.replaceAll("(?<=[\\w/])\\./(?!/)", "");

        /*точку вначале для конкатенации*/
        if (xpath.startsWith("//") || xpath.startsWith("/")) {
            xpath = "." + xpath;
        } else if (!xpath.startsWith(".")) {
            xpath = "./" + xpath;
        }

        log.debug("[XPATH] result '{}'", xpath);
        return xpath;
    }

    public By fixIfXpath(By by) {
        if (by instanceof ByXPath) {
            final String stringXpath = getXpathStringOrNull((ByXPath) by);
            if (Strings.isEmpty(stringXpath)) {
                return by;
            }
            return By.xpath(fixXpath(stringXpath));
        }
        return by;
    }

    public String concat(String mainXpath, String xpath) {
        return fixXpath(mainXpath) + "/" + fixXpath(xpath);
    }

    private String getXpathStringOrNull(ByXPath by) {
        try {
            return (String) FieldUtils.readField(by, "xpathExpression", true);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
