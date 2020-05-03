package com.zenvia.atm.exception;

public class CashMachineValidateException extends RuntimeException{

	private static final long serialVersionUID = 214170373099838391L;
	
	public CashMachineValidateException(String message) {
		super(message);
	}
	
	public CashMachineValidateException(String message, Throwable cause) {
		super(message, cause);
	}

}
