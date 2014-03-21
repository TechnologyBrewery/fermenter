package org.tigris.atlas.transfer;


/**
 * Base class for all parent transfer objects.
 */
public abstract class BaseTO<T> extends AbstractTransferComponent implements TransferObject {

	private static final long serialVersionUID = -7655573120930710176L;

	/**
	 * Default constructor.
	 */
	public BaseTO() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		try {
			return getKey().equals( ((TransferObject)o).getKey() );
		} catch (ClassCastException cce) {
			//TODO: log exception
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return getKey().hashCode();
	}
    
}
