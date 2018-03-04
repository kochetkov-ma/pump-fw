package ru.mk.pump.web.elements.api.part;

import java.nio.file.Path;

@SuppressWarnings({"unused", "WeakerAccess"})
public interface Download {

    String download();

    void checkDownload(String pathOfFileName);
}
