package ru.mk.pump.web.elements;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;
import ru.mk.pump.web.elements.api.ElementConfig;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.TextArea;
import ru.mk.pump.web.elements.internal.BaseElement;

@Slf4j
public class ElementFactoryTest {

    private Browsers browsers;

    private BrowserConfig config;

    private Browser browser;

    private ElementFactory elementFactory;

    @Before
    public void setUp() {
        browsers = new Browsers();
        config = new BrowserConfig(false, Size.of(true), BrowserType.CHROME);
        config.setWebDriverPath(ProjectResources.findResource("chromedriver.exe").toString());
        browser = browsers.newBrowser(config);

        elementFactory = new ElementFactory(new ElementImplDispatcher(), browser);
    }

    @After
    public void tearDown() throws Exception {
        browsers.close();
    }

    @Test
    public void withMainRequirements() {

    }

    @Test
    public void addActionListener() {
    }

    @Test
    public void withActionListener() {
    }

    @Test
    public void newElement() {
        final ElementConfig config = ElementConfig.of("Тестовый элемент", "Для юнит теста")
            .withParameters(ImmutableMap.of("дополнительные xpath", Parameter.of(By::xpath, By.class).withValue("xpath")));
        final By by = By.tagName("div");
        log.info(Strings.mapToPrettyString(elementFactory.getInfo()));

        final Button button = elementFactory.newElement(Button.class, by, config);
        log.info(((BaseElement) button).getName());

        final DropDown dropDown = elementFactory.newElement(DropDown.class, by, config);
        log.info(((BaseElement) button).getParams().toString());
    }

    @Test
    public void newElement1() {
        final ElementConfig configParent = ElementConfig.of("Тестовый элемент", "Для юнит теста")
            .withParameters(ImmutableMap.of("дополнительные xpath", Parameter.of(By::xpath, By.class).withValue("xpath")));
        final ElementConfig configChild = ElementConfig.of("Тестовый элемент", "Для юнит теста")
            .withParameters(ImmutableMap.of("дополнительные xpath", Parameter.of(By::xpath, By.class).withValue("xpath"),
                "еще параметр", Parameter.of("строка")));

        final By byParent = By.tagName("section");
        final By byChild = By.xpath(".//button[@data-aid='startRegistration']");

        final TextArea parent = elementFactory.newElement(TextArea.class, byParent, configParent.withIndex(1));
        final Button button = elementFactory.newElement(Button.class, byChild, parent, configChild);

        browser.start();
        browser.open("https://app-digitalmortgage003.open.ru/");

        log.info(button.getText());

        log.info(System.lineSeparator(), button.toPrettyString());

    }
}