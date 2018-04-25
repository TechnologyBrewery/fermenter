package org.bitbucket.fermenter.mda.metadata.element;

import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

public class ParentMetadata extends MetadataElement implements Parent {

    private String type;
    private InheritanceStrategy inheritanceStrategy = InheritanceStrategy.MAPPED_SUPERCLASS;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public InheritanceStrategy getInheritanceStrategy() {
        return inheritanceStrategy;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInheritanceStrategy(String inheritanceStrategyAsStr) {
        this.inheritanceStrategy = InheritanceStrategy.fromString(inheritanceStrategyAsStr);
    }

    /**
     * Validates the parent entity definition by ensuring that the entity actually exists
     */
    @Override
    public void validate() {
        MetadataRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);
        Entity parentEntity = metadataRepository.getEntity(getType());
        if (parentEntity == null) {
            throw new IllegalArgumentException("Could not find parent entity of type: " + getType());
        }
    }

}
