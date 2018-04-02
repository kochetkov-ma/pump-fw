package ru.mk.pump.web.dx.pages;

import java.util.Optional;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.dx.components.ApplicationMainFrame;
import ru.mk.pump.web.elements.api.Element;

@PPage(value = "Заявки")
public class Applications extends DxBasePage {

    @PElement("Главный фрейм")
    @FindBy(xpath = "//iframe[@id='contentIFrame0']")
    private ApplicationMainFrame mainFrame;

    public Applications(Browser browser) {
        super(browser);
    }

    @Override
    public Optional<Element> getTitle() {
        return Optional.of(mainFrame.getActiveApplication());
    }
}
