package ru.mk.pump.commons.utils;

import static java.lang.String.format;

import com.google.common.io.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.exception.UtilException;

/**
 * @author kochetkovma
 */
@SuppressWarnings("WeakerAccess")
@UtilityClass
@Slf4j
public class FileUtils {

    public static boolean isEmpty(final Path path) {
        try {
            return java.nio.file.Files.size(path) == 0;
        } catch (IOException e) {
            return true;
        }
    }

    public static Path createIfNotExists(final Path path) {
        if (java.nio.file.Files.notExists(path)) {
            try {
                java.nio.file.Files.createDirectories(path);
            } catch (final IOException cause) {
                throw new RuntimeException(format("Не удалось создать директорию %s", path.toString()), cause);
            }
        }
        log.info("Создана директория : " + path.toString());
        return path;
    }

    /**
     * Сохранить файл в строку
     *
     * @param path Путь к файлу для сохранения.
     * @param string Содержимое файла в виде строки
     * @param encoding Кодировка файла.
     */
    public static void addToFile(Path path, String string, Charset encoding, String delimiter) throws IOException {
        if (!isEmpty(path)) {
            string = delimiter + string;
        }
        org.apache.commons.io.FileUtils.writeStringToFile(path.toFile(), string, encoding.toString(), true);
    }

    /**
     * Сохранить файл в строку
     *
     * @param path Путь к файлу для сохранения.
     * @param String Содержимое файла в виде строки
     * @param encoding Кодировка файла.
     */
    public static void toFile(Path path, String String, Charset encoding) throws IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(path.toFile(), String, encoding.toString());
    }

    /**
     * Сохранить файл в строку
     *
     * @param filePath Путь к файлу.
     * @param encoding Кодировка файла.
     * @return Содержимое файла в виде строки.
     */
    public static String toString(Path filePath, Charset encoding) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(filePath.toFile(), encoding);
        } catch (IOException e) {
            throw new UtilException(format("Error when tried to get file '%s' content '%s'", filePath.toString(), encoding.toString()));
        }
    }

    /**
     * Получить расширение файла
     *
     * @param path Путь к файлу.
     * @return Расширение файла.
     */
    public static String getExtension(Path path) {
        return Files.getFileExtension(path.getFileName().toString());
    }

    /**
     * Получение списка файлов в каталоге.
     *
     * @param dirPath Путь к каталогу.
     * @param onlyFile Фильтровать только файлы - true
     * @return Список файлов в каталоге.
     */
    public static final List<Path> getFileList(Path dirPath, boolean onlyFile) throws IOException {
        List<Path> result = new ArrayList<>();
        try (Stream<Path> paths = java.nio.file.Files.walk(dirPath)) {
            paths.forEach(filePath -> {
                if (!onlyFile || java.nio.file.Files.isRegularFile(filePath)) {
                    result.add(filePath);
                }
            });
        }
        return result;
    }

    /**
     * Проверить наличие новых файлов относительно переданного списка старых файлов в каталоге.
     *
     * @param oldFiles Список старых файлов.
     * @param dirPath Путь к каталогу для проверки.
     * @return Список файлов, которые появились в каталоге.
     */
    public static final List<Path> getFileListOfNewFiles(List<Path> oldFiles, Path dirPath) throws IOException {
        List<Path> newList = getFileList(dirPath, true);
        newList.removeAll(oldFiles);
        return newList;
    }

    /**
     * Очистить директорию. !!! Осторожно
     *
     * @param dirPath Путь к каталогу.
     */
    public static final void clearDir(Path dirPath) throws IOException {
        org.apache.commons.io.FileUtils.cleanDirectory(dirPath.toFile());

    }

    /**
     * Вернуть размер директории
     *
     * @return Размер директории в МБ
     */
    public static final float getDirSize(Path dirPath) throws IOException {
        return org.apache.commons.io.FileUtils.sizeOfDirectory(dirPath.toFile()) / 1024 / 1024;
    }
}
