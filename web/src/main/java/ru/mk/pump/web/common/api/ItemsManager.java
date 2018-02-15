package ru.mk.pump.web.common.api;

import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.NonNull;
import ru.mk.pump.commons.interfaces.StrictInfo;

public interface ItemsManager<T> extends StrictInfo {

    @Nullable
    T getCurrent();

    @NonNull
    List<? extends T> getCurrentList();

    @NonNull
    T getOne(String name);

    @NonNull
    List<T> getList(String name);

    @NonNull
    <V extends T> V getOneByClass(String name, Class<V> itemClass);

    @NonNull
    <V extends T> List<V> getListByClass(String name, Class<V> itemClass);

    @NonNull <V extends T> Set<Class<V>> find(@NonNull String name, @NonNull Class<V> itemClass);
}
