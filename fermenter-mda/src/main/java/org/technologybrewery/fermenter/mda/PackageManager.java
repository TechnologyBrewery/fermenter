package org.technologybrewery.fermenter.mda;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackageManager {

    private static final Logger logger = LoggerFactory.getLogger(PackageManager.class);

    private static ThreadLocal<PackageManager> threadBoundInstance = ThreadLocal.withInitial(PackageManager::new);

    private Map<String, String> artifactIdToBasePackage = new HashMap<>();

    private PackageManager() {
    }

    private static PackageManager getInstance() {
        return threadBoundInstance.get();
    }

    /**
     * Returns a base package for a given artifact id.
     * 
     * @param artifactId
     *            The name for which to look
     * @return The returned base package or null if one does not exist
     */
    public static String getBasePackage(String artifactId) {
        return getInstance().artifactIdToBasePackage.get(artifactId);
    }

    public static void addMapping(String artifactId, URL url, String defaultPackageName) {
        try (InputStream stream = processURL(url)) {
            Properties props = new Properties();
            props.load(stream);

            getInstance().artifactIdToBasePackage.put(artifactId, props.getProperty("basePackage"));

        } catch (IOException ex) {
            logger.debug("Could not find package properties for artifactId '{}' at URL {}", artifactId, url.getPath());
            logger.debug("Using default package name ('{}') for artifactId '{}' instead", defaultPackageName,
                    artifactId);
            getInstance().artifactIdToBasePackage.put(artifactId, defaultPackageName);

        }
    }

    public static void addMapping(String artifactId, String basePackage) {
        getInstance().artifactIdToBasePackage.put(artifactId, basePackage);
    }


    private static InputStream processURL(URL url) throws IOException {
        String sUrl = url.toString();
        if (sUrl.indexOf(".jar") != -1) {
            sUrl = "jar:" + url + "!/" + "package.properties";
            URL jarUrl = new URL(sUrl);
            JarURLConnection jarConnection = (JarURLConnection) jarUrl.openConnection();
            return jarConnection.getInputStream();
        }

        return url.openStream();
    }

    /**
     * Cleans up thread local resources.
     */
    public static void cleanUp() {
        threadBoundInstance.remove();
    }

}
