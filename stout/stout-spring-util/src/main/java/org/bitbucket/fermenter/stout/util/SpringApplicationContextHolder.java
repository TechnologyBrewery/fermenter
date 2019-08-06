package org.bitbucket.fermenter.stout.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Maintains a copy of the encapsulating container's Spring {@link ApplicationContext} to facilitate programmatic access
 * to {@link ApplicationContext} functionality, such as manually auto-wiring business objects not managed by Spring with
 * Spring-managed components.
 */
public class SpringApplicationContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringApplicationContextHolder.applicationContext = applicationContext;
	}

	public static ApplicationContext getContext() {
		return SpringApplicationContextHolder.applicationContext;
	}
}
