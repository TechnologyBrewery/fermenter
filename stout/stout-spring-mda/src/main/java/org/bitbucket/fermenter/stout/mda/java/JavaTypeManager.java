package org.bitbucket.fermenter.stout.mda.java;

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
 * Responsible for maintaining mappings from logical type names to Java types.
 */
public class JavaTypeManager {

    private static final Log LOG = LogFactory.getLog(JavaTypeManager.class);
    private static final String ENTITY_RESOLVER_PROPERTIES = "entityResolver.properties";
    private static final String TYPES = "/types.xml";
    private static JavaTypeManager INSTANCE = new JavaTypeManager();
    
    private Map<String, String> typeMap = new HashMap<String, String>();
    private EntityResolver resolver;

    public static JavaTypeManager getInstance() {
        return INSTANCE;
    }

    private JavaTypeManager() {
        // create the entity resolver to ensure proper DTD resolution
        // regardless of whether or not the system id specified in an
        // xml file is valid:
        CatalogManager catalogManager = new CatalogManager(ENTITY_RESOLVER_PROPERTIES);
        resolver = new CatalogResolver(catalogManager);

        load();
    }

    /**
     * Load in the metadata from the associated file
     *
     */
    protected void load() {
        LoggingErrorHandler errorHandler = new LoggingErrorHandler(LOG);
        Digester digester = getNewDigester(resolver, new File(TYPES), errorHandler);

        digester.push(this);
        digester.addCallMethod("types/type", "addType", 2);
        digester.addCallParam("types/type/name", 0);
        digester.addCallParam("types/type/java", 1);

        try {
            digester.parse(JavaTypeManager.class.getResourceAsStream(TYPES));
        } catch (IOException | SAXException e) {
            LOG.error("Unexpected issue loading types!", e);
        }

    }

    private Digester getNewDigester(EntityResolver resolver, File fileToParse, LoggingErrorHandler errorHandler) {
        // Digester's javadoc specifically suggests that a new instance
        // should be created even though Digester has a clear() method:
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

    public void addType(String name, String javaClass) {
        getTypeMap().put(name, javaClass);
    }

    public static String getJavaType(String name) {
        return (String) getInstance().getTypeMap().get(name);
    }
}
