package org.bitbucket.fermenter.stout.mda.generator.message;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.message.AbstractMessageGroupGenerator;
import org.bitbucket.fermenter.mda.metamodel.element.MessageGroup;
import org.bitbucket.fermenter.stout.mda.JavaMessageGroup;
import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;

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
