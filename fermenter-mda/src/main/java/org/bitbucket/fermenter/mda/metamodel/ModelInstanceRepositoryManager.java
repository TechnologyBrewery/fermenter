package org.bitbucket.fermenter.mda.metamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Container and lookup capability for different {@link ModelInstanceRepository} repositories.
 */
public final class ModelInstanceRepositoryManager {

    private static Map<String, Object> instanceMap = new HashMap<>();

    private ModelInstanceRepositoryManager() {

    }

    /**
     * Adds a repository.  Only one repository of each type will be kept.
     * @param respository repository to add
     */
    public static void setRepository(ModelInstanceRepository respository) {
        instanceMap.put(respository.getClass().toString(), respository);
    }

    /**
     * Returns the stored repository for a given class.
     * @param type class to lookup
     * @return instance of that class
     */
    public static <V> V getMetamodelRepository(Class<V> type) {
        return type.cast(instanceMap.get(type.toString()));
    }

    /**
     * For testing only.
     */
    static void clear() {
        instanceMap = new HashMap<>();
    }

}
