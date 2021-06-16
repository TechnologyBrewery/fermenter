package org.bitbucket.fermenter.mda.metamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Container and lookup capability for different {@link ModelInstanceRepository} repositories.
 */
public final class ModelInstanceRepositoryManager {

    private static ThreadLocal<Map<String, Object>> threadBoundInstance = ThreadLocal.withInitial(HashMap::new);

    private ModelInstanceRepositoryManager() {
        // prevent private instantiation of all static class
    }

    /**
     * Adds a repository. Only one repository of each type will be kept.
     * 
     * @param respository
     *            repository to add
     */
    public static void setRepository(ModelInstanceRepository respository) {
        Map<String, Object> instanceMap = threadBoundInstance.get();
        instanceMap.put(respository.getClass().toString(), respository);
    }

    /**
     * Returns the stored repository for a given class.
     * 
     * @param type
     *            class to lookup
     * @return instance of that class
     */
    public static <V> V getMetamodelRepository(Class<V> type) {
        Map<String, Object> instanceMap = threadBoundInstance.get();
        return type.cast(instanceMap.get(type.toString()));
    }

    /**
     * For testing only.
     */
    static void clear() {
        threadBoundInstance.remove();
    }

}
