package ru.mk.pump.web.common.api;

import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.interfaces.StrictInfo;

public interface ItemsManager<T> extends StrictInfo {

    @Nullable
    T getCurrent();

    @NotNull
    List<T> getCurrentList();

    @NotNull
    T getOne(String name);

    @NotNull
    List<T> getList(String name);

    @NotNull
    <V extends T> T getOneByClass(String name, Class<V> itemClass);

    @NotNull
    <V extends T> List<T> getListByClass(String name, Class<V> itemClass);

    @NotNull <V extends T> Set<Class<V>> find(@NotNull String name, @NotNull Class<V> itemClass);
}
