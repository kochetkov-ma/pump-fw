package ru.mk.pump.web.page.api;

import java.util.Optional;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.WebObject;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.component.api.Component;
import ru.mk.pump.web.elements.api.Element;

public interface Page extends StrictInfo, Component, AppResource {

    void open();

    Browser getBrowser();

    /**
     * @return PageObject name {@link PPage#value()}
     */
    String getName();

    /**
     * @return Static text in element from real page html code. I should override this in concrete page
     */
    Optional<Element> getTitle();

    /**
     * @return PageObject description {@link PPage#desc()}
     */
    String getDescription();

    /**
     * PPage Loader for adding conditions and checking correct page loading result
     *
     * @return {@link PageLoader}
     */
    PageLoader getPageLoader();
}
