package org.bitbucket.fermenter.stout.mda;

import org.bitbucket.fermenter.mda.metamodel.element.Return;

/**
 * Decorates a {@link Return} with Java-specific capabilities that focus on treating the operation as a *remote*
 * Java class. For instance, one that references TransferObjects instead of BusinessObjects.
 */
public class RemoteJavaReturn extends JavaReturn {
    
    /**
     * Creates a new Java-specific decorator.
     * @param returnToDecorate instance to decorate
     */
    public RemoteJavaReturn(Return returnToDecorate) {
        super(returnToDecorate);
        
    }
    
    /**
     * Returns the original type of return object, as specified in metadata, in its Java form.
     * 
     * @return The Java version of the return type
     */
    public String getJavaType() {
        return JavaElementUtils.getJavaShortNameByPackageAndType(getPackage(), getType(), false);
    }

}
