package org.bitbucket.fermenter.mda.metadata.element;

import org.apache.commons.collections4.CollectionUtils;
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
     * Validates the parent entity definition by: <br>
     * 1. Ensuring that the entity actually exists <br>
     * 2. If the inheritance strategy is mapped superclass, that the parent entity has no references nor relations
     */
    @Override
    public void validate() {
        MetadataRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);
        Entity parentEntity = metadataRepository.getEntity(getType());
        if (parentEntity == null) {
            throw new IllegalArgumentException("Could not find parent entity of type: " + getType());
        }
        if (InheritanceStrategy.MAPPED_SUPERCLASS.equals(getInheritanceStrategy())
                && (CollectionUtils.isNotEmpty(parentEntity.getReferences().values())
                        || CollectionUtils.isNotEmpty(parentEntity.getRelations().values()))) {
            throw new IllegalArgumentException("Parent entity " + getType()
                    + " cannot have any references nor relations and use a mapped superclass inheritance strategy");
        }
    }

}
