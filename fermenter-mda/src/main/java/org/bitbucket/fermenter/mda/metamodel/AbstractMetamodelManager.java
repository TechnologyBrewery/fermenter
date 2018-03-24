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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.exception.FermenterException;
import org.bitbucket.fermenter.mda.metamodel.element.MetamodelElement;
import org.bitbucket.fermenter.mda.metamodel.element.NamespacedMetamodelElement;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractMetamodelManager<T extends NamespacedMetamodelElement> {

    private static final Log log = LogFactory.getLog(AbstractMetamodelManager.class);
    
    private static final String METAMODEL_SUFFIX = ".json";
    private Map<String, Map<String, T>> metadataByPackageMap = new HashMap<>();
    private Map<String, T> completeMetadataMap = new HashMap<>();

    private static MessageTracker messageTracker = MessageTracker.getInstance();

    protected MetamodelConfig config = KrauseningConfigFactory.create(MetamodelConfig.class);

    /**
     * Resets this instance to ensure a clean set of metadata is available.
     */
    public void reset() {
        metadataByPackageMap = new HashMap<>();
        completeMetadataMap = new HashMap<>();
    }

    /**
     * Validation occurs after the loading of all metadata to ensure that we can access all metadata in a safe fashion,
     * without having to worry to about what is already laoded and what needs to be loaded.
     */
    protected final void validate() {
        for (String packageName : metadataByPackageMap.keySet()) {
            Collection<T> metadataItems = getMetadataMap(packageName).values();
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

    protected void loadMetadata(MetadataUrl metadataUrl) {
        if (StringUtils.isBlank(metadataUrl.getUrl())) {
            messageTracker.addErrorMessage("Metadata for artifactId '" + metadataUrl.getArtifactId()
                    + "' can not be found!  Please ensure the proper jar is on your classpath.");

        } else {
            List<URL> resources = null;

            try {
                resources = getMetadataResources(metadataUrl.getUrl());

            } catch (IOException | URISyntaxException e) {
                messageTracker.addWarningMessage("No " + getMetadataLocation() + " metadata found for '"
                        + metadataUrl.getArtifactId() + "', skipping...");

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

    protected List<URL> getMetadataResources(String name) throws IOException, URISyntaxException {
        List<URL> metadataResources = null;
        if (name.contains(".jar")) {
            metadataResources = getMetadataResourceFromJar(name);
        } else {
            getMetadataResourcesFromDirectory(name);
        }
        return metadataResources;
    }

    private List<URL> getMetadataResourceFromJar(String name) throws IOException {
        List<URL> metadataResources = new ArrayList<>();
        name = "jar:" + name + "!/";
        URL jarUrl = new URL(name);
        JarURLConnection jarConnection = (JarURLConnection) jarUrl.openConnection();
        JarFile file = jarConnection.getJarFile();
        Enumeration<JarEntry> e = file.entries();
        while (e.hasMoreElements()) {
            JarEntry newEntry = e.nextElement();
            String entryName = newEntry.getName();
            if (entryName.startsWith(this.getMetadataLocation()) && entryName.endsWith(METAMODEL_SUFFIX)) {
                metadataResources.add(new URL(name + newEntry.getName()));
            }
        }

        return metadataResources;
    }

    private List<URL> getMetadataResourcesFromDirectory(String name) throws URISyntaxException, MalformedURLException {
        List<URL> metadataResources = new ArrayList<>();
        File metamodelDir = new File(new URI(name + this.getMetadataLocation()));
        File[] files = metamodelDir.listFiles();
        File file = null;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                if (file.getName().endsWith(METAMODEL_SUFFIX)) {
                    metadataResources.add(file.toURI().toURL());
                }
            }
        }

        return metadataResources;
    }

    protected abstract String getMetadataLocation();

    private void loadMetamodelFile(InputStream stream) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            T instance = objectMapper.readValue(stream, getMetamodelClass());
            addMetadataElement(instance);

        } catch (IOException e) {
            throw new FermenterException("Problem reading metamodel!", e);

        }

    }

    protected abstract Class<T> getMetamodelClass();

    protected void postLoadMetamodel() {
        if (log.isInfoEnabled()) {
            log.info("Loaded " + completeMetadataMap.size() + " " + getMetamodelClass().getSimpleName());
        }
    }

    protected Map<String, T> getMetadataMap(String packageName) {
        return metadataByPackageMap.get(packageName);
    }

    protected Map<String, T> getCompleteMetadataMap() {
        return completeMetadataMap;
    }

    protected void addMetadataElement(T element) {
        String packageName = element.getPackage();
        String name = element.getName();
        Map<String, T> packageMap = getMetadataMap(packageName);
        if (packageMap == null) {
            packageMap = new HashMap<>();
            metadataByPackageMap.put(packageName, packageMap);
        }
        getMetadataMap(packageName).put(name, element);
        completeMetadataMap.put(name, element);

    }

    public T getMetadataElementByPackageAndName(String applicationName, String name) {
        Map<String, T> metadataMap = getMetadataMap(applicationName);
        return (metadataMap != null) ? metadataMap.get(name) : null;
    }

    public Map<String, T> getMetadataElementByPackage(String applicationName) {
        Map<String, T> enumerationMap = getMetadataMap(applicationName);
        return (enumerationMap != null) ? enumerationMap : Collections.emptyMap();
    }

    public T getMetadataElementByName(String name) {
        return completeMetadataMap.get(name);
    }

    public Map<String, T> getMetadataElementWithoutPackage() {
        return completeMetadataMap;
    }

}
