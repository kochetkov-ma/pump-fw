package ru.mk.pump.commons.utils;

import com.google.common.base.MoreObjects;
import lombok.Getter;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Walker {

    private final StringBuilder left;

    private final StringBuilder right;

    @Getter
    private final String source;

    public Walker(String source) {
        this.source = source;
        this.right = new StringBuilder(source);
        this.left = new StringBuilder("");
    }

    public boolean hasNext() {
        return right.length() > 0;
    }

    public String next() {
        String res = right.substring(0, 1);
        left.append(res);
        right.deleteCharAt(0);
        return res;
    }

    public String getLeft() {
        return left.toString();
    }

    public String getRight() {
        return right.toString();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("left", left)
            .add("right", getRight())
            .add("source", getLeft())
            .toString();
    }
}
