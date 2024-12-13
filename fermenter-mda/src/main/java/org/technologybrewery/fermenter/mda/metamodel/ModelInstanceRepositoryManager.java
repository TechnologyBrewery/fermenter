package org.technologybrewery.fermenter.mda.metamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Container and lookup capability for different {@link ModelInstanceRepository} repositories.
 */
public final class ModelInstanceRepositoryManager {

    private static ThreadLocal<Map<String, Object>> threadBoundInstance = ThreadLocal.withInitial(HashMap::new);

    private static final String ABSTRACT_MODEL_INSTANCE_REPOSITORY_CLASS_NAME = 
                                "org.technologybrewery.fermenter.mda.metamodel.AbstractModelInstanceRepository";

    private ModelInstanceRepositoryManager() {
        // prevent private instantiation of all static class
    }

    /**
     * Sets a repository as the repository implementation for its own class and any super 
     * classes if present. This enables downstream projects to safely extend upstream 
     * {@link ModelInstanceRepository}'s without breaking existing calls to getRepository().
     * 
     * For example, given the classes:
     *  MIR_A implements AbstractModelInstanceRepository
     *  MIR_B extends MIR_A
     *  MIR_C extends MIR_B
     * 
     * When the repository is set using setRepository(MIR_C). Then following calls to 
     * getRepository would all return the MIR_C instance:
     *  getMetamodelRepository(MIR_A.class) -> MIR_C
     *  getMetamodelRepository(MIR_B.class) -> MIR_C
     *  getMetamodelRepository(MIR_C.class) -> MIR_C
     * 
     * @param repository
     *            repository to add
     */
    public static void setRepository(ModelInstanceRepository repository) {
        Map<String, Object> instanceMap = threadBoundInstance.get();

        instanceMap.put(repository.getClass().toString(), repository);
        Class<?> repositorySuperClass = repository.getClass().getSuperclass();

        // Add an entry for all superclasses of the repository
        while (!repositorySuperClass.getName().equals(ABSTRACT_MODEL_INSTANCE_REPOSITORY_CLASS_NAME)) {
            instanceMap.put(repositorySuperClass.toString(), repository);
            repositorySuperClass = repositorySuperClass.getSuperclass();
        }
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
