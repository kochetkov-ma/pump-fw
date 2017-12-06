package ru.mk.pump.web.elements.internal;

import com.google.common.collect.ImmutableMap;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.elements.api.ElementConfig;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.api.concrete.TextArea;

@Slf4j
public class SubElementHelperTest {

    private Browsers browsers;

    private TextArea parent;

    @Before
    public void setUp() {
        browsers = new Browsers();
        BrowserConfig config = new BrowserConfig(false, Size.of(true), BrowserType.CHROME);
        config.setWebDriverPath(ProjectResources.findResource("chromedriver.exe").toString());
        Browser browser = browsers.newBrowser(config);

        ElementFactory elementFactory = new ElementFactory(new ElementImplDispatcher(), browser);

        final ElementConfig configParent = ElementConfig.of("Тестовый элемент", "Для юнит теста")
            .withParameters(ImmutableMap.of("дополнительные xpath", Parameter.of(By::xpath, By.class).withValue("xpath")));

        final By byParent = By.tagName("section");

        parent = elementFactory.newElement(TextArea.class, byParent, configParent.withIndex(1));

        browser.start();
        browser.open("https://app-digitalmortgage003.open.ru/");


    }

    @Test
    public void find() {

        parent.isDisplayed();
        final By byChild = By.xpath(".//button[@data-aid='startRegistration']");
        log.info(parent.getSubElements(Button.class).find(byChild).getText());

    }

    @Test
    public void findList() {

        parent.isDisplayed();

        final By byChild = By.xpath(".//button");

        parent.getSubElements(Button.class).findList(byChild).forEach(i -> log.info(i.getTextHidden()));

    }

    @Test
    public void findListXpathAdvanced() {

        parent.isDisplayed();

        final Predicate<WebElement> pred2 = (el) -> "startRegistration".equals(el.getAttribute("data-aid"));

        final Predicate<WebElement> pred1 = (el) -> "button".equals(el.getTagName());

        parent.getSubElements(Button.class).findListXpathAdvanced(".//div", pred1, "/button").forEach(i -> log.info(i.getTextHidden()));
        parent.getSubElements(Button.class).findListXpathAdvanced(".//div", pred2, "/button", "/button[text()='Подать заявку']")
            .forEach(i -> log.info(i.getTextHidden()));
    }

    @Test
    public void findXpathAdvanced() {
        parent.isDisplayed();

        final Predicate<WebElement> pred = (el) -> "button".equals(el.getTagName());

        log.info(parent.getSubElements(Button.class).findXpathAdvanced(".//div", pred, "/button", "/button[text()='Подать заявку']").getText());
    }

    @After
    public void tearDown() {
        browsers.close();
    }

}