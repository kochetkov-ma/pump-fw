package ru.mk.pump.web.dx.pages;

import java.util.Optional;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.Button;

@PPage(value = "Заявки")
public class Applications extends DxBasePage {

    @PElement("Мои активные Заявки")
    @FindBy(xpath = "//span[text()='Мои активные Заявки']")
    private Button activeApplication;

    public Applications(Browser browser) {
        super(browser);
    }

    @Override
    public Optional<Element> getTitle() {
        return Optional.of(activeApplication);
    }
}
