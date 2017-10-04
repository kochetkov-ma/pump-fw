package ru.mk.pump.commons.reporter;

import java.util.function.Supplier;
import lombok.Getter;

@Getter
public class Attachment {

    private String name;

    private Supplier<byte[]> sourceByte;

    private String source;

    private String type;

    private String extension;

    public Attachment withName(String name) {
        this.name = name;
        return this;
    }

    public Attachment withSource(String source) {
        this.source = source;
        return this;
    }

    public Attachment withType(String type) {
        this.type = type;
        return this;
    }

    public Attachment withSourceByte(Supplier<byte[]> sourceByte) {
        this.sourceByte = sourceByte;
        return this;
    }

    public Attachment withExtension(String extension) {
        this.extension = extension;
        return this;
    }

    @Override
    public String toString() {
        return "Attachment{" +
            "name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", extension='" + extension + '\'' +
            '}';
    }
}
