package ru.mk.pump.commons.reporter;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.Wither;
import lombok.val;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.utils.Str;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * Immutable attachment. New instance on every setter. Every field can be null.
 */
@SuppressWarnings("WeakerAccess")
@Value
@Wither
@Accessors(chain = true)
@Immutable
@Nullable
@AllArgsConstructor
public class Attachment implements PrettyPrinter {
    @Nullable private final String name;
    @Nullable private final String source;
    @Nullable private final Supplier<byte[]> sourceByte;
    @Nullable private final String type;
    @Nullable private final String extension;

    public Attachment() {
        name = null;
        sourceByte = null;
        source = null;
        type = null;
        extension = null;
    }

    @Override
    public String toPrettyString() {
        return format("Attachment[Name: '%s' Source: '%s' Bytes size: '%d' Type: '%s' Extension: '%s']",
                Str.toString(name),
                Str.toString(source),
                byteSize(),
                Str.toString(type),
                Str.toString(extension));
    }

    private int byteSize() {
        val b = sourceByte != null ? sourceByte.get() : null;
        return b != null ? b.length : 0;

    }
}