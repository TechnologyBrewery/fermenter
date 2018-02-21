package org.bitbucket.fermenter.mda.metamodel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.exception.FermenterException;
import org.bitbucket.fermenter.mda.metamodel.element.MetamodelElement;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractMetamodelManager<T extends MetamodelElement> {

    private static final String METAMODEL_SUFFIX = ".json";
    private Map<String, Map<String, T>> metamodelByPackageMap = new HashMap<>();
    private Map<String, T> completeMetamodelMap = new HashMap<>();

    private static MessageTracker messageTracker = MessageTracker.getInstance();

    /**
     * Resets this instance to ensure a clean set of metadata is available.
     */
    public void reset() {
        metamodelByPackageMap = new HashMap<>();
        completeMetamodelMap = new HashMap<>();
    }

    /**
     * Validation occurs after the loading of all metadata to ensure that we can access all metadata in a safe fashion,
     * without having to worry to about what is already laoded and what needs to be loaded.
     */
    protected final void validate() {
        for (String packageName : metamodelByPackageMap.keySet()) {
            Collection<T> metadataItems = getMetamodelMap(packageName).values();
            validateElements(metadataItems);
        }
    }

    /**
     * Performs validation on a collection of <{@link MetamodelElement} instances.
     * 
     * @param elements
     *            The instances to validate
     */
    protected static <T extends MetamodelElement> void validateElements(Collection<T> elements) {
        if (!CollectionUtils.isEmpty(elements)) {
            for (T element : elements) {
                element.validate();
            }
        }
    }

    protected void loadMetamodel(String artifactId, String url) {
        if (StringUtils.isBlank(url)) {
            messageTracker.addErrorMessage("Metamodels for application '" + artifactId
                    + "' can not be found!  Please ensure the proper jar is on your classpath.");

        } else {
            List<URL> resources = null;

            try {
                resources = getMetamodelResources(url);

            } catch (IOException | URISyntaxException e) {
                messageTracker.addWarningMessage(
                        "No " + getMetamodelLocation() + " metamodels found for '" + artifactId + "', skipping...");

            }

            if (resources == null) {
                return;
            }

            try {
                for (URL resource : resources) {
                    InputStream is = resource.openStream();
                    try {
                        loadMetamodelFile(is);

                    } finally {
                        IOUtils.closeQuietly(is);
                    }

                }
            } catch (Exception ex) {
                throw new FermenterException("Error while loading metadata", ex);
            }

            postLoadMetamodel();
        }
    }

    protected List<URL> getMetamodelResources(String name) throws IOException, URISyntaxException {
        List<URL> metamodelResources = null;
        if (name.contains(".jar")) {
            metamodelResources = getMetamodelResourceFromJar(name);
        } else {
            getMetamodelResourcesFromDirectory(name);
        }
        return metamodelResources;
    }

    private List<URL> getMetamodelResourceFromJar(String name) throws IOException {
        List<URL> metamodelResources = new ArrayList<>();
        name = "jar:" + name + "!/";
        URL jarUrl = new URL(name);
        JarURLConnection jarConnection = (JarURLConnection) jarUrl.openConnection();
        JarFile file = jarConnection.getJarFile();
        Enumeration<JarEntry> e = file.entries();
        while (e.hasMoreElements()) {
            JarEntry newEntry = e.nextElement();
            String entryName = newEntry.getName();
            if (entryName.startsWith(this.getMetamodelLocation()) && entryName.endsWith(METAMODEL_SUFFIX)) {
                metamodelResources.add(new URL(name + newEntry.getName()));
            }
        }

        return metamodelResources;
    }

    private List<URL> getMetamodelResourcesFromDirectory(String name) throws URISyntaxException, MalformedURLException {
        List<URL> metamodelResources = new ArrayList<>();
        File metamodelDir = new File(new URI(name + this.getMetamodelLocation()));
        File[] files = metamodelDir.listFiles();
        File file = null;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                if (file.getName().endsWith(METAMODEL_SUFFIX)) {
                    metamodelResources.add(file.toURI().toURL());
                }
            }
        }

        return metamodelResources;
    }

    protected abstract String getMetamodelLocation();

    private void loadMetamodelFile(InputStream stream) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            T instance = objectMapper.readValue(stream, getMetamodelClass());
            addMetamodelElement(instance);

        } catch (IOException e) {
            throw new FermenterException("Problem reading metamodel!", e);

        }

    }

    protected abstract Class<T> getMetamodelClass();

    protected void postLoadMetamodel() {

    }

    protected Map<String, T> getMetamodelMap(String packageName) {
        return metamodelByPackageMap.get(packageName);
    }

    protected Map<String, T> getCompleteMetamodelMap() {
        return completeMetamodelMap;
    }

    protected void addMetamodelElement(T element) {
        getMetamodelMap(element.getPackage()).put(element.getName(), element);
        completeMetamodelMap.put(element.getName(), element);

    }

}
