package ru.mk.pump.web.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.text.StrBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.utils.Preconditions;
import ru.mk.pump.commons.utils.Strings;

/**
 * [RUS] Утилитные методы для проверки и создания URL.
 * Не является универсальным URL билдером типа {@link org.apache.http.client.utils.URIBuilder}
 * Применение ограничено обработкой URL для сущностей данного под-проекта.
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
@UtilityClass
public class UrlUtils {

    private static final String PROTOCOL_DELIMITER = "://";

    /**
     * Default protocol. Insert when checking URL protocol is empty in {@link #fixUrl(String)}
     */
    public static String PROTOCOL = "https";

    /**
     * [RUS] Соединение URL и путей.
     * Если базовый URL является паттерном вида {@code http://domain.ru/{1}/{2}} ,
     * то параметры {1} и {2} заменяются на переданные части пути в соответсвие с индексом начиная с 1.
     * Если базовый URL является стандартным URL, то части пути добаляются в конце с разделителем '/'
     * Лишние '/' удаляются автоматически
     *
     * @param baseUrl базовый URL либо паттерн с параметрми подстановки вида {int > 0}
     * @param path часть пути через '/' (имя ресурса)
     * @throws IllegalArgumentException Если URL после всех постановок и обработок не соответсвует формату либо не найден парметр с указанным номером
     */
    public String concatWithPath(@NotNull String baseUrl, @Nullable String... path) {
        Preconditions.checkStringNotBlank(baseUrl);
        final String res;
        if (hasPathParam(baseUrl)) {
            res = fixUrl(replaceParams(baseUrl, path));
        } else {
            StrBuilder sb = new StrBuilder(baseUrl);
            if (path != null) {
                Arrays.stream(path).forEach(p -> sb.append("/").append(p));
            }
            res = fixUrl(sb.toString());
        }
        if (isUrl(res)) {
            return res;
        } else {
            if (ArrayUtils.isNotEmpty(path)) {
                throw new IllegalArgumentException(String.format("Incorrect URL '%s' with params '%s'", baseUrl, Strings.toString(path)));
            } else {
                throw new IllegalArgumentException(String.format("Incorrect URL '%s'", baseUrl));
            }
        }
    }

    /**
     * [RUS] Исправить URL
     * <ul>
     *     <li>добавить протокол, если нет (по умолчанию {@link #PROTOCOL})</li>
     *     <li>удалить лишние слэши</li>
     *     <li>проверка по паттерну URL</li>
     * </ul>
     * Если URL не корректный или исправление не удалось , то возвращается исходное значение аргумента. Не генерирует Исключения
     * @param candidateUrl URL на исправление
     */
    @Nullable
    public String fixUrl(@Nullable String candidateUrl) {
        if (candidateUrl == null) {
            return null;
        }
        final StringBuilder result = new StringBuilder();
        candidateUrl = replaceParams(candidateUrl);
        Matcher matcher = Pattern.compile("^([\\w]+)([:/]+)(.*)").matcher(candidateUrl);
        if (matcher.find()) {
            candidateUrl = matcher.group(1) + PROTOCOL_DELIMITER + matcher.group(3);
        } else {
            candidateUrl = PROTOCOL + PROTOCOL_DELIMITER + candidateUrl;
        }

        URI url;
        try {
            url = new URI(candidateUrl);
        } catch (URISyntaxException e) {
            return candidateUrl;
        }
        if (url.getScheme() == null) {
            result.append(PROTOCOL);
        } else {
            result.append(url.getScheme());
        }
        result.append(PROTOCOL_DELIMITER);
        matcher = Pattern.compile("^([^\\w]+)([\\w\\\\.]+.*)").matcher(url.getRawSchemeSpecificPart());
        if (url.getHost() == null && matcher.find()) {
            result.append(matcher.group(2));
            return fixUrl(result.toString());
        }
        result.append(url.getHost());
        if (url.getPort() != -1) {
            result.append(":");
            result.append(url.getPort());
        }
        if (!Strings.isEmpty(url.getPath())) {
            result.append(url.getPath().replaceAll("/+", "/"));
        }
        return isUrl(result.toString()) ? result.toString() : candidateUrl;
    }

    /**
     * [RUS] Является ли переданный URL корректным
     */
    public boolean isUrl(@Nullable String candidateUrl) {
        if (candidateUrl == null) {
            return false;
        }
        try {
            new URL(candidateUrl);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    /**
     * Является ли переданный URL параметризированным вида {@code http://domain.ru/{1}/{2}},
     * где {1} и {2} являются порядковыми именами параметров.
     */
    public boolean hasPathParam(@Nullable String candidateUrl) {
        if (candidateUrl == null) {
            return false;
        }
        final Matcher matcher = Pattern.compile("\\{(\\d)}").matcher(candidateUrl);
        return matcher.find();
    }

    private String replaceParams(String url, String... params) {

        final Matcher matcher = Pattern.compile("\\{(\\d)}").matcher(url);
        final StringBuffer res = new StringBuffer(url.length());
        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group(1));
            final String rText;
            if (ArrayUtils.isEmpty(params)) {
                rText = "parameter_" + number;
            } else if (params.length >= number) {
                rText = params[number - 1];
            } else {
                throw new IllegalArgumentException(
                    String.format("Wrong parameters '%s' count '%s' in URL '%s', expected '%d'", Strings.toString(params), params.length, url, number));
            }
            matcher.appendReplacement(res, rText);
        }
        if (res.length() > 0) {
            return res.toString();
        } else {
            return url;
        }
    }
}