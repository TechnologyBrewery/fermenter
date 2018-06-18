package org.bitbucket.fermenter.stout.authz;

/**
 * A library-egnostic view of possible policy decision values.  These values map directly to the XACML 3.0 standard.
 */
public enum PolicyDecision {

    PERMIT,
    DENY,
    NOT_APPLICABLE,
    INDETERMINATE;
    
}
