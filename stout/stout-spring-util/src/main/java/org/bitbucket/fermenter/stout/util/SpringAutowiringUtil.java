package org.bitbucket.fermenter.stout.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * Facilitates the auto-wiring of objects that are *not* created by Spring with dependencies that are managed by Spring.
 * Specifically, this class enables developers to easily create business objects via normal object constructors and have
 * them injected with the Spring-managed components needed to perform entity persistence, validation, and more.
 */
public final class SpringAutowiringUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringAutowiringUtil.class);

    private SpringAutowiringUtil() {
        // prevent instantiation of all static class
    }

    public static <BO> BO autowireBizObj(BO bizObj) {
        ApplicationContext context = SpringApplicationContextHolder.getContext();
        if (context != null) {
            context.getAutowireCapableBeanFactory().autowireBeanProperties(bizObj,
                    AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
        } else {
            LOGGER.debug("Business object [{}] created without an available Spring application context",
                    bizObj.getClass().getName());
        }
        return bizObj;
    }
}
