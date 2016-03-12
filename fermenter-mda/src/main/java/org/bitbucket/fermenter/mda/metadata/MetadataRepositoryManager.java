package org.bitbucket.fermenter.mda.metadata;

import java.util.HashMap;
import java.util.Map;

public class MetadataRepositoryManager {
    
    private static Map<String, Object> instanceMap = new HashMap<String, Object>();
    
    private MetadataRepositoryManager() {
        
    }
    
    public static void setRepository(AbstractMetadataRepository respository) {
        instanceMap.put(respository.getClass().toString(), respository);
    }
    
    public static <V> V getMetadataRepostory(Class<V> type) {
        return type.cast(instanceMap.get(type.toString()));
    }
    
    static void clear() {
    	instanceMap = new HashMap<String, Object>();
    }
    
}
