package org.bitbucket.fermenter.mda.element;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds type abstraction information to support MDA execution. A type represents the underlying implementation that
 * will be used for a higher-level metamodel type. For instance, a metamodel 'decimal' type could be a java.lang.Float,
 * java.lang.BigDecimal, etc.
 */
public class Type implements ValidatedElement {

    @JsonProperty(required = true)
    private String name;

    @JsonProperty
    private String fullyQualifiedImplementation;

    @JsonProperty
    private String shortImplementation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullyQualifiedImplementation() {
        return fullyQualifiedImplementation;
    }

    public void setFullyQualifiedImplementation(String fullyQualifiedImplementation) {
        this.fullyQualifiedImplementation = fullyQualifiedImplementation;
    }

    public String getShortImplementation() {
        return shortImplementation;
    }

    public void setShortImplementation(String shortImplementation) {
        this.shortImplementation = shortImplementation;
    }

    /**
     * {@inheritDoc}
     */
    public String getSchemaFileName() {
        return "fermenter-2-type-schema.json";
    }
}
