package ru.mk.pump.web.dx.components;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.component.BaseFrame;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

public class ApplicationMainFrame extends BaseFrame {

    @Getter
    @PElement("Мои активные Заявки")
    @FindBy(xpath = "//span[text()='Мои активные Заявки']")
    private Button activeApplication;

    //region CONSTRUCTORS
    public ApplicationMainFrame(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public ApplicationMainFrame(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public ApplicationMainFrame(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }
    //endregion
}
