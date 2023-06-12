package org.technologybrewery.fermenter.mda.generator;

import org.technologybrewery.fermenter.mda.notification.Notification;
import org.technologybrewery.fermenter.mda.notification.NotificationCollector;
import org.technologybrewery.fermenter.mda.notification.VelocityNotification;

import java.util.HashSet;

public class TestMultipleNotificationGenerator extends TestGenerator {

    private int notificationsPerFile;

    public TestMultipleNotificationGenerator(int numberOfFiles, int notificationsPerFile) {
        super(numberOfFiles, false);

        this.notificationsPerFile = notificationsPerFile;
    }

    @Override
    public void generate(GenerationContext context) {
        for (int i = 0; i < notificationsPerFile; i++) {
            Notification notification = new VelocityNotification("foo-" + i, new HashSet<>(),
                "templates/notifications/sample.notification.vm");
            NotificationCollector.addNotification(getOutputPath().toString(), notification);
        }

        super.generate(context);
    }

}
