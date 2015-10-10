package org.bitbucket.fermenter.mda.metadata.element;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MessageMetadata implements Message {

	private String key;
	private Map summaries;
	private Map details;

	public MessageMetadata() {
		super();
		
		summaries = new HashMap();
		details = new HashMap();
	}
	
	public void addDetail(MessageText detail) {
		details.put(detail.getLocale(), detail);
	}
	
	public void addSummary(MessageText summary) {
		summaries.put(summary.getLocale(), summary);
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public MessageText getDetail(String locale) {
		MessageText text = (MessageText) details.get(locale);
		return text != null ? text : getDefaultDetail();
	}

	public Collection getLocales() {
		Set locales = new HashSet();
		
		for (Iterator i = summaries.keySet().iterator(); i.hasNext();) {
			locales.add(i.next());
		}
		
		for (Iterator i = details.keySet().iterator(); i.hasNext();) {
			locales.add(i.next());
		}
		
		return Collections.unmodifiableCollection(locales);
	}

	public MessageText getSummary(String locale) {
		MessageText text = (MessageText) summaries.get(locale);
		return text != null ? text : getDefaultSummary();
	}
	
	private MessageText getDefaultDetail() {
		MessageText text = (MessageText) details.get(DEFAULT_LOCALE);
		return text != null ? text : getDefaultSummary();
	}
	
	private MessageText getDefaultSummary() {
		return (MessageText) summaries.get(DEFAULT_LOCALE);
	}

	
}
