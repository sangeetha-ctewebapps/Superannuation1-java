package com.lic.epgs.common.exception;

/**
 * @author Dhatchanamoorthy M
 * 
 */
@SuppressWarnings("serial")
public class QuotePersistenceException extends Exception {
	public QuotePersistenceException() {

	}

	public QuotePersistenceException(String message) {
		super(message);

	}

	public QuotePersistenceException(Throwable cause) {
		super(cause);

	}

	public QuotePersistenceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public QuotePersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

}
