package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.DMUrls;

@Slf4j
class LinkImplTest extends AbstractWebTest{

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        createPages(getBrowser());
    }

    @Test
    void getHref() {
        getBrowser().open(DMUrls.MAIN_PAGE_URL);
        mainPage.getFlatDocs().click();
        Assertions.assertThat(mainPage.getFlatDocsDownload().getHref()).isEqualTo("https://ipotekaonline.open.ru/assets/info/list_documents.docx");

    }

    @Test
    void download() {
        getBrowser().open(DMUrls.MAIN_PAGE_URL);
        mainPage.getFlatDocs().click();
        Assertions.assertThat(mainPage.getFlatDocsDownload().download()).contains("auto_downloads","list_documents.docx");
    }
}