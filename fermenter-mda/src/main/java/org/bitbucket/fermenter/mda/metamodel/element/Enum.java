package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Contract for a constant defined within an {@link Enumeration}.
 */
public interface Enum {

    /**
     * The name of this enumeration.
     * 
     * @return Returns the name.
     */
    public String getName();

    /**
     * The value of this enumeration (optional). For example, for a MonthOfYear enumeration, you might have a enum
     * January with a value of 1. If you want a multi-valued enumeration, please use a Reference instead.
     * 
     * @return Value of the enumeration
     */
    public Integer getValue();

}