package ru.mk.pump.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.exception.UtilException;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class ProjectResourcesTest {

    @Test
    public void getBuildDir() {
        final Path result = new ProjectResources(getClass()).getBuildDir();
        log.info(result.toString());
        Assertions.assertNotNull(result);
    }

    @Test
    public void getResourcesDir() {
        final Path result = new ProjectResources(getClass()).getResourcesDir();
        log.info(result.toString());
        Assertions.assertNotNull(result);
    }

    @Test
    public void getResourcesFile() {
        final Path result = new ProjectResources(getClass()).findResource("logback.xml");
        log.info(result.toString());
        Assertions.assertNotNull(result);
        assertThatThrownBy(() -> new ProjectResources(getClass()).findResource("1logback")).isInstanceOf(UtilException.class);
    }

    @Test
    public void getResourcesFiles() {
        final List<Path> result = new ProjectResources(getClass()).findResourceFiles("classes", "R", 10);
        log.info(result.toString());
        Assertions.assertNotNull(result);
    }

    @Test
    public void getBuildFiles() {
        final Path result = new ProjectResources(getClass()).findFileInBuildDir("logback.xml");
        log.info(result.toString());
        Assertions.assertNotNull(result);
    }

}