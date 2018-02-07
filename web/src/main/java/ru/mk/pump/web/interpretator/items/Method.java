package ru.mk.pump.web.interpretator.items;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
public final class Method extends AbstractItem<String> {

    @Getter
    @Setter
    private boolean nonArg;

    @Setter
    private List<Object> args = Lists.newArrayList();

    public Method(String source) {
        super(source);
    }

    public boolean hasArgs() {
        return !args.isEmpty();
    }

    public Object[] getArgs() {
        return args.toArray();
    }

    public Method addArg(Object arg) {
        args.add(arg);
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("nonArg", nonArg)
            .add("args", args)
            .add("source", this.value())
            .toString();
    }
}
