package org.technologybrewery.fermenter.stout.authz.json;

/**
 * Common aspect of a request for authorization information (e.g., a policy decision, attribute).
 *
 */
public abstract class AbstractAuthorizationRequest {

    /**
     * Returns the type of claim represented by this instance.
     * 
     * @return claim type
     */
    abstract ClaimType getClaimType();

    /**
     * Returns the types of claims that we can handle. Used to avoid costly instanceof checks.
     */
    enum ClaimType {
        POLICY, ATTRIBUTE;
    }

}
