package ru.mk.pump.web.interpretator.items;

import com.google.common.base.MoreObjects;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@SuppressWarnings("unused")
@EqualsAndHashCode
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
            .add("source", this.getSource())
            .add("index", index)
            .toString();
    }
}