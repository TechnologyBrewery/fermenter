package org.bitbucket.fermenter.stout.mda;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metamodel.element.BaseEnumDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Enum;

/**
 * Decorates an enumeration constant (enum) for easier Java rendering.
 */
public class JavaEnum extends BaseEnumDecorator implements Enum, JavaNamedElement {

    private static final char SPACE = ' ';
    private static final char BACKSLASH = '\'';
    private static final char UNDERSCORE = '_';
    private static final char DASH = '-';
    private static final char RIGHT_PAREN = ')';
    private static final char LEFT_PAREN = '(';

    /**
     * {@inheritDoc}
     */
    public JavaEnum(Enum enumToDecorate) {
        super(enumToDecorate);
    }

    /**
     * Transforms the name into a Java enum-friendly, uppercased version.
     * @return Returns the uppercased name.
     */
    public String getUppercasedName() {
        String name = getName();

        if (StringUtils.contains(name, LEFT_PAREN)) {
            name = StringUtils.remove(name, LEFT_PAREN);
        }
        if (StringUtils.contains(name, RIGHT_PAREN)) {
            name = StringUtils.remove(name, RIGHT_PAREN);
        }
        if (StringUtils.contains(name, DASH)) {
            name = name.replace(DASH, UNDERSCORE);
        }
        if (StringUtils.contains(name, BACKSLASH)) {
            name = name.replace(BACKSLASH, UNDERSCORE);
        }
        if (StringUtils.contains(name, SPACE)) {
            name = name.replace(SPACE, UNDERSCORE);
        }

        return StringUtils.upperCase(name);
    }

}
