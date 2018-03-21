package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Represents a constant that is defined within a specific {@link Enumeration}.
 */
public class Enum extends MetamodelElement {

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        // nothing specific to validate that isn't handled at JSON load time already
    }

}
