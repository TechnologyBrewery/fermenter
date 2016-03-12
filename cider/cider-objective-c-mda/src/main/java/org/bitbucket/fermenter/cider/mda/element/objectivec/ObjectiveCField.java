package org.bitbucket.fermenter.cider.mda.element.objectivec;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.cider.mda.objectivec.ObjectiveCStringEscapeUtils;
import org.bitbucket.fermenter.mda.metadata.FormatMetadataManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.element.Enumeration;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Format;
import org.bitbucket.fermenter.mda.metadata.element.Pattern;

public class ObjectiveCField implements Field {

	private Field field;
	private String importName;

	/**
	 * Create a new instance of {@link Field} with the correct functionality set
	 * to generate Objective-C code.
	 * @param fieldToDecorate The {@link Field} to decorate
	 */
	public ObjectiveCField(Field fieldToDecorate) {
		if (fieldToDecorate == null) {
			throw new IllegalArgumentException("ObjectiveCFields must be instantiated with a non-null field!");
		}
		field = fieldToDecorate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumn() {
		return field.getColumn();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGenerator() {
		return field.getGenerator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return field.getLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return field.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDocumentation() {
		return field.getDocumentation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getType() {
		MetadataRepository repo = MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		return ObjectiveCElementUtils.getObjectiveCType(repo.getApplicationName(), field.getType());
	}

	public Boolean isEnumeration() {
		MetadataRepository repo = MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		Enumeration enumeration = repo.getEnumeration(field.getType());
		return (enumeration != null);
	}

	public String getEnumerationType() {
		MetadataRepository repo = MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		ObjectiveCEnumeration enumeration = new ObjectiveCEnumeration(repo.getEnumeration(field.getType()));
		return enumeration.getName();
	}

	public String getTypeReferenceAttribute() {
		return ObjectiveCElementUtils.getObjectiveCTypeReferenceAttribute(getType());
	}

	public String getTypeAttributes() {
		return ObjectiveCElementUtils.getObjectiveCTypeAttribute(getType());
	}

	public String getCapitalizedName() {
		return StringUtils.capitalize(getName());
	}

	public String getUppercasedName() {
		return field.getName().toUpperCase();
	}

	public String getUppercasedType() {
		return field.getType().toUpperCase();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isSimpleType() {
		return field.isSimpleType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isEnumerationType() {
		return field.isEnumerationType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration getEnumeration() {
		return field.getEnumeration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMaxLength() {
		return field.getMaxLength();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasMaxLength() {
		return field.hasMaxLength();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMinLength() {
		return field.getMinLength();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasMinLength() {
		return field.hasMinLength();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMaxValue() {
		return field.getMaxValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasMaxValue() {
		return field.hasMaxValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMinValue() {
		return field.getMinValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasMinValue() {
		return field.hasMinValue();
	}

	@Override
	public String getRequired() {
		return field.getRequired();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRequired() {
		return field.isRequired();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSourceName() {
		return field.getSourceName();
	}

	public boolean isEntity() {
		MetadataRepository repo = MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		return repo.getEntity(getProject(), getType() ) != null;
	}

	public String getImport() {
		if (importName == null) {
			MetadataRepository repo = MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
			String appName = isExternal() ? getProject() : repo.getApplicationName();
			importName = ObjectiveCElementUtils.getObjectiveCImport(appName, field.getType());
		}

		return importName;
	}

	Field getFieldObject() {
		return field;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScale() {
		return field.getScale();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasScale() {
		return field.hasScale();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProject() {
		return field.getProject();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isExternal() {
		return field.isExternal();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFormat() {
		return field.getFormat();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasFormat() {
		return field.hasFormat();
	}

	public String getPatterns() {
		Format format = FormatMetadataManager.getInstance().getFormat(getFormat());

		StringBuilder sb = new StringBuilder(100);
		for (Iterator<Pattern> i = format.getPatterns().iterator(); i.hasNext();) {
			Pattern pattern = i.next();

			sb.append("\"");
			sb.append(ObjectiveCStringEscapeUtils.escapeObjectiveC(pattern.getText()));
			sb.append("\"");

			if (i.hasNext()) {
				sb.append(", ");
			}
		}

		return sb.toString();
	}
}
