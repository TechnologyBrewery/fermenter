package org.bitbucket.fermenter.mda.metadata;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.bitbucket.fermenter.mda.metadata.element.Message;
import org.bitbucket.fermenter.mda.metadata.element.MessageMetadata;
import org.bitbucket.fermenter.mda.metadata.element.MessageTextMetadata;
import org.bitbucket.fermenter.mda.xml.TrackErrorsErrorHandler;
import org.xml.sax.EntityResolver;

public final class MessagesMetadataManager {

	private static final String DEFAULT_MESSAGES = "/default-messages.xml";
	private static final String APPLICATION_MESSAGES = "messages.xml";
	private static final String ENTITY_RESOLVER_PROPERTIES = "entityResolver.properties";
	private static final Log LOG = LogFactory.getLog(MessagesMetadataManager.class);
	private static final MessagesMetadataManager INSTANCE;
	
	static {
		INSTANCE = new MessagesMetadataManager();
	}
	
	private Map<String, Message> messages;
	private Set<String> locales;
	
	private MessagesMetadataManager() {
		super();
		
		messages = new HashMap<String, Message>();
		locales = new HashSet<String>();
		locales.add(Message.DEFAULT_LOCALE);
	}
		
	public void reset() {
        // TODO: see issue #16 and #17 to eliminate the need for this in the next version:
		messages = new HashMap<>();
	}

	public static MessagesMetadataManager getInstance() {
		return INSTANCE;
	}
	
	public Collection<Message> getAllMessages() {
		return Collections.unmodifiableCollection(messages.values());
	}
	
	public Collection<String> getAllLocales() {
		return Collections.unmodifiableCollection(locales);		
	}
	
	public void addMessage(Message msg) {
		messages.put(msg.getKey(), msg);
		locales.addAll(msg.getLocales());
	}
	
	public Message getMessage(String key) {
		Message msg = (Message) messages.get(key);
		
		if (msg == null) {
			throw new IllegalArgumentException("No message found for key " + key);
		}
		
		return msg;
	}
	
	private void configure(String url) {
		TrackErrorsErrorHandler handler = new TrackErrorsErrorHandler(LOG);
		
		try {
			CatalogManager catalogManager = new CatalogManager( ENTITY_RESOLVER_PROPERTIES );
			EntityResolver resolver = new CatalogResolver(catalogManager);
			
			// First get default messages
			loadDefaultMessages(resolver, handler);

			// Now load messages for the current app
			loadApplicationMessages(resolver, url, handler);
			
		} catch (Exception ex) {
			LOG.error("Unable to load messages metadata", ex);
			throw new RuntimeException("Unable to load messages metadata", ex);
		}
		
		if (handler.haveErrorsOccurred()) {
			LOG.error("Encountered XML parsing errors; check log for details");
			throw new RuntimeException("Encountered XML parsing errors; check log for details");
		}
	}

	private void loadApplicationMessages(EntityResolver resolver, String url, TrackErrorsErrorHandler handler) throws Exception {
		String path = url + APPLICATION_MESSAGES;		
		File file = new File( new URI(path) );
		if(!file.exists()){
			LOG.info("No " + APPLICATION_MESSAGES + " found. Skipping....");
			return;
		}
		
		URL resource = (file==null) ? null : file.toURI().toURL();
		if (resource != null) {
			LOG.info("Loading messages from: " + path);
			Digester digester = configureDigester(resolver, handler);
			InputStream is = resource.openStream();
			digester.parse(is);
			is.close();
		} else {
			LOG.info("Resource not found: " + path);
			LOG.info("Skipping custom message loading");
		}
	}

	private void loadDefaultMessages(EntityResolver resolver, TrackErrorsErrorHandler handler) throws Exception {
		LOG.info("Loading messages from: " + DEFAULT_MESSAGES);
		Digester digester = configureDigester(resolver, handler);
		InputStream is = null;
		try {
			is = MessagesMetadataManager.class.getResourceAsStream(DEFAULT_MESSAGES);
			if (is != null) {
				digester.parse(is);
				
			} else {
				LOG.warn("Default messages were not found at " + DEFAULT_MESSAGES + " and will not be available!");
				
			}
			
		} finally {
			IOUtils.closeQuietly(is);
			
		}
	}

	void loadMetadata(String url) {
		INSTANCE.configure(url);		
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
			digester.addObjectCreate("messages/message", MessageMetadata.class);
			digester.addSetProperties("messages/message");
			digester.addObjectCreate("messages/message/summary", MessageTextMetadata.class);
			digester.addSetProperties("messages/message/summary");
			digester.addCallMethod("messages/message/summary", "setText", 0);
			digester.addSetNext("messages/message/summary", "addSummary");
			digester.addObjectCreate("messages/message/detail", MessageTextMetadata.class);
			digester.addSetProperties("messages/message/detail");
			digester.addCallMethod("messages/message/detail", "setText", 0);
			digester.addSetNext("messages/message/detail", "addDetail");
			digester.addSetNext("messages/message", "addMessage");
		}

	}

}
