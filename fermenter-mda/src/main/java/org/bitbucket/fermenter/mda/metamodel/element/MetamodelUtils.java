package org.bitbucket.fermenter.mda.metamodel.element;

public final class MetamodelUtils {

    protected MetamodelUtils() {
        // prevent instantiation of all static class
    }

    static void validateWrappedInstanceIsNonNull(Class<?> wrapperClass, Object wrappedInstance) {
        if (wrappedInstance == null) {
            throw new IllegalArgumentException(
                    wrapperClass.getSimpleName() + " must be instatiated with a non-null instance to wrap!");
        }
    }

}
