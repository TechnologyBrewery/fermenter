package org.technologybrewery.fermenter.mda.metamodel;

import org.technologybrewery.fermenter.mda.metamodel.element.Rule;
import org.technologybrewery.fermenter.mda.metamodel.element.RuleElement;

/**
 * Responsible for maintaining the list of rule model instances elements in the system.
 */
class RuleModelInstanceManager extends AbstractMetamodelManager<Rule> {

    private static ThreadLocal<RuleModelInstanceManager> threadBoundInstance = ThreadLocal
            .withInitial(RuleModelInstanceManager::new);

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static RuleModelInstanceManager getInstance() {
        return threadBoundInstance.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        super.reset();
        threadBoundInstance.remove();
    }

    /**
     * Prevent instantiation of this singleton from outside this class.
     */
    private RuleModelInstanceManager() {
        super();
    }

    @Override
    protected String getMetadataLocation() {
        return config.getRulesRelativePath();
    }

    @Override
    protected Class<RuleElement> getMetamodelClass() {
        return RuleElement.class;
    }

    @Override
    protected String getMetamodelDescription() {
        return Rule.class.getSimpleName();
    }

}
