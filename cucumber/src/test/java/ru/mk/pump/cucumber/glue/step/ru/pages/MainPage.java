package ru.mk.pump.cucumber.glue.step.ru.pages;

import lombok.NonNull;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.api.concrete.TextArea;
import ru.mk.pump.web.page.BasePage;

import java.util.Optional;

@PPage(value = "Главная страница", desc = "Главная страница", baseUrl = "https://ipotekaonline.open.ru")
public class MainPage extends BasePage {

    @FindBy(tagName = "h2")
    @PElement(value = "Заголовок", desc = "Главный заголовок страницы")
    private TextArea pageTitle;

    @FindBy(id = "CalculatorTabnewId")
    @PElement(value = "Новостройка")
    private Button newApart;

    @FindBy(id = "CalculatorTabusedId")
    @PElement(value = "Вторичное жилье")
    private Button usedApart;

    public MainPage(@NonNull Browser browser) {
        super(browser);
    }

    @Override
    public Element getTitle() {
        return pageTitle;
    }
}
