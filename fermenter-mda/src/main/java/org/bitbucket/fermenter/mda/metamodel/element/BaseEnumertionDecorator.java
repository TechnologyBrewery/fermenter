package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.List;

public class BaseEnumertionDecorator implements Enumeration {

    protected Enumeration wrapped;
    
    public BaseEnumertionDecorator(Enumeration enumerationToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), enumerationToDecorate);
        wrapped = enumerationToDecorate;
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }

    @Override
    public String getPackage() {
        return wrapped.getPackage();
    }

    @Override
    public List<Enum> getEnums() {
        return wrapped.getEnums();
    }

    @Override
    public void validate() {
        wrapped.validate();
        
    }

    @Override
    public Integer getMaxLength() {
        return wrapped.getMaxLength();
    }
    
}
