package org.bitbucket.fermenter.stout.authz;

/**
 * The interface that defines the contract for looking up attribute values. This can be a local or remote source. It
 * should be specified in the stout attribute definition json file for each attribute so that
 * {@link StoutAttributeProvider} can find the value for a specified attribute.
 * 
 * While any number of attributes can be used for lookups, almost all scenarios will be revolve around look up
 * attributes for specific subjects. As such, the interface will focus on that until a demand signal arises for more
 * complicated scenarios.
 * 
 * Implementations MUST have a no-argument constructor.
 */
public interface StoutAttributePoint {

    /**
     * Returns the valid for a specific attribute id. For our purposes, we will just use id along to determine the value
     * and not worry about category, etc.
     * 
     * @param attributeId
     *            attribute id
     * @param subject
     *            the subject for which to find the attribute
     * @return The attribute's value
     */
    AttributeValue<?> getValueForAttribute(String attributeId, String subject);

}
