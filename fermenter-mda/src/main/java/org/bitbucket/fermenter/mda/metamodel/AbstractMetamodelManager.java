package org.bitbucket.fermenter.mda.metamodel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.element.MetadataElement;
import org.bitbucket.fermenter.mda.metamodel.element.MetamodelElement;
import org.bitbucket.fermenter.mda.util.MessageTracker;
import org.bitbucket.fermenter.mda.xml.TrackErrorsErrorHandler;
import org.bitbucket.fermenter.mda.xml.XmlUtils;

public abstract class AbstractMetamodelManager<T extends MetamodelElement> {

    private Map<String, Map<String, T>> metadataByArtifactIdMap = new HashMap<>();
    private Map<String, T> completeMetadataMap = new HashMap<>();
    protected String currentArtifactId;

    private static MessageTracker messageTracker = MessageTracker.getInstance();

    /**
     * Resets this instance to ensure a clean set of metadata is available.
     */
    public void reset() {
        metadataByArtifactIdMap = new HashMap<>();
        completeMetadataMap = new HashMap<>();
    }

    /**
     * Validation occurs after the loading of all metadata to ensure that we can access all metadata in a safe fashion,
     * without having to worry to about what is already laoded and what needs to be loaded.
     */
    protected final void validate() {
        for (String artifactId : metadataByArtifactIdMap.keySet()) {
            Collection<T> metadataItems = getMetadataMap(artifactId).values();
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

    protected void loadMetadata(String appName, String url) {
        if (StringUtils.isBlank(url)) {
            messageTracker.addErrorMessage("Metadata for application '" + appName
                    + "' can not be found!  Please ensure the proper jar is on your classpath.");

        } else {
            currentArtifactId = appName;
            metadataByArtifactIdMap.put(currentArtifactId, new HashMap<>());

            List resources = null;

            try {
                resources = getMetadataResources(url);
            } catch (IOException | URISyntaxException e) {
                messageTracker.addWarningMessage("No " + getMetadataLocation() + " metadata found for '" + appName + "', skipping...");
               
            }

            if (resources == null) {
                return;
            }

            try {
                Iterator iterator = resources.iterator();
                while (iterator.hasNext()) {

                    loadMetadataFile(((URL) iterator.next()).openStream());
                }
            } catch (Exception ex) {
                throw new RuntimeException("Error while loading metadata", ex);
            }

            postLoadMetadata();
        }
    }

    protected List getMetadataResources(String name) throws IOException, URISyntaxException {
        List metadataResources = new ArrayList();
        if (name.contains(".jar")) {
            name = "jar:" + name + "!/";
            URL jarUrl = new URL(name);
            JarURLConnection jarConnection = (JarURLConnection) jarUrl.openConnection();
            JarFile file = jarConnection.getJarFile();
            Enumeration e = file.entries();
            while (e.hasMoreElements()) {
                JarEntry newEntry = (JarEntry) e.nextElement();
                if (newEntry.getName().startsWith(this.getMetadataLocation())
                        && newEntry.getName().endsWith(METADATA_SUFFIX)) {
                    metadataResources.add(new URL(name + newEntry.getName()));
                }
            }
        } else {
            File metadataDir = new File(new URI(name + this.getMetadataLocation()));
            File[] files = metadataDir.listFiles();
            File file = null;
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    file = files[i];
                    if (file.getName().endsWith(METADATA_SUFFIX)) {
                        metadataResources.add(file.toURL());
                    }
                }
            }
        }
        return metadataResources;
    }

    protected abstract String getMetadataLocation();

    private void loadMetadataFile(InputStream stream) throws Exception {
        TrackErrorsErrorHandler handler = new TrackErrorsErrorHandler(log);

        Digester digester = XmlUtils.getNewDigester(handler);
        digester.push(this);
        initialize(digester);
        digester.parse(stream);

        if (handler.haveErrorsOccurred()) {
            throw new RuntimeException("XML parsing error(s) encountered; check log for details");
        }
    }

    protected void postLoadMetadata() {

    }

    /**
     * Returns the metadata map for the given artifactId.
     * 
     * @param artifactId
     *            the artifactId of the jar for which to return metadata
     * @return The metadata map for a specified application
     */
    protected Map<String, T> getMetadataMap(String artifactId) {
        return metadataByArtifactIdMap.get(artifactId);
    }

    protected Map getCompleteMetadataMap() {
        return completeMetadataMap;
    }

    /**
     * Add a metadata element to the current application map. Can only happen during metadata loading.
     *
     * @param name
     * @param me
     */
    protected void addMetadataElement(String name, MetadataElement me) {
        getMetadataMap(currentArtifactId).put(name, me);
        completeMetadataMap.put(name, me);

    }

}
