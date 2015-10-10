package org.bitbucket.fermenter.mda.metadata;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.bitbucket.fermenter.mda.metadata.element.Format;
import org.bitbucket.fermenter.mda.metadata.element.FormatMetadata;
import org.bitbucket.fermenter.mda.metadata.element.PatternMetadata;
import org.bitbucket.fermenter.mda.xml.TrackErrorsErrorHandler;
import org.xml.sax.EntityResolver;

public final class FormatMetadataManager {

	private static final String FORMATS = "formats.xml";
	private static final String ENTITY_RESOLVER_PROPERTIES = "entityResolver.properties";
	private static final Log LOG = LogFactory.getLog(FormatMetadataManager.class);
	private static final FormatMetadataManager INSTANCE;
	
	private Map formats;
	
	static {
		INSTANCE = new FormatMetadataManager();
	}
	
	public static FormatMetadataManager getInstance() {
		return INSTANCE;
	}
	
	private FormatMetadataManager() {
		super();
		
		formats = new HashMap();
	}
	
	void loadMetadata(String url) {
		configure(url);
	}
	
	private void configure(String url) {
		TrackErrorsErrorHandler handler = new TrackErrorsErrorHandler(LOG);
		
		try {
			CatalogManager catalogManager = new CatalogManager( ENTITY_RESOLVER_PROPERTIES );
			EntityResolver resolver = new CatalogResolver(catalogManager);

			// Now load messages for the current app
			loadAllFormats(resolver, url, handler);
			
			LOG.info("Loaded " + formats.size() + " format(s)");
		} catch (Exception ex) {
			LOG.error("Unable to load messages metadata", ex);
			throw new RuntimeException("Unable to load messages metadata", ex);
		}
		
		if (handler.haveErrorsOccurred()) {
			LOG.error("Encountered XML parsing errors; check log for details");
			throw new RuntimeException("Encountered XML parsing errors; check log for details");
		}
	}

	private void loadAllFormats(EntityResolver resolver, String url, TrackErrorsErrorHandler handler) throws Exception {
		String path = url + FORMATS;		
		File file = new File( new URI(path) );
		if(!file.exists()){
			LOG.info("No formats found at: " + file + ". Skipping....");
			return;
		}
		
		URL resource = file.toURL();
		LOG.info("Loading formats from: " + resource);
		
		if (resource != null) {
			Digester digester = configureDigester(resolver, handler);
			InputStream is = resource.openStream();
			digester.parse(is);
			is.close();
		} else {
			LOG.info("Resource not found: " + path);
			LOG.info("Skipping format loading");
		}
	}
	
	private Digester configureDigester(EntityResolver resolver, TrackErrorsErrorHandler handler) {
		Digester digester = new Digester();
		digester.setErrorHandler(handler);
		digester.setValidating(true);
		digester.setNamespaceAware(true);
		digester.setUseContextClassLoader(true);
		digester.addRuleSet(new RuleSet());
		digester.setEntityResolver(resolver);
		digester.push(this);
		return digester;
	}
	
	class RuleSet extends RuleSetBase {

		public void addRuleInstances(Digester digester) {
			digester.addObjectCreate("formats/format", FormatMetadata.class);
			digester.addSetProperties("formats/format");
			digester.addObjectCreate("formats/format/pattern", PatternMetadata.class);
			digester.addCallMethod("formats/format/pattern", "setText", 0);
			digester.addSetNext("formats/format/pattern", "addPattern");
			digester.addSetNext("formats/format", "addFormat");
		}

	}
	
	public void addFormat(Format format) {
		String name = format.getName();
		if (formats.containsKey(name)) {
			throw new IllegalArgumentException("Duplicate format name: " + name);
		}
		formats.put(name, format);
	}
	
	public Format getFormat(String name) {
		return (Format) formats.get(name);
	}
	
	public Collection getAllFormats() {
		return Collections.unmodifiableCollection(formats.values());
	}
	
}
