package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.Link;
import ru.mk.pump.web.elements.api.concrete.complex.Child;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.web.utils.DownloadHelper;

@SuppressWarnings("unused")
public class LinkImpl extends BaseElement implements Link {

    private static By[] A_BYS = new By[]{By.tagName("a"), By.xpath("./parent::a")};
    private Child<Element> aElement;
    private DownloadHelper downloadHelper;

    public LinkImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public LinkImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public LinkImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    public String getHref() {
        Action<String> action = newDelegateActionFactory().newAction(webElement -> getA().getAttribute("href"), "Get link URL")
                .withStrategy(ActionStrategy.SIMPLE)
                .redefineExpectedState(exists());
        return getActionExecutor().execute(action);
    }

    @Override
    public String download() {
        if (downloadHelper == null) {
            downloadHelper = new DownloadHelper(getBrowser());
        }
        return downloadHelper.download(getHref());
    }

    @Override
    public void checkDownload(String pathOfFileName) {
        throw new UnsupportedOperationException();
    }

    private Element getA() {
        if ("a".equals(getTagName())) {
            return this;
        }
        if (aElement == null) {
            aElement = new Child<>(this, A_BYS);
        }
        return aElement.get(Element.class);
    }
}
