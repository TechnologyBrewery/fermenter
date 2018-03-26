package org.bitbucket.fermenter.mda.metamodel.element;

public class BaseEnumDecorator implements Enum {

    protected Enum wrapped;
    
    public BaseEnumDecorator(Enum enumToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), enumToDecorate);
        wrapped = enumToDecorate;
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }    
    
}
