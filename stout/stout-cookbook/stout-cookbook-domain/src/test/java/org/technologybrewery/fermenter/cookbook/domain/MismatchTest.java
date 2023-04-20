package org.technologybrewery.fermenter.cookbook.domain;

import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.TableNameMismatchBO;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.TableNameMismatchBaseBO;
import org.technologybrewery.fermenter.stout.exception.UnrecoverableException;
import org.junit.After;
import org.junit.Test;

public class MismatchTest {

	@After
	public void deleteAllMismatchTestExamples() {
		TableNameMismatchBO.deleteAllTableNameMismatch();
	}
	
    /**
     * Use reflection to set a table name mismatch, then restore the value after the test.
     * @throws Exception
     */
    @Test
    public void testMismatchingTableNameBetweenBoAndModel() throws Exception {
        Method method = Class.class.getDeclaredMethod("annotationData");
        method.setAccessible(true);
        Object annotationData = method.invoke(TableNameMismatchBO.class);
        Field annotations = annotationData.getClass().getDeclaredField("annotations");
        annotations.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Class<? extends Annotation>, Annotation> map = (Map<Class<? extends Annotation>, Annotation>) annotations
                .get(annotationData);
        Table originalTable = (Table) map.get(Table.class);
        map.put(Table.class, new OverrideTable());

        // May have already been loaded, short-circuiting the check, so make sure the short-circuit 
        // flag is appropriately set:
        Field shortCircuitFlagField = TableNameMismatchBaseBO.class.getDeclaredField("hasCheckedTableMismatch");
        shortCircuitFlagField.setAccessible(true);
        shortCircuitFlagField.set(null, false);

        try {
            new TableNameMismatchBO();
            fail("Should have thrown a table mismatch exception!");

        } catch (UnrecoverableException e) {
            // put things back how they were:
            map.put(Table.class, originalTable);
            new TableNameMismatchBO();
        }
    }

    /**
     * Local class used to override the table name annotation for testing purposes.
     */
    class OverrideTable implements Table {

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }

        @Override
        public String name() {
            return "OLD_TABLE_NAME";
        }

        @Override
        public String catalog() {
            return null;
        }

        @Override
        public String schema() {
            return null;
        }

        @Override
        public UniqueConstraint[] uniqueConstraints() {
            return null;
        }

        @Override
        public Index[] indexes() {
            return null;
        }

    }
}
