package org.technologybrewery.fermenter.stout.mda.generator.message;

import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.generator.message.AbstractMessageGroupGenerator;
import org.technologybrewery.fermenter.mda.metamodel.element.MessageGroup;
import org.technologybrewery.fermenter.stout.mda.JavaMessageGroup;
import org.technologybrewery.fermenter.stout.mda.java.JavaGeneratorUtil;

/**
 * Iterates through message group instances, passing {@link JavaMessageGroup}s instance to the templates.
 *
 */
public class MessageGroupJavaGenerator extends AbstractMessageGroupGenerator {

    private static final String BASE_PACKAGE = "basePackage";
    protected static final String MESSAGE_GROUP = "messageGroup";

    @Override
    protected void populateVelocityContext(VelocityContext vc, MessageGroup messageGroup, GenerationContext generationContext) {
        JavaMessageGroup javaMessageGroup = new JavaMessageGroup(messageGroup);
        vc.put(MESSAGE_GROUP, javaMessageGroup);
        vc.put(BASE_PACKAGE, generationContext.getBasePackage());
    }

    @Override
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

}
