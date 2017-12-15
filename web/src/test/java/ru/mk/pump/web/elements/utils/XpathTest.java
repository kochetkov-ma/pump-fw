package ru.mk.pump.web.elements.utils;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

@Slf4j
class XpathTest {

    @Test
    void fixIfXpath() {



        assertThat(Xpath.fixIfXpath(By.id("//id//")).toString()).isEqualTo("By.id: //id//");
        assertThat(Xpath.fixIfXpath(By.tagName("//tag//")).toString()).isEqualTo("By.tagName: //tag//");
        assertThat(Xpath.fixIfXpath(By.className("//class//")).toString()).isEqualTo("By.className: //class//");


        assertThat(Xpath.fixIfXpath(By.xpath("./div/./button")).toString())
            .isEqualTo("By.xpath: ./div/button");

        assertThat(Xpath.fixIfXpath(By.xpath("////")).toString())
            .isEqualTo("By.xpath: .");

        assertThat(Xpath.fixIfXpath(By.xpath(".....///...//../././..")).toString())
            .isEqualTo("By.xpath: .");

        assertThat(Xpath.fixIfXpath(By.xpath(".")).toString())
            .isEqualTo("By.xpath: .");

        assertThat(Xpath.fixIfXpath(By.xpath("./.div")).toString())
            .isEqualTo("By.xpath: ./div");

        assertThat(Xpath.fixIfXpath(By.xpath("div")).toString())
            .isEqualTo("By.xpath: ./div");

        assertThat(Xpath.fixIfXpath(By.xpath("/div/")).toString())
            .isEqualTo("By.xpath: ./div");

        assertThat(Xpath.fixIfXpath(By.xpath("./div/.")).toString())
            .isEqualTo("By.xpath: ./div");

        assertThat(Xpath.fixIfXpath(By.xpath("/./div/./")).toString())
            .isEqualTo("By.xpath: ./div");

        assertThat(Xpath.fixIfXpath(By.xpath("/././div/././")).toString())
            .isEqualTo("By.xpath: ./div");

        assertThat(Xpath.fixIfXpath(By.xpath("div.")).toString())
            .isEqualTo("By.xpath: ./div");

        assertThat(Xpath.fixIfXpath(By.xpath("./div")).toString())
            .isEqualTo("By.xpath: ./div");

        assertThat(Xpath.fixIfXpath(By.xpath("div/..")).toString())
            .isEqualTo("By.xpath: ./div/..");

        assertThat(Xpath.fixIfXpath(By.xpath("div..")).toString())
            .isEqualTo("By.xpath: ./div/..");

        assertThat(Xpath.fixIfXpath(By.xpath(".div..")).toString())
            .isEqualTo("By.xpath: ./div/..");

        assertThat(Xpath.fixIfXpath(By.xpath("./div/..")).toString())
            .isEqualTo("By.xpath: ./div/..");

        assertThat(Xpath.fixIfXpath(By.xpath("..div..")).toString())
            .isEqualTo("By.xpath: ./../div/..");

        assertThat(Xpath.fixIfXpath(By.xpath("./../div/..")).toString())
            .isEqualTo("By.xpath: ./../div/..");

        assertThat(Xpath.fixIfXpath(By.xpath("/.//./div/././/")).toString())
            .isEqualTo("By.xpath: .//div");

        assertThat(Xpath.fixIfXpath(By.xpath(".//div")).toString())
            .isEqualTo("By.xpath: .//div");

        assertThat(Xpath.fixIfXpath(By.xpath(".//div")).toString())
            .isEqualTo("By.xpath: .//div");

        assertThat(Xpath.fixIfXpath(By.xpath("/././div/div//div/../..//div/././/")).toString())
            .isEqualTo("By.xpath: ./div/div//div/../..//div");

        assertThat(Xpath.fixIfXpath(By.xpath("/././div/div////////////////div/../././..//.div/././/")).toString())
            .isEqualTo("By.xpath: ./div/div//div/../..//div");

        assertThat(Xpath.fixIfXpath(By.xpath("....../././div/div/....../////////div/../././..//.div/././/")).toString())
            .isEqualTo("By.xpath: ../div/div/..//div/../..//div");

        assertThat(Xpath.fixIfXpath(By.xpath("./div/div//div/../..//div")).toString())
            .isEqualTo("By.xpath: ./div/div//div/../..//div");
    }
}