package ru.mk.pump.web.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ImmutableCapabilities;
import ru.mk.pump.commons.constants.MainConstants;
import ru.mk.pump.commons.exception.UtilException;
import ru.mk.pump.commons.utils.FileUtils;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.commons.utils.PropertiesUtil;

@UtilityClass
public class CapabilitiesUtils {

    /**
     * Load properties with capabilities and convert to map. First check as file path then try to find among the project resources.
     *
     * @param filePathOrFileName Full path to file or file name in project
     * @param projectResources ProjectResources class
     * @return ImmutableCapabilities
     * @throws UtilException If file has not been found
     */
    public Capabilities loadFromProperties(String filePathOrFileName, ProjectResources projectResources) {
        Path capsFile;
        if (FileUtils.isExistsAndValid(filePathOrFileName)) {
            capsFile = Paths.get(filePathOrFileName);
        } else {
            capsFile = projectResources.findResource(filePathOrFileName);
        }
        return new ImmutableCapabilities(PropertiesUtil.propertiesToMap(PropertiesUtil.load(capsFile, MainConstants.FILE_ENCODING)));
    }

}
