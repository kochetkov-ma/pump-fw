package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.common.api.ItemsManager;
import ru.mk.pump.web.page.PageManager;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ItemManagerException extends AbstractWebException {

    static final String MANAGER = "items manager";

    public ItemManagerException(String message) {
        super(message);
    }

    public ItemManagerException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ItemManagerException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public ItemManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemManagerException withManager(ItemsManager itemsManager) {
        addTarget(MANAGER, itemsManager);
        return this;
    }
}