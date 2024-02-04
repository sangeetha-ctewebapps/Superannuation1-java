package com.lic.epgs.common.exception;

/**
 * @author Dhatchanamoorthy M
 */
@SuppressWarnings("serial")
public class ResourceNotFoundException extends Exception {
	public ResourceNotFoundException() {

	}

	public ResourceNotFoundException(String message) {
		super(message);

	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);

	}

	public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
