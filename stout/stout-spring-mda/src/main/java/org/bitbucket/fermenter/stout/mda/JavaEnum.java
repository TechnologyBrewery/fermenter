package org.bitbucket.fermenter.stout.mda;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metamodel.element.BaseEnumDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Enum;

public class JavaEnum extends BaseEnumDecorator implements Enum, JavaNamespacedElement {

    public JavaEnum(Enum enumToDecorate) {
        super(enumToDecorate);
    }

    /**
     * @return Returns the uppercased name.
     */
    public String getUppercasedName() {
        String name = getName();

        if (StringUtils.contains(name, '(')) {
            name = StringUtils.remove(name, '(');
        }
        if (StringUtils.contains(name, ')')) {
            name = StringUtils.remove(name, ')');
        }
        if (StringUtils.contains(name, '-')) {
            name = name.replace('-', '_');
        }
        if (StringUtils.contains(name, '\'')) {
            name = name.replace('\'', '_');
        }
        if (StringUtils.contains(name, ' ')) {
            name = name.replace(' ', '_');
        }

        return StringUtils.upperCase(name);
    }

}
