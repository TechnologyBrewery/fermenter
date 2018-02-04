package org.bitbucket.fermenter.mda.element;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.bitbucket.fermenter.mda.generator.GenerationException;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Defines the contract and common functionality classes that represent json and can be validated by a json schema.
 */
public abstract class ValidatedElement {

    /**
     * Returns a URL to the schema that can be used to validate json files for the implementing sub-class.
     * 
     * @return URL to a json schema
     */
    @JsonIgnore
    public URL getJsonSchemaUrl() {
        Enumeration<URL> jsonSchema;
        String schemaFileName = getSchemaFileName();
        try {
            jsonSchema = Target.class.getClassLoader().getResources(schemaFileName);

        } catch (IOException e) {
            throw new GenerationException("Could not find Fermenter json schema for '" + schemaFileName + "'!", e);
        }

        return jsonSchema.nextElement();

    }

    /**
     * The name of the schema file used to validate a sub-class (e.g., foo-schema.json).
     * 
     * @return schema name
     */
    @JsonIgnore
    public abstract String getSchemaFileName();

}
