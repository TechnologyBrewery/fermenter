package org.bitbucket.fermenter.mda.metamodel;

import java.util.HashMap;
import java.util.Map;

public final class MetadataRepositoryManager {

    private static Map<String, Object> instanceMap = new HashMap<>();

    private MetadataRepositoryManager() {

    }

    public static void setRepository(MetadataRepository respository) {
        instanceMap.put(respository.getClass().toString(), respository);
    }

    public static <V> V getMetadataRepostory(Class<V> type) {
        return type.cast(instanceMap.get(type.toString()));
    }

    static void clear() {
        instanceMap = new HashMap<>();
    }

}
