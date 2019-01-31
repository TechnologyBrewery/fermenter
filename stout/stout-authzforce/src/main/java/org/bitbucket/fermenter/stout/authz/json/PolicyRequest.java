package org.bitbucket.fermenter.stout.authz.json;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents a resource and action pair for which to request a policy decision. Typically, this will include the
 * following: * A simple name by which to represent the policy rule or claim name * A representative resource/action
 * pair that creates a decision pursuant to that rule/claim
 */
public class PolicyRequest extends AbstractAuthorizationRequest {

    public static final String ANY = "";

    private String name;
    private String resource;
    private String action;

    public PolicyRequest() {
        // default constructor
    }

    public PolicyRequest(String name, String resource, String action) {
        this.name = name;
        this.resource = resource;
        this.action = action;
    }

    /**
     * Returns the name or blends the resource and action to create a name, if none is found.
     * 
     * @return name of this attribute
     */
    public String getName() {
        String localName = this.name;
        if (StringUtils.isBlank(localName)) {
            localName = getResource() + "^" + getAction();
        }
        return localName;
    }

    public void setName(String name) {
        this.name = name;
    } 

    public String getResource() {
        return StringUtils.isNotBlank(resource) ? resource : ANY;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return StringUtils.isNotBlank(action) ? action : ANY;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }       
    
    /**
     * {@inheritDoc}
     */
    @Override
    ClaimType getClaimType() {
        return ClaimType.POLICY;
    }    

}