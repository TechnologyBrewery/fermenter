package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Common utility methods that are leverages across metamodel classes.
 */
public final class MetamodelUtils {

    protected MetamodelUtils() {
        // prevent instantiation of all static class
    }

    /**
     * Checks that a instance that will be wrapped by a decorator is not null and provides a consistent error response.
     * 
     * @param wrapperClass
     *            class that is being wrapped
     * @param wrappedInstance
     *            instance that is being wrapped
     */
    static void validateWrappedInstanceIsNonNull(Class<?> wrapperClass, Object wrappedInstance) {
        if (wrappedInstance == null) {
            throw new IllegalArgumentException(
                    wrapperClass.getSimpleName() + " must be instatiated with a non-null instance to wrap!");
        }
    }

}
