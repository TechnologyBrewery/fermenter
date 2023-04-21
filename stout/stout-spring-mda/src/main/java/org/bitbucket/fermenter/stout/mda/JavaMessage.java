package org.technologybrewery.fermenter.stout.mda;

import org.apache.commons.lang3.StringUtils;
import org.technologybrewery.fermenter.mda.metamodel.element.BaseMessageDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.Message;

/**
 * Decorates an message constant for easier Java rendering.
 */
public class JavaMessage extends BaseMessageDecorator implements Message, JavaNamedElement {

    private static final char SPACE = ' ';    
    private static final char UNDERSCORE = '_';
    private static final char DASH = '-';
    private static final char DOT = '.';

    /**
     * {@inheritDoc}
     */
    public JavaMessage(Message messageToDecorate) {
        super(messageToDecorate);
    }

    /**
     * Transforms the name into a Java enum-friendly, uppercased version.
     * 
     * @return Returns the uppercased name.
     */
    public String getUppercasedName() {
        String name = getName();

        if (StringUtils.contains(name, DASH)) {
            name = name.replace(DASH, UNDERSCORE);
        }
        if (StringUtils.contains(name, SPACE)) {
            name = name.replace(SPACE, UNDERSCORE);
        }
        if (StringUtils.contains(name, DOT)) {
            name = name.replace(DOT, UNDERSCORE);
        }

        return StringUtils.upperCase(name);
    }

}
