package org.bitbucket.fermenter.mda.objectivec;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.bitbucket.fermenter.mda.xml.LoggingErrorHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

/**
 * Responsible for maintaining mappings from logical type names to Objective-C
 * types.
 */
public class ObjectiveCTypeManager {

    private static final Log LOGGER = LogFactory.getLog(ObjectiveCTypeManager.class);
    private static final String ENTITY_RESOLVER_PROPERTIES = "entityResolver.properties";
    private static final String TYPES = "/types-objectivec.xml";
    private static ObjectiveCTypeManager INSTANCE = new ObjectiveCTypeManager();

    private Map<String, String> typeMap = new HashMap<String, String>();
    private EntityResolver resolver;
    
    public static ObjectiveCTypeManager getInstance() {
        return INSTANCE;
    }

    private ObjectiveCTypeManager() {
        // Create the entity resolver to ensure proper DTD resolution regardless
        // of whether or not the system id specified in an xml file is valid
        CatalogManager catalogManager = new CatalogManager(ENTITY_RESOLVER_PROPERTIES);
        resolver = new CatalogResolver(catalogManager);

        load();
    }

    /**
     * Load in the metadata from the associated file.
     */
    protected void load() {
        LoggingErrorHandler errorHandler = new LoggingErrorHandler(LOGGER);
        Digester digester = getNewDigester(resolver, new File(TYPES), errorHandler);

        digester.push(this);
        digester.addCallMethod("types/type", "addType", 2);
        digester.addCallParam("types/type/name", 0);
        digester.addCallParam("types/type/objectivec", 1);

        try {
            digester.parse(ObjectiveCTypeManager.class.getResourceAsStream(TYPES));
        } catch (IOException | SAXException e) {
            LOGGER.error("Unexpected issue loading types!", e);
        }

    }

    private Digester getNewDigester(EntityResolver resolver, File fileToParse, LoggingErrorHandler errorHandler) {
        // Digester's documentation specifically suggests that a new instance
        // should be created even though Digester has a clear() method
        Digester digester = new Digester();
        digester.setEntityResolver(resolver);
        digester.setValidating(true);
        errorHandler.setFileName(fileToParse.getName());
        digester.setErrorHandler(errorHandler);
        return digester;
    }

    protected Map<String, String> getTypeMap() {
        return typeMap;
    }

    public void addType(String name, String objectiveCClass) {
        getTypeMap().put(name, objectiveCClass);
    }

    public static String getObjectiveCType(String name) {
        return getInstance().getTypeMap().get(name);
    }

    public static String getObjectiveCClassPrefix() {
        // TODO: load this from metadata
        return "Wino";
    }
}
