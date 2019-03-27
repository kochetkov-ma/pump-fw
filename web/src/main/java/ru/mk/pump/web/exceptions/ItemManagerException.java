package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;
import ru.mk.pump.web.common.api.ItemsManager;

import javax.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor
public class ItemManagerException extends WebException {

    public ItemManagerException(@Nullable String title, @Nullable ItemsManager manager) {
        this(title, manager, null);
    }

    public ItemManagerException(@Nullable String title, @Nullable ItemsManager manager, @Nullable Throwable cause) {
        super(title, cause);
        withManager(manager);
    }

    @Override
    public ItemManagerException withManager(@Nullable ItemsManager manager) {
        return (ItemManagerException) super.withManager(manager);
    }
}