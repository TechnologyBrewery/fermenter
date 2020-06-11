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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.exception.FermenterException;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.element.Metamodel;
import org.bitbucket.fermenter.mda.metamodel.element.MetamodelElement;
import org.bitbucket.fermenter.mda.metamodel.element.NamespacedMetamodel;
import org.bitbucket.fermenter.mda.util.JsonUtils;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The bulk of metamodel management in a single abstract class. This base
 * functionality is intended to provide common loading and and accessibility of
 * metadata related to a specific metamodel.
 *
 * @param <T> An instance of {@link NamespacedMetamodel}
 */
public abstract class AbstractMetamodelManager<T extends NamespacedMetamodel> {

	private static final Log log = LogFactory.getLog(AbstractMetamodelManager.class);

	private static final String METAMODEL_SUFFIX = "json";
	private Map<String, Map<String, T>> metadataByPackageMap = new HashMap<>();
	private Map<String, Map<String, T>> metadataByArtifactIdMap = new HashMap<>();
	private Map<String, T> completeMetadataMap = new HashMap<>();

	private static MessageTracker messageTracker = MessageTracker.getInstance();

	protected ModelRepositoryConfiguration repoConfiguration;

	protected MetamodelConfig config = KrauseningConfigFactory.create(MetamodelConfig.class);

	/**
	 * Resets this instance to ensure a clean set of metadata is available.
	 */
	public void reset() {
		metadataByPackageMap = new HashMap<>();
		metadataByArtifactIdMap = new HashMap<>();
		completeMetadataMap = new HashMap<>();
	}

	/**
	 * Validation occurs after the loading of all metadata to ensure that we can
	 * access all metadata in a safe fashion, without having to worry to about what
	 * is already loaded and what needs to be loaded.
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
	 * @param elements The instances to validate
	 */
	protected static <T extends Metamodel> void validateElements(Collection<T> elements) {
		if (!CollectionUtils.isEmpty(elements)) {
			for (T element : elements) {
				element.validate();
			}
		}
	}

	protected void loadMetadata(ModelInstanceUrl modelInstanceUrl, ModelRepositoryConfiguration repoConfiguration) {
        this.repoConfiguration = repoConfiguration;
        if (StringUtils.isBlank(modelInstanceUrl.getUrl())) {
            messageTracker.addErrorMessage("Model instance for artifactId '" + modelInstanceUrl.getArtifactId()
                    + "' can not be found!  Please ensure the proper jar is on your classpath.");

        } else {
            List<URL> resources = null;

            try {
                resources = getMetadataResources(modelInstanceUrl.getUrl());

            } catch (IOException | URISyntaxException e) {
                log.error("Problem encountered loading model instances for " + modelInstanceUrl.getArtifactId(), e);
                messageTracker.addWarningMessage("No " + getMetadataLocation() + " metadata found for '"
                        + modelInstanceUrl.getArtifactId() + "', skipping...");

            }

            if (resources == null) {
                return;
            }
           
            for (URL resource : resources) {
                InputStream is = null; 
                try {
                	is = resource.openStream();
                    loadMetamodelFile(is, modelInstanceUrl.getArtifactId(), resource);
	
	            } catch (IOException | GenerationException e) {
	                log.error("Problem encountered loading model instance " + resource.toExternalForm(), e);
	                messageTracker.addErrorMessage("Problem loading" + resource.toExternalForm()
	                        + " model instance found in '" + modelInstanceUrl.getArtifactId() + "', skipping...");
	                
	            } finally {
	                IOUtils.closeQuietly(is);
	
	            }
            }

            postLoadMetamodel();
        }
    }

	protected List<URL> getMetadataResources(String name) throws IOException, URISyntaxException {
		List<URL> metadataResources = null;
		if (name.contains(".jar")) {
			metadataResources = getMetadataResourceFromJar(name);
		} else {
			metadataResources = getMetadataResourcesFromDirectory(name);
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
			if (entryName.startsWith(this.getMetadataLocation()) && entryName.endsWith("." + METAMODEL_SUFFIX)) {
				metadataResources.add(new URL(name + newEntry.getName()));
			}
		}

		return metadataResources;
	}

	private List<URL> getMetadataResourcesFromDirectory(String name) throws URISyntaxException, IOException {
		List<URL> metadataResources = new ArrayList<>();
		File metamodelDir = new File(new URI(name + this.getMetadataLocation()));
		if (metamodelDir.isDirectory()) {
			String[] suffixFilter = { METAMODEL_SUFFIX };
			Collection<File> files = FileUtils.listFiles(metamodelDir, suffixFilter, true);
			if (CollectionUtils.isNotEmpty(files)) {
				for (File file : files) {
					metadataResources.add(file.toURI().toURL());
				}
			}

		} else {
			log.warn(metamodelDir.getCanonicalPath() + " is not a valid directory!");

		}

		return metadataResources;
	}

	/**
	 * Returns the location *within* the base metadata directory to search for
	 * metadata related to this specific metamodel.
	 * 
	 * @return relative path (e.g., ./entities)
	 */
	protected abstract String getMetadataLocation();

	private void loadMetamodelFile(InputStream stream, String artifactId, URL resource) {
		ObjectMapper objectMapper = JsonUtils.getObjectMapper();
		try {
			T instance = objectMapper.readValue(stream, getMetamodelClass());
			((MetamodelElement) instance).setFileUrl(resource);
			addMetadataElement(instance, artifactId);

		} catch (IOException e) {
			throw new FermenterException("Problem reading metamodel!", e);

		}

	}

	/**
	 * The metamodel class into which metadata should be read.
	 * 
	 * @return metamodel deserialization target
	 */
	protected abstract Class<? extends T> getMetamodelClass();

	/**
	 * The metamodel name for use in log output (e.g., Enumeration, Entity).
	 * 
	 * @return metamodel string descriptor
	 */
	protected abstract String getMetamodelDescription();

	protected void postLoadMetamodel() {
		if (log.isInfoEnabled()) {
			log.info("Loaded " + completeMetadataMap.size() + " " + getMetamodelDescription() + "(s)");
		}
	}

	protected Map<String, T> getMetadataMap(String packageName) {
		return metadataByPackageMap.get(packageName);
	}

	protected Map<String, T> getMetadataByArtifactIdMap(String artifactId) {
		return metadataByArtifactIdMap.get(artifactId);
	}

	protected Map<String, T> getCompleteMetadataMap() {
		return completeMetadataMap;
	}

	protected void addMetadataElement(T element, String artifactId) {
		String packageName = element.getPackage();
		String name = element.getName();
		Map<String, T> packageMap = getMetadataMap(packageName);
		if (packageMap == null) {
			packageMap = new HashMap<>();
			metadataByPackageMap.put(packageName, packageMap);
		} else if (packageMap.containsKey(name)) {
		    log.warn("Metamodel " + name + " exists for package " + packageName + ". Replacing...");
		    messageTracker.addWarningMessage("Metamodel " + name + " exists for package " + packageName + ". Replacing...");
		}
		packageMap.put(name, element);

		Map<String, T> artifactIdMap = getMetadataByArtifactIdMap(artifactId);
		if (artifactIdMap == null) {
			artifactIdMap = new HashMap<>();
			metadataByArtifactIdMap.put(artifactId, artifactIdMap);
		} else if (artifactIdMap.containsKey(name)) {
		    log.warn("Metamodel " + name + " exists for artifact Id " + artifactId + ". Replacing...");
		    messageTracker.addWarningMessage("Metamodel " + name + " exists for artifact Id " + artifactId + ". Replacing...");
		}
		artifactIdMap.put(name, element);

		if (getCompleteMetadataMap().containsKey(name)) {
		    log.warn("Metamodel " + name + " exists. Replacing...");
		    messageTracker.addWarningMessage("Metamodel " + name + " exists. Replacing...");
		}
		completeMetadataMap.put(name, element);

	}

	/**
	 * Returns a metadata instance of this metamodel by package name and element
	 * name.
	 * 
	 * @param packageName package
	 * @param name        name of element
	 * @return instance of element or null if no instance exists
	 */
	public T getMetadataElementByPackageAndName(String packageName, String name) {
		Map<String, T> metadataMap = getMetadataMap(packageName);
		return (metadataMap != null) ? metadataMap.get(name) : null;
	}

	/**
	 * Returns a metadata instance of this metamodel by artifact id and element
	 * name.
	 * 
	 * @param artifactId artifact id
	 * @param name       name of element
	 * @return instance of element or null if no instance exists
	 */
	public T getMetadataElementByArtifactIdAndName(String artifactId, String name) {
		Map<String, T> metadataMap = getMetadataByArtifactIdMap(artifactId);
		return (metadataMap != null) ? metadataMap.get(name) : null;
	}

	/**
	 * Returns all metadata instances of this metamodel by package name.
	 * 
	 * @param packageName package
	 * @return instance of element or an empty map is no instances exist
	 */
	public Map<String, T> getMetadataElementByPackage(String packageName) {
		Map<String, T> byPackageMap = getMetadataMap(packageName);
		return (byPackageMap != null) ? byPackageMap : Collections.emptyMap();
	}

	/**
	 * Returns all metadata instances of this metamodel (regardless of package
	 * name).
	 * 
	 * @return instance of element or an empty map is no instances exist
	 */
	public Map<String, T> getMetadataElementWithoutPackage() {
		return completeMetadataMap;
	}

	/**
	 * Retrieves services based on a generation context.
	 * 
	 * @param context type of generation target context being used
	 * @return map of services
	 */
	public Map<String, T> getMetadataElementByContext(String context) {
		Map<String, T> metamodelInstanceMap;
		if (ModelContext.useLocalModelInstancesOnly(context)) {
			metamodelInstanceMap = getMetadataByArtifactIdMap(repoConfiguration.getCurrentApplicationName());

		} else if (ModelContext.useTargetedModelInstances(context)) {
			metamodelInstanceMap = new HashMap<>();
			List<String> targetedArtifactIds = repoConfiguration.getTargetModelInstances();
			for (String artifactId : targetedArtifactIds) {
				Map<String, T> targetedModelMap = getMetadataByArtifactIdMap(artifactId);
				if (targetedModelMap == null || targetedModelMap.size() == 0) {
					log.debug("No instances were found for targeted artifactId '" + artifactId + "'");
				} else {
				    metamodelInstanceMap.putAll(targetedModelMap);    
				}
			}

		} else {
			throw new GenerationException("Invalid context being requested '" + context + "'!");

		}

		if (metamodelInstanceMap == null) {
			metamodelInstanceMap = Collections.emptyMap();
		}

		return metamodelInstanceMap;
	}

}
