package ru.mk.pump.web.elements.internal;

import java.util.Optional;
import org.openqa.selenium.By;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.Action;
import ru.mk.pump.web.elements.State;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.page.Page;

public interface InternalElement extends Element, ElementAction, ElementState {

    WaitResult<Boolean> wait(State elementState);

    <T> WaitResult<T> wait(Action<T> elementState);

    String getName();

    By getBy();

    Optional<Page> getPage();

    Optional<InternalElement> getParent();

    String text();

    boolean isReady();

    void checkIsReady();

    boolean isList();

    int getIndex();

    InternalElement setIndex(int index);

    Browser getBrowser();

    Finder getFinder();

    Waiter getWaiter();
}
