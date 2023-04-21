package org.technologybrewery.fermenter.ale.mda.generator.angular;

import org.technologybrewery.fermenter.mda.metamodel.element.MetamodelType;

import com.google.common.base.CaseFormat;

public class AngularImport {

    private String type;
    private String packageName;

    public AngularImport(String type, String packageName) {
        this.type = type;
        this.packageName = packageName;
    }

    public String getType() {
        return type;
    }
    
    public Boolean isEnumeration() {
        MetamodelType metamodelType = MetamodelType.getMetamodelType(packageName, type);
        return MetamodelType.ENUMERATION.equals(metamodelType);
    }
    
    public String getPackageName() {
        return packageName;
    }

    public String getTypeLowerHyphen() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, getType());
    }

    @Override
    public String toString() {
        return type;
    }
}
