package ru.mk.pump.commons.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import ru.mk.pump.commons.exception.UtilException;

@Slf4j
public class ProjectResourcesTest {

    @Test
    public void getBuildDir() throws Exception {
        final Path result = ProjectResources.getBuildDir(ProjectResourcesTest.class);
        log.info(result.toString());
        Assert.assertNotNull(result);
    }

    @Test
    public void getResourcesDir() throws Exception {
        final Path result = ProjectResources.getResourcesDir();
        log.info(result.toString());
        Assert.assertNotNull(result);
    }

    @Test
    public void getResourcesFile() throws Exception {
        final Path result = ProjectResources.findResource("logback.xml");
        log.info(result.toString());
        Assert.assertNotNull(result);
        assertThatThrownBy(() -> ProjectResources.findResource("1logback")).isInstanceOf(UtilException.class);
    }

    @Test
    public void getResourcesFiles() throws Exception {
        final List<Path> result = ProjectResources.findResourceFiles("classes", "R", 10);
        log.info(result.toString());
        Assert.assertNotNull(result);
    }

    @Test
    public void getBuildFiles() throws Exception {
        final Path result = ProjectResources.findFileInBuildDir(ProjectResourcesTest.class, "logback.xml");
        log.info(result.toString());
        Assert.assertNotNull(result);
    }

}