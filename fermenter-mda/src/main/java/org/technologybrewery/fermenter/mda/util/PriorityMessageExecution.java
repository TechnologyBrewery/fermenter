package org.technologybrewery.fermenter.mda.util;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Class that extends {@link AbstractMavenLifecycleParticipant} and overrides the afterSessionEnd function. This allows
 * for {@link PriorityMessage}'s to be replayed at the end of the maven build output.
 */
@Named
@Singleton
public class PriorityMessageExecution extends AbstractMavenLifecycleParticipant {
    private static final Logger logger = LoggerFactory.getLogger(PriorityMessageExecution.class);
    private static final String PRIORITYMESSAGES = "PriorityMessages";
    private static final String DIVIDER = "***********************************************************************\n";
    private static final String HEADER = "*** Fermenter High Priority Messages                                ***\n";
    private static final String EMPTY_LINE = "\n";

    /**
     * Override of the afterSessionEnd class from {@link AbstractMavenLifecycleParticipant}. This method is
     * called after the maven build completes. This implementation loads all the {@link PriorityMessage}'s
     * stored in the {@link MavenSession} and replays them to the user.
     * @param session
     *          {@link MavenSession} used for retrieving the {@link PriorityMessage}'s from the user properties.
     */
    @Override
    public void afterSessionEnd(final MavenSession session) {
        List<PriorityMessage> messages = PriorityMessageService.getPriorityMessages(session);

        if (messages.size() > 0) {
            final StringBuilder logMessage = new StringBuilder();
            logMessage.append(EMPTY_LINE);
            logMessage.append(DIVIDER);
            logMessage.append(HEADER);
            logMessage.append(DIVIDER);

            messages.stream()
                .map(Object::toString)
                .forEach( message -> {
                    logMessage.append(message);
                    logMessage.append(EMPTY_LINE);
                });

            logMessage.append(DIVIDER);
            logMessage.append(DIVIDER);

            logger.warn(logMessage.toString());
            session.getUserProperties().put(PRIORITYMESSAGES, "");
        }
    }
}
