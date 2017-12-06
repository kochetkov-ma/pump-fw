package ru.mk.pump.web.elements.utils;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openqa.selenium.By;

@Slf4j
public class XpathTest {

    @Test
    public void fixIfXpath() {

        assertThat(Xpath.fixIfXpath(By.id("//id//")).toString()).isEqualTo("By.id: //id//");
        assertThat(Xpath.fixIfXpath(By.tagName("//tag//")).toString()).isEqualTo("By.tagName: //tag//");
        assertThat(Xpath.fixIfXpath(By.className("//class//")).toString()).isEqualTo("By.className: //class//");

        assertThat(Xpath.fixIfXpath(By.xpath("div[@class='test test']//div/./..//////div/../../..")).toString())
            .isEqualTo("By.xpath: ./div[@class='test test']//div/./..//div/../../..");

        assertThat(Xpath.fixIfXpath(By.xpath("//div[@class='test test']//div/./..//////div/../../..//")).toString())
            .isEqualTo("By.xpath: .//div[@class='test test']//div/./..//div/../../..");

        assertThat(Xpath.fixIfXpath(By.xpath("/div[@class='test test']//div/./..//////div/../../../")).toString())
            .isEqualTo("By.xpath: ./div[@class='test test']//div/.//div/..");

        assertThat(Xpath.fixIfXpath(By.xpath(".div[@class='test test']//div/./..//////div/../../../.")).toString())
            .isEqualTo("By.xpath: ./div[@class='test test']//div/.//div/..");

        assertThat(Xpath.fixIfXpath(By.xpath(".div[@class='test test']//div/./..//////div/../../../././")).toString())
            .isEqualTo("By.xpath: ./div[@class='test test']//div/.//div/..");

        assertThat(Xpath.fixIfXpath(By.xpath("./../div[@class='test test']//div/./..//////div/../../../././")).toString())
            .isEqualTo("By.xpath: ./div[@class='test test']//div/.//div/..");

        assertThat(Xpath.fixIfXpath(By.xpath("./../div[@class='test test']//div/./..//////div/../../../././")).toString())
            .isEqualTo("By.xpath: ./div[@class='test test']//div/.//div/..");

        assertThat(Xpath.fixIfXpath(By.xpath("./../////div[@class='test test']//div/./..//////div/../......./../././")).toString())
            .isEqualTo("By.xpath: ./div[@class='test test']//div/.//div/..");

        assertThat(Xpath.fixIfXpath(By.xpath("//////...../../////div[@class='test test']//div/./..//////div/../......./../././...........")).toString())
            .isEqualTo("By.xpath: ./div[@class='test test']//div/.//div/..");

        assertThat(
            Xpath.fixIfXpath(By.xpath("//////...../../////div[@class='test test']//div/./..//////div/.././././././././../././///........///////")).toString())
            .isEqualTo("By.xpath: ./div[@class='test test']//div/.//div/..");

    }
}