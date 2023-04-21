package org.technologybrewery.fermenter.stout.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

/**
 * Deserializer that trims String values.
 */
public final class TrimmingStringDeserializer extends StdScalarDeserializer<String> {

    private static final long serialVersionUID = 8721520299501142938L;

    /**
     * {@inheritDoc}
     */
    public TrimmingStringDeserializer(Class<?> vc) {
        super(vc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return StringUtils.trim(p.getValueAsString());
    }
}
