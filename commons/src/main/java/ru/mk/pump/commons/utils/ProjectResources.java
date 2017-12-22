package ru.mk.pump.commons.utils;

import static java.lang.String.format;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.exception.UtilException;

/**
 * ONLY IN CALLER PROJECT DIRS
 */
@SuppressWarnings("unused")
@UtilityClass
@Slf4j
public final class ProjectResources {

    public Path getBuildDir(@NotNull Class relatedClass) {
        try {
            return moveToBuildRoot(Paths.get(relatedClass.getProtectionDomain().getCodeSource().getLocation().toURI()));
        } catch (URISyntaxException e) {
            throw new UtilException(format("Cannot get build folder for class '%s'", relatedClass), e);
        }
    }

    private Path moveToBuildRoot(Path innerProjectPath) {
        final String pDir = System.getProperty("user.dir");
        if (pDir == null) {
            log.error("RESOURCES ERROR : Cannot get system property 'user.dir'");
            return innerProjectPath;
        }
        final Path buildDir = Paths.get(pDir);
        Path result = innerProjectPath.getParent();
        while (result != null && !buildDir.equals(result.getParent())) {
            result = result.getParent();
        }
        if (result == null) {
            throw new UtilException("Cannot get build Path from " + innerProjectPath);
        }
        return result;

    }

    @NotNull
    public Path getResourcesDir() {
        final URL url = ProjectResources.class.getClassLoader().getResource("");
        if (url == null) {
            throw new UtilException("ProjectResources folder is null");
        }
        try {
            return moveToBuildRoot(Paths.get(url.toURI()));
        } catch (URISyntaxException e) {
            throw new UtilException("Cannot get resources folder", e);
        }
    }

    public List<Path> findResourceFiles(@NotNull String dirName, @NotNull String fileName, int depth) {
        return FileUtils.findFiles(FileUtils.findDir(getResourcesDir(), dirName, depth), fileName, depth);
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
