package com.zenvia.atm.exception;

public class CashAmountInvalidException extends RuntimeException{

	private static final long serialVersionUID = 214170373099838391L;
	
	public CashAmountInvalidException(String message) {
		super(message);
	}
	
	public CashAmountInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

}
