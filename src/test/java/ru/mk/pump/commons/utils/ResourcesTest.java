package ru.mk.pump.commons.utils;

import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ResourcesTest {

    @Test
    public void getBuildDir() throws Exception {
        final Path result = Resources.getBuildDir(ResourcesTest.class);
        log.info(result.toString());
    }

    @Test
    public void getResourcesDir() throws Exception {
        Path result = Resources.getResourcesDir();
        log.info(result.toString());
    }

    @Test
    public void getResourcesFile() throws Exception {
        Path result = Resources.findResource("logback.xml");
        log.info(result.toString());

        result = Resources.findResource("logback");
        log.info(result.toString());
    }

    @Test
    public void getResourcesFiles() throws Exception {
        final List<Path> result = Resources.findResourceFiles("ru","R", 10);
        log.info(result.toString());
    }

}