package org.technologybrewery.fermenter.stout.authz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To bootstrap the attributes, we need a default attribute point.  There isn't a real case to use this outside of that.
 */
public class NoOpAttributePoint implements StoutAttributePoint {

    private static final Logger logger = LoggerFactory.getLogger(NoOpAttributePoint.class);

    @Override
    public Collection<AttributeValue<?>> getValueForAttribute(String attributeId, String subject) {
        logger.error("The {} attribute provider should not be used - it is for bootstrap configuration purposes only!",
                NoOpAttributePoint.class);
        List<AttributeValue<?>> attributes = new ArrayList<>();
        attributes.add(new AttributeValue<String>(attributeId, "DO NOT USE THIS!"));
        return attributes;
    }

}
