package ru.mk.pump.commons.utils;

import static java.lang.String.format;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.exception.UtilException;

/**
 * ONLY IN CALLER PROJECT DIRS
 */
@UtilityClass
public final class Resources {

    public Path getBuildDir(@NotNull Class relatedClass) {
        try {
            return Paths.get(relatedClass.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (URISyntaxException e) {
            throw new UtilException(format("Cannot get build folder for class '%s'", relatedClass), e);
        }
    }

    @NotNull
    public Path getResourcesDir() {
        final URL url = Resources.class.getClassLoader().getResource("");
        if (url == null) {
            throw new UtilException("Resources folder is null");
        }
        try {
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new UtilException("Cannot get resources folder", e);
        }
    }

    public List<Path> findResourceFiles(@NotNull String dirName, @NotNull String fileName, int depth) {
        return FileUtils.findFiles(FileUtils.resolveDir(getResourcesDir(), dirName), fileName, depth);
    }

    public Path findResource(@NotNull String fileName) {
        return findResourceFiles("", fileName, 5).stream().findFirst()
            .orElseThrow(() -> new UtilException(format("Cannot find resource file '%s'", fileName)));
    }

    public Path findResourceFile(@NotNull String dirName, @NotNull String fileName) {
        return findResourceFiles(dirName, fileName, 1).stream().findFirst()
            .orElseThrow(() -> new UtilException(format("Cannot find resource files '%s' in dir '%s'", fileName, dirName)));
    }

    public static Path findFileInBuildDir(@NotNull Class relatedClass, @NotNull String fileName) {
        return FileUtils.findFiles(getBuildDir(relatedClass), fileName, 5).stream().findFirst()
            .orElseThrow(() -> new UtilException(format("Cannot find resource files '%s' in build dir", fileName)));
    }
}
