package ru.mk.pump.web.elements.utils;

import java.util.Arrays;
import java.util.regex.Pattern;
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

    private final static Pattern[] PATTERN_TO_DEL = {Pattern.compile("/{3,}"),
        Pattern.compile("\\.{3,}"),
        Pattern.compile("([^.]\\./)+$"),
        Pattern.compile("(/\\.[^.])+$"),
        Pattern.compile("(/\\.)+$"),
        Pattern.compile("/+$")};

    private final static Pattern[] PATTERN_TO_DOUBLE_SLASH = {Pattern.compile("/{3,}"),
        Pattern.compile("([^.]\\.//)+$"),
        Pattern.compile("([^/]/\\.//)+$")};

    private final static Pattern[] PATTERN_TO_DOUBLE_DOT = {Pattern.compile("\\.\\.\\.{3,}")};

    public String fixXpath(String xpath) {
        xpath = Arrays.stream(PATTERN_TO_DOUBLE_SLASH)
            .reduce(xpath, (str, p) -> str.replaceAll(p.pattern(), "//"), (str1, str2) -> str2);
        xpath = Arrays.stream(PATTERN_TO_DOUBLE_DOT)
            .reduce(xpath, (str, p) -> str.replaceAll(p.pattern(), ".."), (str1, str2) -> str2);
        xpath = Arrays.stream(PATTERN_TO_DEL)
            .reduce(xpath, (str, p) -> str.replaceAll(p.pattern(), ""), (str1, str2) -> str2);
        xpath = Arrays.stream(PATTERN_TO_DEL)
            .reduce(xpath, (str, p) -> str.replaceAll(p.pattern(), ""), (str1, str2) -> str2);

        if (xpath.startsWith("./") || xpath.startsWith(".//")) {
            return xpath;
        }
        if (xpath.startsWith("//")) {
            return "." + xpath;
        }
        if (xpath.startsWith("/")) {
            return "." + xpath;
        }
        if (!xpath.startsWith(".")) {
            return "./" + xpath;
        }
        if (xpath.startsWith(".")) {
            return xpath.replaceFirst("\\.", "./");
        }
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

    private String getXpathStringOrNull(ByXPath by) {
        try {
            return (String) FieldUtils.readField(by, "xpathExpression", true);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
