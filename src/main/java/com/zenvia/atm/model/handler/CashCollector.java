package com.zenvia.atm.model.handler;

import com.zenvia.atm.model.CashAmount;

public interface CashCollector {
	
	public void addCash(CashAmount cash);
}
