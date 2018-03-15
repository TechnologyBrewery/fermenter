package org.bitbucket.fermenter.mda.metadata.element;

public interface Field {

    //TODO: refactor this out - should be dynamic from the types listing
	public static final String TYPE_STRING = "string";
	public static final String TYPE_FLOAT = "float";
	public static final String TYPE_DATE = "date";
	public static final String TYPE_TIMESTAMP = "timestamp";
	public static final String TYPE_LONG = "long";
	public static final String TYPE_DOUBLE = "double";
	public static final String TYPE_INTEGER = "integer";
	public static final String TYPE_SHORT = "short";
	public static final String TYPE_NUMERIC_BOOLEAN = "numeric_boolean";
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_BIG_DECIMAL = "big_decimal";
	public static final String TYPE_CHARACTER = "character";
	public static final String TYPE_BLOB = "blob";
	public static final String TYPE_GEOSPATIAL_POINT = "geospatial_point";
	public static final String TYPE_UUID = "uuid";
	
    public static final String[] SIMPLE_TYPES = new String[] { TYPE_STRING, TYPE_FLOAT, TYPE_DATE, TYPE_TIMESTAMP,
            TYPE_LONG, TYPE_DOUBLE, TYPE_INTEGER, TYPE_SHORT, TYPE_NUMERIC_BOOLEAN, TYPE_BOOLEAN, TYPE_BIG_DECIMAL,
            TYPE_CHARACTER, TYPE_BLOB, TYPE_GEOSPATIAL_POINT, TYPE_UUID };
	
	public static final String ID_GENERATION_ASSIGNED = "assigned";

	/**
	 * @return Returns the column.
	 */
	public String getColumn();

	/**
	 * @return Returns the generator.
	 */
	public String getGenerator();

	/**
	 * @return Returns the label.
	 */
	public String getLabel();

	/**
	 * @return Returns the name.
	 */
	public String getName();
	
	public String getDocumentation();

	/**
	 * @return Returns the type.
	 */
	public String getType();

	public Boolean isSimpleType();

	public Boolean isEnumerationType();

	public Enumeration getEnumeration();

	/**
	 * @return Returns the maxLength.
	 */
	public String getMaxLength();

	/**
	 * @return Returns whether or not a maxLength exists.
	 */
	public boolean hasMaxLength();

	/**
	 * @return Returns the minLength.
	 */
	public String getMinLength();

	/**
	 * @return Returns whether or not a minLength exists.
	 */
	public boolean hasMinLength();

	/**
	 * @return Returns the maxValue.
	 */
	public String getMaxValue();

	/**
	 * @return Returns whether or not a maxValue exists.
	 */
	public boolean hasMaxValue();

	/**
	 * @return Returns the minValue.
	 */
	public String getMinValue();

	/**
	 * @return Returns whether or not a minValue exists.
	 */
	public boolean hasMinValue();

	/**
	 * @return Returns the required.
	 */
	public String getRequired();

	public boolean isRequired();

	/**
	 * Used when overriding referenced entity field names
	 * 
	 * @return Returns the overriddenName.
	 */
	public String getSourceName();
	
	public String getScale();
	
	public boolean hasScale();
	
	public String getProject();
	
	public boolean isExternal();
	
	String getFormat();
	
	boolean hasFormat();

}