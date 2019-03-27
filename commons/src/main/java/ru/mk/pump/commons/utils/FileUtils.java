package ru.mk.pump.commons.utils;

import com.google.common.io.Files;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.exception.UtilException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * @author kochetkovma
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@UtilityClass
@Slf4j
public class FileUtils {

    public boolean isExistsAndValid(@Nullable String candidatePath) {
        if (Str.isBlank(candidatePath)) {
            return false;
        }
        Path path;
        try {
            path = Paths.get(candidatePath);
        } catch (InvalidPathException e) {
            return false;
        }
        return java.nio.file.Files.exists(path);
    }

    public boolean isEmpty(@NonNull Path path) {
        try {
            return java.nio.file.Files.size(path) == 0;
        } catch (IOException e) {
            return true;
        }
    }

    public Path copy(@NonNull Path sourcePath, @NonNull Path targetPath) {
        targetPath = createIfNotExists(targetPath);
        try {
            Files.copy(sourcePath.toFile(), targetPath.toFile());
        } catch (final IOException cause) {
            throw new UtilException(format("Cannot copy from '%s' to '%s'", sourcePath.toString(), targetPath.toString()), cause);
        }
        return targetPath;
    }

    public Path createIfNotExists(@NonNull Path path) {
        if (java.nio.file.Files.notExists(path)) {
            try {
                java.nio.file.Files.createDirectories(path);
            } catch (final IOException cause) {
                throw new UtilException(format("Cannot create dir %s", path.toString()), cause);
            }
        }
        return path;
    }

    public void appendToFile(@NonNull Path path, String string, @NonNull Charset encoding) throws IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(path.toFile(), string, encoding.toString(), true);
    }

    public void toFile(@NonNull Path path, String String, @NonNull Charset encoding) throws IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(path.toFile(), String, encoding.toString());
    }

    public String toString(@NonNull Path filePath, @NonNull Charset encoding) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(filePath.toFile(), encoding);
        } catch (IOException e) {
            throw new UtilException(format("Error when tried to get file '%s' content '%s'", filePath.toString(), encoding.toString()));
        }
    }

    public String getExtension(@NonNull Path path) {
        return Files.getFileExtension(path.getFileName().toString());
    }

    public List<Path> getFileList(@NonNull Path dirPath, boolean onlyFile) throws IOException {
        final List<Path> result = new ArrayList<>();
        try (Stream<Path> paths = java.nio.file.Files.walk(dirPath)) {
            paths.forEach(filePath -> {
                if (!onlyFile || java.nio.file.Files.isRegularFile(filePath)) {
                    result.add(filePath);
                }
            });
        }
        return result;
    }

    public List<Path> getFileListOfNewFiles(List<Path> oldFiles, Path dirPath) throws IOException {
        final List<Path> newList = getFileList(dirPath, true);
        newList.removeAll(oldFiles);
        return newList;
    }

    public void clearDir(Path dirPath) throws IOException {
        org.apache.commons.io.FileUtils.cleanDirectory(dirPath.toFile());

    }

    /**
     * @return MB
     */
    public float getDirSize(Path dirPath) {
        return org.apache.commons.io.FileUtils.sizeOfDirectory(dirPath.toFile()) / 1024 / 1024;
    }

    public Path findDir(@NonNull Path sourceDir, @NonNull String dirName, int depth) {
        if (depth < 1) {
            depth = 1;
        }
        if (java.nio.file.Files.notExists(sourceDir) || !java.nio.file.Files.isDirectory(sourceDir)) {
            throw new UtilException(format("Cannot find dir '%s'", sourceDir));
        }
        try {
            return java.nio.file.Files.find(sourceDir, depth, (path, attr) -> attr.isDirectory() && path.getFileName().toString().startsWith(dirName))
                    .findFirst().orElseThrow(() -> new UtilException(format("Cannot find dir '%s' in dir '%s'", dirName, sourceDir)));
        } catch (IOException e) {
            throw new UtilException(format("Cannot find dir '%s' in dir '%s'", dirName, sourceDir), e);
        }
    }

    public List<Path> findFiles(@NonNull Path sourceDir, @NonNull String fileName, int depth) {
        if (depth < 1) {
            depth = 1;
        }
        if (java.nio.file.Files.notExists(sourceDir) || !java.nio.file.Files.isDirectory(sourceDir)) {
            throw new UtilException(format("Cannot find dir '%s'", sourceDir));
        }
        try {
            return java.nio.file.Files.find(sourceDir, depth, (path, attr) -> attr.isRegularFile() && path.getFileName().toString().startsWith(fileName))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UtilException(format("Cannot find files '%s' in dir '%s'", fileName, sourceDir), e);
        }
    }

    public Path resolveDir(@NonNull Path sourceDir, @Nullable String targetDir) {
        if (targetDir == null || targetDir.isEmpty()) {
            return sourceDir;
        }
        final Path result = sourceDir.resolve(targetDir);
        if (java.nio.file.Files.notExists(sourceDir) || !java.nio.file.Files.isDirectory(sourceDir)) {
            throw new UtilException(format("Resolved name '%s' from dir '%s' is not a directory", targetDir, sourceDir));
        }
        return result;
    }
}
