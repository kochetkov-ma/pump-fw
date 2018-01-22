package ru.mk.pump.web.page.api;

import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.Title;
import ru.mk.pump.web.component.api.Component;

public interface Page extends StrictInfo, Component, AppResource {

    void open();

    Browser getBrowser();

    /**
     * @return PageObject name {@link Title#value()}
     */
    String getName();

    /**
     * @return Title from html code
     */
    String getTitle();

    /**
     * @return PageObject description {@link Title#desc()}
     */
    String getDescription();

    /**
     * Page Loader for adding conditions and checking correct page loading result
     * @return {@link PageLoader}
     */
    PageLoader getPageLoader();


    default void check(){
        getPageLoader().checkAdditionalCondition();
        getPageLoader().checkElements();
        getPageLoader().checkUrl();
    }
}
