package ru.mk.pump.web.page.api;

import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.component.api.Component;

public interface Page extends StrictInfo, Component, AppResource {

    void open();

    Browser getBrowser();

    /**
     * @return PageObject name {@link PPage#value()}
     */
    String getName();

    /**
     * @return Static text in element from real page html code
     */
    String getTitle();

    /**
     * @return PageObject description {@link PPage#desc()}
     */
    String getDescription();

    /**
     * PPage Loader for adding conditions and checking correct page loading result
     * @return {@link PageLoader}
     */
    PageLoader getPageLoader();


    default void check(){
        getPageLoader().checkAdditionalCondition();
        getPageLoader().checkElements();
        getPageLoader().checkUrl();
    }
}
