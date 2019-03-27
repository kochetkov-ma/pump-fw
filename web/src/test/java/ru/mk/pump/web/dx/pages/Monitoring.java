package ru.mk.pump.web.dx.pages;

import lombok.Getter;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.dx.components.MonitoringContentFrame;
import ru.mk.pump.web.elements.api.Element;

@PPage(value = "Мониторинг")
public class Monitoring extends DxBasePage {

    @Getter
    @PElement("Главный фрейм")
    @FindBy(xpath = "//iframe[contains(@id,'contentIFrame') and contains(@style, 'visible')]")
    private MonitoringContentFrame mainFrame;

    public Monitoring(Browser browser) {
        super(browser);
    }

    @Override
    public Element getTitle() {
        return mainFrame.getDashboardSelector();
    }
}
