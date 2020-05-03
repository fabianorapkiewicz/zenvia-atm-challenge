package com.zenvia.atm.model.handler;

import com.zenvia.atm.exception.CashMachineException;
import com.zenvia.atm.model.CashAmount;

public abstract class GenericCashHandler implements CashHandler{

	private CashHandler nextHandler;
	
	public abstract Integer getBanknoteValue();

	public abstract boolean hasBanknotes();
	
	public abstract void decreaseAvalibleBanknotes();
	
	@Override
	public void dispense(CashAmount amount, CashCollector collector) throws CashMachineException {
		Integer diffAmountRequested = amount.getValue();
		
		while (hasBanknotes() && diffAmountRequested >= getBanknoteValue()) {
			decreaseAvalibleBanknotes();
			collector.addCash(CashAmount.from(getBanknoteValue()));
			diffAmountRequested = diffAmountRequested - getBanknoteValue(); 
		}
		
		if(diffAmountRequested > 0) 
			follow().dispense(CashAmount.from(diffAmountRequested), collector);
	}

	@Override
	public CashHandler appendHandler(CashHandler nextHandler) {
		if (this.nextHandler == null)
			this.nextHandler = nextHandler;
		else
			this.nextHandler.appendHandler(nextHandler);
		
		return this;
	}

	protected CashHandler follow() throws CashMachineException {
		if (nextHandler == null) {
			throw new CashMachineException("Não será possível entregar o valor desejado. "
					+ "Provavelmente não há cédulas o suficiente na máquina.");
		}
		
		return nextHandler;
	}

}
