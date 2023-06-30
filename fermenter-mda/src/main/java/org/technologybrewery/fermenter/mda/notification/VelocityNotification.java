package org.technologybrewery.fermenter.mda.notification;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.technologybrewery.fermenter.mda.generator.GenerationException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;
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

    protected Properties groupVelocityContextValues;

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
     * Allows values to be passed to the *final* group notification.  To keep this simple and easy, only string-based
     * values are being supported.  These will be serialized out between modules, then merged and pushed into the
     * group VelocityContext.  Any conflicts will result in last up wins.
     *
     * @param key   The property key
     * @param value The property value
     */
    public void addToExternalVelocityContextProperties(String key, String value) {
        if (groupVelocityContextValues == null) {
            if (StringUtils.isBlank(group)) {
                throw new GenerationException("External Velocity Context Properties can ONLY be used when group is set!");

            } else {
                groupVelocityContextValues = new Properties();
            }
        }

        groupVelocityContextValues.put(key, value);
    }

    private Properties getExternalVelocityContextProperties() {
        return groupVelocityContextValues;
    }

    void writeExternalVelocityContextProperties(File parentFile) {
        if (hasGroup()) {
            Properties externalVelocityContextValues = getExternalVelocityContextProperties();
            File propertiesFile = getGroupVelocityContenctPropertiesFile(parentFile);
            try {
                if (propertiesFile.exists()) {
                    NotificationUtils.mergeExistingAndNewProperties(propertiesFile, externalVelocityContextValues);
                } else {
                    FileUtils.forceMkdirParent(propertiesFile);
                }

                try (Writer writer = Files.newBufferedWriter(propertiesFile.toPath(), Charset.defaultCharset())) {
                    externalVelocityContextValues.store(writer, null);
                }
            } catch (IOException e) {
                throw new GenerationException("Error writing group velocity context properties!", e);
            }
        }
    }

    File getGroupVelocityContenctPropertiesFile(File parentFile) {
        String propertiesFileName = FilenameUtils.getName(String.format("group-%s.properties", group));
        File parentDirectory = new File(parentFile, NotificationService.NOTIFICATION_DIRECTORY_PATH + group);
        return new File(parentDirectory, propertiesFileName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNotificationAsString() {
        String notificationString;
        if (StringUtils.isBlank(velocityTemplate)) {
            throw new GenerationException("Template location MUST be provided!");
        }

        Template template = velocityEngine.getTemplate(velocityTemplate);
        if (template == null) {
            String error = String.format("No template found at %s!", velocityTemplate);
            logger.error(error);
            notificationString = error;

        } else {
            try (Writer writer = new StringWriter()) {
                template.merge(context, writer);

                notificationString = writer.toString();

            } catch (IOException e) {
                throw new GenerationException("Could not process notification of manual action!", e);
            }
        }

        return notificationString;
    }
}
