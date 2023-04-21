package org.technologybrewery.fermenter.stout.authz.json;

/**
 * Represents a request for an attribute value to be returned for a claim.
 */
public class AttributeRequest extends AbstractAuthorizationRequest {
    
    private String requestedAttributeId;
    
    public AttributeRequest(String requestedAttributeId) {
        this.requestedAttributeId = requestedAttributeId;
    }
    
    public String getRequestedAttributeId() {
        return this.requestedAttributeId;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    ClaimType getClaimType() {
        return ClaimType.ATTRIBUTE;
    }

}
