package org.tigris.atlas.mda.metadata.element;

public class MessageTextMetadata implements MessageText {

	private String text;
	private String locale;
	
	public MessageTextMetadata() {
		super();

		locale = Message.DEFAULT_LOCALE;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
