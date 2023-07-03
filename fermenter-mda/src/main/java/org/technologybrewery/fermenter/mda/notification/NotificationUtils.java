package org.technologybrewery.fermenter.mda.notification;

import org.technologybrewery.fermenter.mda.generator.GenerationException;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;

public final class NotificationUtils {

    private NotificationUtils() {
        // prevent instantiation of all-static class
    }

    public static void mergeExistingAndNewProperties(File sourceGroupPropertiesFile, Properties targetGroupProperties) {
        Properties sourceGroupProperties = new Properties();
        try (Reader reader = Files.newBufferedReader(sourceGroupPropertiesFile.toPath(), Charset.defaultCharset())) {
            sourceGroupProperties.load(reader);
        } catch (IOException e) {
            throw new GenerationException("Could not read group properties!", e);
        }

        targetGroupProperties.putAll(sourceGroupProperties);
    }
}
