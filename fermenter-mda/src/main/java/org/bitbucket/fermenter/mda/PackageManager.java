package org.bitbucket.fermenter.mda;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PackageManager {

    private static final Log LOG = LogFactory.getLog(PackageManager.class.getName());
    
    private static final PackageManager instance = new PackageManager();
    
    private Map<String, String> artifactIdToBasePackage = new HashMap<>();

    private PackageManager() {
    }

    /**
     * Returns a base package for a given artifact id.
     * 
     * @param artifactId
     *            The name for which to look
     * @return The returned base package or null if one does not exist
     */
    public static String getBasePackage(String artifactId) {
        return instance.artifactIdToBasePackage.get(artifactId);
    }

    public static void addMapping(String artifactId, URL url) {
        try (InputStream stream = processURL(url)){
            Properties props = new Properties();
            props.load(stream);

            instance.artifactIdToBasePackage.put(artifactId, props.getProperty("basePackage"));
        } catch (IOException ex) {
            LOG.warn("Could not find package properties for artifactId '" + artifactId + "' at URL "
                    + url.getPath());
        }
    }

    public static void addMapping(String artifactId, String basePackage) {
        instance.artifactIdToBasePackage.put(artifactId, basePackage);
    }

    public static String getPackageForArtifactId(String artifactId) {
        return instance.artifactIdToBasePackage.get(artifactId);
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

}
