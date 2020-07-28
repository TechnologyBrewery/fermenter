package org.bitbucket.fermenter.stout.mda.generator.rule;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.rule.AbstractRuleGenerator;
import org.bitbucket.fermenter.mda.metamodel.element.Rule;
import org.bitbucket.fermenter.stout.mda.JavaRule;
import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;

/**
 * Iterates through rule instances, passing {@link JavaRule}s instance to the templates.
 *
 */
public class RuleGenerator extends AbstractRuleGenerator {

    private static final String BASE_PACKAGE = "basePackage";
    protected static final String RULE = "rule";

    @Override
    protected void populateVelocityContext(VelocityContext vc, Rule rule, GenerationContext generationContext) {
        JavaRule javaRule = new JavaRule(rule);
        vc.put(RULE, javaRule);
        vc.put(BASE_PACKAGE, generationContext.getBasePackage());
    }

    @Override
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_RESOURCES;
    }

}
