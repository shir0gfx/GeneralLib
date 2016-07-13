package org.generallib.serializetools.exceptions;

public class FileSerializeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1908381519846364692L;

	private String message;
	
	public FileSerializeException(String message) {
		this.message = message;
	}

	@Override
	public void printStackTrace() {
		System.out.println(message);
		super.printStackTrace();
	}

	
}
