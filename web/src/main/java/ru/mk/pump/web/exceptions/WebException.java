package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;
import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.common.api.ItemsManager;
import ru.mk.pump.web.common.api.PageItemImplDispatcher;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.Finder;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.page.api.Page;

import javax.annotation.Nullable;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
@NoArgsConstructor
public class WebException extends PumpException {

    private static final String BROWSER = "browser";

    private static final String PAGE = "page";

    private static final String ACTION = "action";

    private static final String DISPATCHER = "element_dispatcher";

    private static final String ELEMENT = "element";

    private static final String PARENT = "parent_element";

    private static final String FACTORY = "element_factory";

    private static final String FINDER = "finder";

    private static final String STATE = "state";

    private static final String MANAGER = "items manager";

    //region Construction
    public WebException(@Nullable String title) {
        super(title);
    }

    public WebException(@Nullable String title, @Nullable Throwable cause) {
        super(title, cause);
    }

    public WebException(@NonNull PumpMessage pumpMessage) {
        super(pumpMessage);
    }

    public WebException(@NonNull PumpMessage pumpMessage, @Nullable Throwable cause) {
        super(pumpMessage, cause);
    }

    public WebException(@NonNull PumpMessage pumpMessage, @NonNull Map<String, StrictInfo> extra,
            @NonNull Map<String, StrictInfo> env, @Nullable Throwable cause) {
        super(pumpMessage, extra, env, cause);
    }
    //endregion

    /**
     * Add : Browser.
     */
    protected WebException withBrowser(@Nullable Browser browser) {
        if (browser == null) {
            return this;
        }
        return (WebException) withExtra(BROWSER, browser);
    }

    /**
     * Add : Page + Browser.
     */
    protected WebException withPage(@Nullable Page page) {
        if (page == null) {
            return this;
        }
        return ((WebException) withExtra(PAGE, page))
                .withBrowser(page.getBrowser());
    }

    /**
     * Add : Action + Page + Browser.
     */
    protected WebException withAction(@Nullable Action action) {
        if (action == null) {
            return this;
        }
        return ((WebException) withExtra(ACTION, action))
                .withPage(action.getTarget().getPage())
                .withBrowser(action.getTarget().getBrowser());
    }

    /**
     * Add : PageItemImplDispatcher
     */
    protected WebException withEnvDispatcher(@Nullable PageItemImplDispatcher dispatcher) {
        if (dispatcher == null) {
            return this;
        }
        return (WebException) withEnv(DISPATCHER, dispatcher);
    }

    /**
     * Add : PageItemImplDispatcher
     */
    protected WebException withDispatcher(@Nullable PageItemImplDispatcher dispatcher) {
        if (dispatcher == null) {
            return this;
        }
        return (WebException) withExtra(DISPATCHER, dispatcher);
    }

    /**
     * Add : Element + Page + Browser
     */
    @SuppressWarnings("Duplicates")
    protected WebException withElement(@Nullable Element element) {
        if (element == null) {
            return this;
        }
        val res = (WebException) withExtra(ELEMENT, element);
        if (element.advanced() == null) {
            return this;
        }
        return res.withPage(element.advanced().getPage());
    }

    /**
     * Add : Element + Page + Browser
     */
    @SuppressWarnings("Duplicates")
    protected WebException withParent(@Nullable Element parent) {
        if (parent == null) {
            return this;
        }
        val res = (WebException) withExtra(PARENT, parent);
        if (parent.advanced() == null) {
            return this;
        }
        return res.withPage(parent.advanced().getPage());
    }

    /**
     * Add : Factory + PageItemImplDispatcher + Browser + Page
     */
    protected WebException withFactory(@Nullable ElementFactory elementFactory) {
        if (elementFactory == null) {
            return this;
        }
        return ((WebException) withExtra(FACTORY, elementFactory))
                .withDispatcher(elementFactory.getElementImplDispatcher())
                .withBrowser(elementFactory.getBrowser())
                .withPage(elementFactory.getPage());
    }

    /**
     * Add : Finder + Element + Page + Browser
     */
    protected WebException withFinder(@Nullable Finder finder) {
        if (finder == null) {
            return this;
        }
        return (WebException) withExtra(FINDER, finder).withExtra(ELEMENT, finder.getMainElement());
    }

    /**
     * Add : State
     */
    protected WebException withState(@Nullable State state) {
        if (state == null) {
            return this;
        }
        return (WebException) withExtra(STATE, state);
    }

    /**
     * Add : ItemsManager
     */
    protected WebException withManager(@Nullable ItemsManager manager) {
        if (manager == null) {
            return this;
        }
        return ((WebException) withExtra(MANAGER, manager));
    }
}