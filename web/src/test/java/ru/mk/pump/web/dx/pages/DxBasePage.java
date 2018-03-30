package ru.mk.pump.web.dx.pages;

import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PComponent;
import ru.mk.pump.web.dx.DxUrls;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.page.BasePage;

abstract class DxBasePage extends BasePage {

    @PComponent("Продажи")
    @FindBy(id = "TabSFA")
    private Button sales;

    @PComponent("Заявки")
    @FindBy(id = "new_application")
    private Button applications;

    public DxBasePage(Browser browser) {
        super(browser);
    }

    @Override
    public String getBaseUrl() {
        return DxUrls.DX_TEST;
    }
}
