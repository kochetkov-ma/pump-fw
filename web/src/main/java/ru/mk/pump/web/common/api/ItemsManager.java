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
    List<? extends T> getCurrentList();

    @NotNull
    T getOne(String name);

    @NotNull
    List<T> getList(String name);

    @NotNull
    <V extends T> V getOneByClass(String name, Class<V> itemClass);

    @NotNull
    <V extends T> List<V> getListByClass(String name, Class<V> itemClass);

    @NotNull <V extends T> Set<Class<V>> find(@NotNull String name, @NotNull Class<V> itemClass);
}
