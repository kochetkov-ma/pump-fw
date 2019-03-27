package ru.mk.pump.web.utils;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class UrlUtilsTest {

    @Test
    void testConcatWithPath() {
        String urlString = "http::////www.google.ru:8080///{1}//{2}///test";
        assertThat(UrlUtils.concatWithPath(urlString, "p1", "p2", "p3")).isEqualTo("http://www.google.ru:8080/p1/p2");
        assertThat(UrlUtils.concatWithPath(urlString, "p1", "p2")).isEqualTo("http://www.google.ru:8080/p1/p2");

        final String finalUrlString = "////www.google.ru:8080///{1}/{2}/{2}//{1}/";
        assertThat(UrlUtils.concatWithPath(finalUrlString, "p1", "p2")).isEqualTo("https://www.google.ru:8080/p1/p2/p2/p1");
        assertThatThrownBy(() -> UrlUtils.concatWithPath(finalUrlString, "p1")).
            isInstanceOf(IllegalArgumentException.class);

        urlString = "////www.google.ru:8080";
        assertThat(UrlUtils.concatWithPath(urlString, "p1", "p2", "p3")).isEqualTo("https://www.google.ru:8080/p1/p2/p3");
        assertThat(UrlUtils.concatWithPath(urlString, "p1", "p2")).isEqualTo("https://www.google.ru:8080/p1/p2");
        assertThat(UrlUtils.concatWithPath(urlString, "p1")).isEqualTo("https://www.google.ru:8080/p1");

        urlString = "////www.google.ru";
        assertThat(UrlUtils.concatWithPath(urlString, "p1", "p2", "p3")).isEqualTo("https://www.google.ru/p1/p2/p3");
        assertThat(UrlUtils.concatWithPath(urlString, "p1", "p2")).isEqualTo("https://www.google.ru/p1/p2");
        assertThat(UrlUtils.concatWithPath(urlString, "p1")).isEqualTo("https://www.google.ru/p1");
    }

    @Test
    void testFixUrl() {
        String urlString = "www.google.ru:8080";
        assertThat(UrlUtils.fixUrl(urlString)).isEqualTo("https://www.google.ru:8080");

        urlString = "www.google.ru///";
        assertThat(UrlUtils.fixUrl(urlString)).isEqualTo("https://www.google.ru/");

        urlString = "https://google.ru:4000/path/path";
        assertThat(UrlUtils.fixUrl(urlString)).isEqualTo("https://google.ru:4000/path/path");

        urlString = "http::////www.google.ru:8080///{1}//{2}///";
        assertThat(UrlUtils.fixUrl(urlString)).isEqualTo("http://www.google.ru:8080/parameter_1/parameter_2");

        urlString = "https::////www.google.ru:8080///{1}//{2}///";
        assertThat(UrlUtils.fixUrl(urlString)).isEqualTo("https://www.google.ru:8080/parameter_1/parameter_2");
    }

    @Test
    void testIsUrl() {
        String urlString = "www.google.ru:8080";
        assertThat(UrlUtils.isUrl(urlString)).isFalse();

        urlString = "www.google.ru///";
        assertThat(UrlUtils.isUrl(urlString)).isFalse();

        urlString = "https://google.ru:4000/{id}/path";
        assertThat(UrlUtils.isUrl(urlString)).isTrue();

        urlString = "http::////www.google.ru:8080///{1}//{2}///";
        assertThat(UrlUtils.isUrl(urlString)).isTrue();

        urlString = "https::////www.google.ru:8080///{1}//{2}///";
        assertThat(UrlUtils.isUrl(urlString)).isTrue();
    }

    @Test
    void testIsUrlWithPathParam() {
        String urlString = "www.google.ru:8080";
        assertThat(UrlUtils.hasPathParam(urlString)).isFalse();

        urlString = "www.google.ru///";
        assertThat(UrlUtils.hasPathParam(urlString)).isFalse();

        urlString = "https://google.ru:4000/path/path";
        assertThat(UrlUtils.hasPathParam(urlString)).isFalse();

        urlString = "http::////www.google.ru:8080///{1}//{2}///";
        assertThat(UrlUtils.hasPathParam(urlString)).isTrue();

        urlString = "https::////www.google.ru:8080///{1}//{2}///";
        assertThat(UrlUtils.hasPathParam(urlString)).isTrue();
    }
}