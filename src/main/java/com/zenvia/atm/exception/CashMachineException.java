package com.zenvia.atm.exception;

public class CashMachineException extends Exception {

	private static final long serialVersionUID = -6233084629496503403L;

	public CashMachineException(String message) {
		super(message);
	}
	
	public CashMachineException(String message, Throwable cause) {
		super(message, cause);
	}
}
