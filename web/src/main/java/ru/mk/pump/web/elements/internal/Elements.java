package ru.mk.pump.web.elements.internal;

import java.util.Map;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.web.common.Parameter;
import ru.mk.pump.web.elements.api.Element;

@UtilityClass
public class Elements {

    private boolean init = false;

    public <T extends Element> T findImplementation(@NotNull Class<T> elementInterface, @Nullable Map<String, Parameter> additionalParams){
        return null;
    }

    public <T extends Element> Elements addImplementation(@NotNull Class<T> elementInterface, @NotNull Class<T> elementImplementation){
        return null;
    }


}
