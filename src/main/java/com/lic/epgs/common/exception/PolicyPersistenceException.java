package com.lic.epgs.common.exception;

/**
 * @author DhatchanaMoorthy M
 */
@SuppressWarnings("serial")
public class PolicyPersistenceException extends Exception {
	public PolicyPersistenceException() {

	}

	public PolicyPersistenceException(String message) {
		super(message);

	}

	public PolicyPersistenceException(Throwable cause) {
		super(cause);

	}

	public PolicyPersistenceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PolicyPersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

}
