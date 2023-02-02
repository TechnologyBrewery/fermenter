package org.bitbucket.fermenter.mda.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.internal.DefaultLegacySupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to aid in adding and retrieving {@link PriorityMessage}'s to and from the {@link MavenSession}.
 */
public class PriorityMessageService {
    private static final Logger logger = LoggerFactory.getLogger(PriorityMessageService.class);
    private static final String PRIORITYMESSAGES = "PriorityMessages";
    private MavenSession session;

    /**
     * Constructor that automatically sets the {@link MavenSession} to the current one.
     */
    public PriorityMessageService() {
        this.session = new DefaultLegacySupport().getSession();
    }

    /**
     * Constructor used for testing to allow a different {@link MavenSession}.
     * @param session
     *          {@link MavenSession} used for testing.
     */
    public PriorityMessageService(MavenSession session) {
        this.session = session;
    }

    /**
     * Method for adding a {@link PriorityMessage} to the associated {@link MavenSession}.
     * @param priorityMessage
     *          {@link PriorityMessage} for storing the message to be replayed and its associated metadata.
     */
    public void addPriorityMessage(final PriorityMessage priorityMessage) {
        try {
            final List<PriorityMessage> messages = getPriorityMessages(this.session);
            messages.add(priorityMessage);

            final ObjectMapper mapper = new ObjectMapper();
            final String json = mapper.writeValueAsString(messages);
            this.session.getUserProperties().put(PRIORITYMESSAGES, json);

        } catch (JsonProcessingException e) {
            logger.error("Unable to set priority message", e);
        }
    }

    /**
     * Method for retrieving the current {@link PriorityMessage}'s from the {@link MavenSession}
     * @param session
     *          {@link MavenSession} used for retrieving the {@link PriorityMessage}'s.
     * @return
     *          {@link List} containing all the {@link PriorityMessage}'s found in the provided {@link MavenSession}.
     */
    public static List<PriorityMessage> getPriorityMessages(final MavenSession session) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String json = (String) session.getUserProperties().get(PRIORITYMESSAGES);
            if (!StringUtils.isEmpty(json)) {
                return mapper.readValue(json, new TypeReference<List<PriorityMessage>>() {});
            }
        } catch (JsonProcessingException e) {
            logger.error("Unable to get priority messages", e);
        }
        return new ArrayList<PriorityMessage>();
    }
}
