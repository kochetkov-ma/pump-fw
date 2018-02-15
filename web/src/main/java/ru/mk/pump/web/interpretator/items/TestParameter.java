package ru.mk.pump.web.interpretator.items;

import com.google.common.base.MoreObjects;
import lombok.EqualsAndHashCode;

@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
public final class TestParameter<T> extends AbstractItem<T> {

    public TestParameter(T source) {
        super(source);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("source", this.getSource())
            .toString();
    }
}
