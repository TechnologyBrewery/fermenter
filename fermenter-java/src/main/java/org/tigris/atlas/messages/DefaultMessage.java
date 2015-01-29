package org.tigris.atlas.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Default implementation of the {@link Message} interface.
 */
public class DefaultMessage implements Message {

	private static final long serialVersionUID = -8763173306121355462L;

	private String key;
	private Severity severity;
	private List<Object> inserts;
	private Set<String> properties;

	/**
	 * Create a new message instance, with no key, severity, properties, or inserts.
	 */
	public DefaultMessage() {
		super();

		inserts = new ArrayList<Object>();
		properties = new HashSet<String>();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getKey() {
		return key;
	}

	/**
	 * {@inheritDoc}
	 */
	public Severity getSeverity() {
		return severity;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getProperties() {
		return Collections.unmodifiableCollection(properties);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Object> getInserts() {
		return Collections.unmodifiableCollection(inserts);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addProperty(String property) {
		properties.add(property);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addInsert(Object insert) {
		inserts.add(insert);
	}

	/**
	 * Custom equality check. Two messages are equal if and only if they have the same key, the same severity, and the
	 * same properties. The order of properties is not considered.
	 * 
	 * @return True of the parameter object equals this message, false otherwise
	 */
	public boolean equals(Object o) {
		try {
			DefaultMessage that = (DefaultMessage) o;

			// This never equals a null value
			if (that == null) {
				return false;
			}

			// First, ensure that the key and severity are the same
			boolean areEqual = equalsBasedOnSeverityAndKey(that);

			// Next, make sure the properties are the same (ignoring order)
			if (areEqual) {
				areEqual = equalsBasedOnProperties(that);
			}

			return areEqual;
		} catch (ClassCastException ex) {
			return false;
		}
	}

	private boolean equalsBasedOnSeverityAndKey(DefaultMessage that) {
		if (that == null) {
			return false;
		}

		String thisKey = this.getKey();
		String thatKey = that.getKey();
		Severity thisSeverity = this.getSeverity();
		Severity thatSeverity = that.getSeverity();

		// Keys are null - do a reference equals check
		if (thisKey == null && thatKey == null) {
			return this == that;
		}

		// Severities are null - do a reference equals check
		if (thisSeverity == null && thatSeverity == null) {
			return this == that;
		}

		// Exactly one key is null - return false
		if ((thisKey == null && thatKey != null) || (thisKey != null && thatKey == null)) {
			return false;
		}

		// Exactly one Severity is null - return false
		if ((thisSeverity == null && thatSeverity != null) || (thisSeverity != null && thatSeverity == null)) {
			return false;
		}

		// All keys/severities are non-null, do do a .equals check on both
		return thisKey.equals(thatKey) && thisSeverity.equals(thatSeverity);
	}

	private boolean equalsBasedOnProperties(DefaultMessage that) {
		if (that == null) {
			return false;
		}

		Collection<String> thisProperties = this.getProperties();
		Collection<String> thatProperties = that.getProperties();

		// The properties are of different sizes - return false
		if (thisProperties.size() != thatProperties.size()) {
			return false;
		}

		// Objects are equal if they contain the same properties, ignoring order
		for (Iterator<String> i = thisProperties.iterator(); i.hasNext();) {
			if (!thatProperties.contains(i.next())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Custom hash code calculation algorithm. The hash code for a message is determined by the key, severity, and
	 * properties. As with <code>
	 * equals</code>, property ordering is irrelevant, i.e. two messages with the same key and severity and properties
	 * will return a consistent hash code value no matter how the properties are ordered. If the message does not
	 * contain a key and severity, the default Java object hash code is returned.
	 * 
	 * @return The hash code value for this message
	 */
	public int hashCode() {
		// Build up the hashCode for this message.
		// The code is based off of the severity, key, and properties.
		// Note: severity and key must be specified in order for this
		// to be meaningful - if either is missing, the hashCode returned
		// is the hashCode returned from super.

		if (getKey() == null || getSeverity() == null) {
			return super.hashCode();
		}

		int result = 17;
		result = (37 * result) + getKey().hashCode();
		result = (37 * result) + getSeverity().hashCode();

		for (Iterator<String> i = properties.iterator(); i.hasNext();) {
			result = (37 * result) + i.next().hashCode();
		}

		return result;
	}

	/**
	 * Return a more friendly representation of the message.
	 * 
	 * @return The string representation of this message
	 */
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}

}
