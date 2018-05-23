package org.bitbucket.fermenter.stout.authz;

import static org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT;
import static org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory.XACML_3_0_ACTION;
import static org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory.XACML_3_0_RESOURCE;

import java.io.IOException;
import java.util.Optional;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.bitbucket.fermenter.stout.authz.config.AuthorizationConfig;
import org.bitbucket.fermenter.stout.exception.UnrecoverableException;
import org.ow2.authzforce.core.pdp.api.AttributeFqn;
import org.ow2.authzforce.core.pdp.api.AttributeFqns;
import org.ow2.authzforce.core.pdp.api.DecisionRequest;
import org.ow2.authzforce.core.pdp.api.DecisionRequestBuilder;
import org.ow2.authzforce.core.pdp.api.DecisionResult;
import org.ow2.authzforce.core.pdp.api.value.AttributeBag;
import org.ow2.authzforce.core.pdp.api.value.Bags;
import org.ow2.authzforce.core.pdp.api.value.StandardDatatypes;
import org.ow2.authzforce.core.pdp.api.value.StringValue;
import org.ow2.authzforce.core.pdp.impl.BasePdpEngine;
import org.ow2.authzforce.core.pdp.impl.PdpEngineConfiguration;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.DecisionType;

/**
 * Authorization hub inspired by the XACML Policy Decision Point (PDP) model.
 */
public class PolicyDecisionPoint {

    private static final Logger logger = LoggerFactory.getLogger(PolicyDecisionPoint.class);

    private BasePdpEngine pdpEngine;

    private static PolicyDecisionPoint instance = new PolicyDecisionPoint();

    private final AuthorizationConfig config = KrauseningConfigFactory.create(AuthorizationConfig.class);

    protected PolicyDecisionPoint() {
        try {
            String accessExternalSchemaType = config.getAccessExternalSchemaType();
            String pdpConfigurationLocation = config.getPdpConfigurationLocation();
            String pdpCatalogLocation = config.getPdpCatalogLocation();
            String pdpExtensionXsdLocation = config.getPdpExtensionXsdLocation();

            System.setProperty("javax.xml.accessExternalSchema", accessExternalSchemaType);
            PdpEngineConfiguration pdpEngineConfiguration = PdpEngineConfiguration.getInstance(pdpConfigurationLocation,
                    pdpCatalogLocation, pdpExtensionXsdLocation);
            pdpEngine = new BasePdpEngine(pdpEngineConfiguration);

        } catch (IllegalArgumentException | IOException e) {
            throw new UnrecoverableException("Problem creating the PDP!", e);

        }
    }

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static PolicyDecisionPoint getInstance() {
        return instance;
    }

    /**
     * Authorization request for a given subject, resource, and action.
     * 
     * @param subject
     *            the identity to authorize
     * @param resource
     *            the resource to authorize
     * @param action
     *            the action to authorize
     * @return authorization decision
     */
    public PolicyDecision isAuthorized(String subject, String resource, String action) {
        long start = System.currentTimeMillis();

        DecisionRequestBuilder<?> requestBuilder = pdpEngine.newRequestBuilder(-1, -1);

        addSubjectAttribute(subject, requestBuilder);
        addResourceAttribute(resource, requestBuilder);
        addActionAttribute(action, requestBuilder);

        DecisionRequest decisionRequest = requestBuilder.build(false);

        DecisionResult result = pdpEngine.evaluate(decisionRequest);
        DecisionType decision = result.getDecision();

        long stop = System.currentTimeMillis();
        String message = "Authorization decision {} for subject={}, resource={} and action={} in {}ms";
        if (!DecisionType.PERMIT.equals(decision)) {
            logger.warn(message, decision, subject, resource, action, stop - start);

        } else {
            logger.info(message, decision, subject, resource, action, stop - start);

        }

        return PolicyDecision.valueOf(decision.toString());

    }

    private void addActionAttribute(String action, DecisionRequestBuilder<?> requestBuilder) {
        AttributeFqn actionIdAttributeId = AttributeFqns.newInstance(XACML_3_0_ACTION.value(), Optional.empty(),
                XacmlAttributeId.XACML_1_0_ACTION_ID.value());
        AttributeBag<?> actionIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(action));
        requestBuilder.putNamedAttributeIfAbsent(actionIdAttributeId, actionIdAttributeValues);
    }

    private void addResourceAttribute(String resource, DecisionRequestBuilder<?> requestBuilder) {
        AttributeFqn resourceIdAttributeId = AttributeFqns.newInstance(XACML_3_0_RESOURCE.value(), Optional.empty(),
                XacmlAttributeId.XACML_1_0_RESOURCE_ID.value());
        AttributeBag<?> resourceIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(resource));
        requestBuilder.putNamedAttributeIfAbsent(resourceIdAttributeId, resourceIdAttributeValues);
    }

    private void addSubjectAttribute(String subject, DecisionRequestBuilder<?> requestBuilder) {
        AttributeFqn subjectIdAttributeId = AttributeFqns.newInstance(XACML_1_0_ACCESS_SUBJECT.value(),
                Optional.empty(), XacmlAttributeId.XACML_1_0_SUBJECT_ID.value());
        AttributeBag<?> subjectIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(subject));
        requestBuilder.putNamedAttributeIfAbsent(subjectIdAttributeId, subjectIdAttributeValues);
    }

}
