package org.generallib.database;

public interface Observable {
	/**
	 * 
	 * @return true if this object is modified; false if not
	 */
	public boolean isChanged();
	/**
	 * clear any observable state from this object
	 */
	public void clearChanged();
	/**
	 * set this object as modified.
	 */
	public void setChanged();
}
