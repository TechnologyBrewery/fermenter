package org.bitbucket.fermenter.mda.metamodel;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.util.MessageTracker;

/**
 * Defines the different contexts that can be used by a target.
 * 
 * <pre>
 * * ALL -      Indicates that all model instances registered in the plugin's metadataDependencies will be used.
 * * LOCAL -    Indicates that only model instances that lives in the current artifactId will be used. 
 * * TARGETED - Indicates that only model instances that are part of the registered artifactIds in the plugin's 
 *              targetModelInstances will be used.
 * </pre>
 */
public enum ModelContext {
    ALL, LOCAL, TARGETED;
    
    /**
     * Determines whether or not a context setting denotes should only use local model instances.
     * @param modelInstanceContext context to check
     * @return true if local only
     */
    public static boolean useLocalModelInstancesOnly(String modelInstanceContext) {
        boolean useLocalModelInstnacesOnly;
        if (StringUtils.isBlank(modelInstanceContext)
                || ALL.equalsIgnoreCase(modelInstanceContext)
                || TARGETED.equalsIgnoreCase(modelInstanceContext)) {
            useLocalModelInstnacesOnly = false;

        } else if (LOCAL.equalsIgnoreCase(modelInstanceContext)) {
            useLocalModelInstnacesOnly = true;

        } else {            
            MessageTracker messageTracker = MessageTracker.getInstance();
            messageTracker.addErrorMessage("An invalid model instance context of '" + modelInstanceContext
                    + "' has been specified.  Using 'all' instead - BUT YOU NEED TO FIX THIS ON THE TARGET");
            useLocalModelInstnacesOnly = false;
        }

        return useLocalModelInstnacesOnly;
    }

    
    /**
     * Determines whether or not a context setting denotes should only use targeted model instances.
     * @param modelInstanceContext context to check
     * @return true if targeted only
     */
    public static boolean useTargetedModelInstances(String metadataContext) {
        return (TARGETED.equalsIgnoreCase(metadataContext));
    }
    
    /**
     * Checks string equality, ignoring case, with the name of the enum.
     * @param value string to compare
     * @return equality result
     */
    public boolean equalsIgnoreCase(String value) {
        return this.name().equalsIgnoreCase(value);
    }
}