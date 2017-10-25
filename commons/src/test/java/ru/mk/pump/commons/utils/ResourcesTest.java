package ru.mk.pump.commons.utils;

import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import ru.mk.pump.commons.exception.UtilException;

@Slf4j
public class ResourcesTest {

    @Test
    public void getBuildDir() throws Exception {
        final Path result = Resources.getBuildDir(ResourcesTest.class);
        log.info(result.toString());
        Assert.assertNotNull(result);
    }

    @Test
    public void getResourcesDir() throws Exception {
        final Path result = Resources.getResourcesDir();
        log.info(result.toString());
        Assert.assertNotNull(result);
    }

    @Test(expected = UtilException.class)
    public void getResourcesFile() throws Exception {
        final Path result = Resources.findResource("logback.xml");
        log.info(result.toString());
        Assert.assertNotNull(result);

        Resources.findResource("1logback");
    }

    @Test
    public void getResourcesFiles() throws Exception {
        final List<Path> result = Resources.findResourceFiles("ru", "R", 10);
        log.info(result.toString());
        Assert.assertNotNull(result);
    }

    @Test
    public void getBuildFiles() throws Exception {
        final Path result = Resources.findFileInBuildDir(ResourcesTest.class, "logback.xml");
        log.info(result.toString());
        Assert.assertNotNull(result);
    }

}