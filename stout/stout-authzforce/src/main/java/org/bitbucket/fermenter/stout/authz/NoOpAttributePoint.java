package org.bitbucket.fermenter.stout.authz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To bootstrap the attributes, we need a default attribute point.  There isn't a real case to use this outside of that.
 */
public class NoOpAttributePoint implements StoutAttributePoint {

    private static final Logger logger = LoggerFactory.getLogger(NoOpAttributePoint.class);

    @Override
    public AttributeValue<?> getValueForAttribute(String attributeId, String subject) {
        logger.error("The {} attribute provider should not be used - it is for bootstrap configuration purposes only!",
                NoOpAttributePoint.class);
        return new AttributeValue<String>(attributeId, "DO NOT USE THIS!");
    }

}
