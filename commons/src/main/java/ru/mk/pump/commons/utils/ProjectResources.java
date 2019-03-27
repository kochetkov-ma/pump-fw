package ru.mk.pump.commons.utils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.exception.UtilException;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static java.lang.String.format;

@SuppressWarnings({"unused", "WeakerAccess"})
@Slf4j
@RequiredArgsConstructor
public class ProjectResources {
    public static final String ENV_BUILD_DIR_NAME = "project.build.dir.name";
    public static final String[] BUILD_DIRS = {"out", "build", "target"};

    @NonNull private final Class sourceClass;

    @NonNull
    public Path getBuildDir() {
        try {
            return moveToBuildRoot(Paths.get(sourceClass.getProtectionDomain().getCodeSource().getLocation().toURI()));
        } catch (URISyntaxException e) {
            throw new UtilException(format("Cannot get build folder for class '%s'", sourceClass), e);
        }
    }

    @NonNull
    public Path getResourcesDir() {
        final URL url = sourceClass.getResource("");
        if (url == null) {
            throw new UtilException("ProjectResources folder is null");
        }
        try {
            return moveToBuildRoot(Paths.get(url.toURI()));
        } catch (URISyntaxException e) {
            throw new UtilException("Cannot get resources folder", e);
        }
    }

    @NonNull
    public List<Path> findResourceFiles(@NonNull String dirName, @NonNull String fileName, int depth) {
        return FileUtils.findFiles(FileUtils.findDir(getResourcesDir(), dirName, depth), fileName, depth);
    }

    @NonNull
    public List<Path> findResourceList(@NonNull String fileName) {
        return findResourceFiles("", fileName, 5);
    }

    @NonNull
    public Path findResource(@NonNull String fileName) {
        return findResourceFiles("", fileName, 5).stream().findFirst().orElseThrow(() -> new UtilException(format("Cannot find resource file '%s'", fileName)));
    }

    @NonNull
    public Path findResourceFile(@NonNull String dirName, @NonNull String fileName) {
        return findResourceFiles(dirName, fileName, 1).stream().findFirst()
                .orElseThrow(() -> new UtilException(format("Cannot find resource files '%s' in dir '%s'", fileName, dirName)));
    }

    @NonNull
    public Path findFileInBuildDir(@NonNull String fileName) {
        return FileUtils.findFiles(getBuildDir(), fileName, 5).stream().findFirst()
                .orElseThrow(() -> new UtilException(format("Cannot find resource files '%s' in build dir", fileName)));
    }

    @NonNull
    private Path moveToBuildRoot(Path innerProjectPath) {
        final String[] candidateBuildDirs;
        if (EnvVariables.has(ENV_BUILD_DIR_NAME)) {
            candidateBuildDirs = new String[]{EnvVariables.get(ENV_BUILD_DIR_NAME)};
        } else {
            candidateBuildDirs = BUILD_DIRS;
        }
        Iterator<Path> iterator = new Iterator<Path>() {

            private Path currentPath = innerProjectPath;

            @Override
            public boolean hasNext() {
                return currentPath.getParent() != null;
            }

            @Override
            public Path next() {
                final Path res = currentPath.getParent();
                currentPath = res;
                return res;
            }
        };

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), true)
                .filter(curPath -> Arrays.stream(candidateBuildDirs).anyMatch(curPath::endsWith)).findFirst().orElseThrow(() -> new UtilException(
                        format("Cannot find any build directory '%s' in path '%s'", Str.toString(candidateBuildDirs), innerProjectPath)));
    }
}