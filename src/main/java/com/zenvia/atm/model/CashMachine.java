package com.zenvia.atm.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zenvia.atm.exception.CashMachineException;
import com.zenvia.atm.model.handler.CashHandler;
import com.zenvia.atm.validation.CashMachineValidator;

public class CashMachine {

	private CashHandler cashHandler;
	private CashMachineValidator validator;

	@Autowired
	public CashMachine(CashHandler cashHandler, CashMachineValidator validator) {		
		this.cashHandler = cashHandler;
		this.validator = validator;
		
	}
	
	public List<Cash> dispense(CashAmount amount) throws CashMachineException {
		validator.validate(amount);
		
		WadCash cashCollector = new WadCash();
		
		cashHandler.dispense(amount, cashCollector);
		
		return cashCollector.getCash();
	}
}
