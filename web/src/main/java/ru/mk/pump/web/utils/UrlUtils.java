package ru.mk.pump.web.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlUtils {

    /**
     * [RUS] Соединение URL и путей.
     * Если базовый URL является паттерном вида {@code http://domain.ru/{1}/{2}} ,
     * то параметры {1} и {2} заменяются на переданные части пути в соответсвие с индексом начиная с 1.
     * Если базовый URL является стандартным URL, то части пути добаляются в конце с разделителем '/'
     * Лишние '/' удаляются автоматически
     *
     * @param baseUrl базовый URL либо паттерн с параметрми подстановки вида {int > 0}
     * @param path path string
     * @throws IllegalArgumentException Если URL после всех постановок и обработок не соответсвует формату,
     * либо если для параметра не найдено пути по индексу, либо параметр не соответствует int > 0
     */
    public String concatWithPath(String baseUrl, String... path) {

        return null;
    }

    public String fixUrl(String candidateUrl) {
        return null;
    }

    public boolean isUrl(String candidateUrl) {
        return false;
    }

    public boolean isUrlWithPathParam(String candidateUrl) {
        return false;
    }
}

