package org.technologybrewery.fermenter.mda.metamodel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.technologybrewery.fermenter.mda.metamodel.element.Entity;
import org.technologybrewery.fermenter.mda.metamodel.element.Reference;
import org.technologybrewery.fermenter.mda.metamodel.element.Relation;

/**
 * Comparator that orders entities based on their dependencies. Referenced objects will be first, leaf nodes will be
 * last. Ties are sorted alphabetically.
 */
public class EntityComparator implements Comparator<Entity> {

    private Map<String, List<String>> referencedObjects;

    /**
     * New instance that takes an inverted list of objects and *which* entities reference them.
     * 
     * @param referencedObjects
     *            inverted reference list
     */
    public EntityComparator(Map<String, List<String>> referencedObjects) {
        this.referencedObjects = referencedObjects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(Entity o1, Entity o2) {
        int compareValue = 0;

        if (o1 != o2) {
            if (referencedObjects.containsKey(o1.getName()) && !referencedObjects.containsKey(o2.getName())) {
                // if o1 has references to it, but o2 does not, then o1 is less than:
                compareValue = -1;

            } else if (referencedObjects.containsKey(o2.getName()) && !referencedObjects.containsKey(o1.getName())) {
                // if o2 has references to it, but o1 does not, then o1 is less than:
                compareValue = 1;

            } else {
                // otherwise, do a deeper comparison:
                compareValue = compareAgainstReference(o1, o2, compareValue);

                compareValue = compareAgainstRelations(o1, o2, compareValue);

                compareValue = orderAlphabetically(o1, o2, compareValue);

            }

        }

        return compareValue;
    }

    protected int orderAlphabetically(Entity o1, Entity o2, int compareValue) {
        if (compareValue == 0) {
            compareValue = o1.getName().compareTo(o2.getName());
        }
        return compareValue;
    }

    protected int compareAgainstRelations(Entity o1, Entity o2, int compareValue) {
        compareValue = compareRelationsAssumingO1IsParent(o1, o2, compareValue);
        compareValue = compareRelationsAssumingO1IsChild(o1, o2, compareValue);

        return compareValue;
    }

    protected int compareRelationsAssumingO1IsChild(Entity o1, Entity o2, int compareValue) {
        if (compareValue == 0) {
            List<Relation> o2Relations = o2.getRelations();
            if (CollectionUtils.isNotEmpty(o2Relations)) {
                for (Relation relation : o2Relations) {
                    if (relation.getPackage().equals(o1.getPackage()) && relation.getType().equals(o1.getName())) {
                        compareValue = 1;
                        break;
                    }
                }
            }
        }
        return compareValue;
    }

    protected int compareRelationsAssumingO1IsParent(Entity o1, Entity o2, int compareValue) {
        List<Relation> o1Relations = o1.getRelations();
        if (CollectionUtils.isNotEmpty(o1Relations)) {
            for (Relation relation : o1Relations) {
                if (relation.getPackage().equals(o2.getPackage()) && relation.getType().equals(o2.getName())) {
                    compareValue = -1;
                    break;
                }
            }
        }
        return compareValue;
    }

    protected int compareAgainstReference(Entity o1, Entity o2, int compareValue) {
        List<String> referencesToEntity = referencedObjects.getOrDefault(o1.getName(), Collections.emptyList());
        if (referencesToEntity.contains(o2.getName())) {
            compareValue = -1;

        } else {
            List<Reference> o1References = o1.getReferences();
            if (CollectionUtils.isNotEmpty(o1References)) {
                for (Reference reference : o1References) {
                    if (reference.getPackage().equals(o2.getPackage()) && reference.getType().equals(o2.getName())) {
                        compareValue = 1;
                        break;

                    }
                }
            }

        }

        return compareValue;
    }

}
