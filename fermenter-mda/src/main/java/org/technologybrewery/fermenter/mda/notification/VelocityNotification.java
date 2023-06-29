package org.technologybrewery.fermenter.mda.notification;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.technologybrewery.fermenter.mda.generator.GenerationException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Set;

/**
 * A notification that can be passed a velocity template to help make notification output easier to read and maintain.
 * <p>
 * Template name is read off the classpath and should be referred relative to the classpath root path.
 */
public class VelocityNotification extends AbstractNotification {

    private static final Logger logger = LoggerFactory.getLogger(VelocityNotification.class);

    protected String velocityTemplate;

    protected VelocityContext context;

    protected static VelocityEngine velocityEngine;

    static {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        velocityEngine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
    }

    /**
     * New instance.
     *
     * @param key              notification key
     * @param items            set of strings that can be passed into the template
     * @param velocityTemplate the tempalte name (e.g., "templates/notifications/foo.vm")
     */
    public VelocityNotification(String key, Set<String> items, String velocityTemplate) {
        super(key, items);
        init(key, items, velocityTemplate);
    }

    public VelocityNotification(String key, String group, Set<String> items, String velocityTemplate) {
        super(key, group, items);
        init(key, items, velocityTemplate);

        this.context.put("group", group);
    }

    private void init(String key, Set<String> items, String velocityTemplate) {
        this.velocityTemplate = velocityTemplate;
        this.context = new VelocityContext();

        this.context.put("key", key);
        this.context.put("items", items);
    }

    /**
     * Allows any object to be added into the {@link VelocityContext} for use via the template.  This is basically a
     * HashMap of objects that can be pulled from the template with ${key} syntax.  See Velocity documentation for more
     * information.
     *
     * @param velocityContextKey key of the object in the map
     * @param value              value related to the key
     */
    public void addToVelocityContext(String velocityContextKey, Object value) {
        this.context.put(velocityContextKey, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNotificationAsString() {
        if (StringUtils.isBlank(velocityTemplate)) {
            throw new GenerationException("Template location MUST be provided!");
        }

        Template template = velocityEngine.getTemplate(velocityTemplate);
        if (template == null) {
            logger.error("No template found at {}!", velocityTemplate);
        }

        try (Writer writer = new StringWriter()) {
            template.merge(context, writer);

            return writer.toString();

        } catch (IOException e) {
            throw new GenerationException("Could not process notification of manual action!", e);
        }
    }
}
