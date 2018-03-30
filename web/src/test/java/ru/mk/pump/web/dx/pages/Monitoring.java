package ru.mk.pump.web.dx.pages;

import java.util.Optional;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.Button;

@PPage(value = "Мониторинг")
public class Monitoring extends DxBasePage {

    @FindBy(id = "dashboardSelectorLink")
    private Button monitoringTitleButton;

    public Monitoring(Browser browser) {
        super(browser);
    }

    @Override
    public Optional<Element> getTitle() {
        return Optional.of(monitoringTitleButton);
    }
}
