package org.technologybrewery.fermenter.mda.generator;

import org.technologybrewery.fermenter.mda.notification.NotificationCollector;
import org.technologybrewery.fermenter.mda.notification.VelocityNotification;

import java.util.Set;

public class TestSpecificNotificationGenerator extends TestGenerator {

    private String key;
    private Set<String> items;
    private String programmaticValue;

    public TestSpecificNotificationGenerator(String key, Set<String> items, String programmaticValue) {
        super(1, false);

        this.key = key;
        this.items = items;
        this.programmaticValue = programmaticValue;

    }

    @Override
    public void generate(GenerationContext context) {
        VelocityNotification notification = new VelocityNotification(key, items, "templates/notifications/sample.notification.vm");
        notification.addToVelocityContext("programmaticValue", programmaticValue);

        NotificationCollector.addNotification("foo", notification);

    }

}
