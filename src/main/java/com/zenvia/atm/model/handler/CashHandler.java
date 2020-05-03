package com.zenvia.atm.model.handler;

import com.zenvia.atm.exception.CashMachineException;
import com.zenvia.atm.model.CashAmount;

public interface CashHandler {
	
	public void dispense(CashAmount amount, CashCollector collector) 
			throws CashMachineException;
	
	public CashHandler appendHandler(CashHandler nextHandler);

}
