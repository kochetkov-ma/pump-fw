package ru.mk.pump.web.interpretator.items;

import com.google.common.base.MoreObjects;
import lombok.Getter;

@SuppressWarnings("unused")
public final class Field extends AbstractItem<String> {

    @Getter
    private int index = -1;

    public Field(String source) {
        super(source);
    }

    public Field setIndex(int index) {
        this.index = index;
        return this;
    }

    public boolean hasIndex() {
        return index > -1;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("source", this.value())
            .add("index", index)
            .toString();
    }
}