package org.bitbucket.fermenter.stout.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.persistence.Table;

import org.bitbucket.fermenter.stout.exception.FermenterException;
import org.bitbucket.fermenter.stout.exception.UnrecoverableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains methods that help validate potential drift between non-overwritable resources (e.g., src/main/**) and
 * shifting metamodel instances.
 *
 */
public final class MetamodelInstanceUtil {

    public static final Logger logger = LoggerFactory.getLogger(MetamodelInstanceUtil.class);

    private MetamodelInstanceUtil() {
        // prevent instantiation of all static class
    }

    /**
     * Validates if a table name on the {@link Table} annotation matches what is specified in the metamodel instance.
     * @param className Class to verify
     * @param expectedTableName metamodel instance value (typically maintained in generated code)
     */
    public static void checkTableNameForMismatch(String className, String expectedTableName) {
        boolean isMismatched = false;
        try {
            Class<?> boTarget = Class.forName(className);

            Method method = Class.class.getDeclaredMethod("annotationData");
            method.setAccessible(true);
            Object annotationData = method.invoke(boTarget);
            Field annotations = annotationData.getClass().getDeclaredField("annotations");
            annotations.setAccessible(true);
            
            @SuppressWarnings("unchecked")
            Map<Class<? extends Annotation>, Annotation> map = (Map<Class<? extends Annotation>, Annotation>) annotations
                    .get(annotationData);
            
            Table tableAnnotation = (Table) map.get(Table.class);

            String foundTableName = tableAnnotation.name();
            if (!expectedTableName.equalsIgnoreCase(foundTableName)) {
                isMismatched = true;
                logger.error("Mismatching table names encountered for {}!\n\t table name found: {},\n\t table name expected: {}",
                        className, foundTableName, expectedTableName);
            }

        } catch (Exception e) {
            throw new FermenterException(
                    "Exception encountered validating " + className + " against metamodel instances!", e);
        }
        
        if (isMismatched) {
            logger.error("To fix this, ensure {}'s @Table annotation matches your metamodel instance!", className);
            throw new UnrecoverableException("Mimatching table names for " + className + " and its entity file!");            
        }
    }

}
