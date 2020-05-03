package com.zenvia.atm.model.handler;

import java.util.Optional;

public class FiftyNoteHandler extends GenericCashHandler {

	private final Integer banknoteValue = 50;
	private Integer banknotesCount;
	private boolean hasBanknotesLimit;
	
	public FiftyNoteHandler(Optional<Integer> numberOfNotes) {
		this.banknotesCount = numberOfNotes.orElse(Integer.MIN_VALUE);
		this.hasBanknotesLimit = numberOfNotes.isPresent();
	}

	@Override
	public Integer getBanknoteValue() {
		return banknoteValue;
	}

	@Override
	public boolean hasBanknotes() {
		return !hasBanknotesLimit || banknotesCount > 0; 
	}

	@Override
	public void decreaseAvalibleBanknotes() {
			banknotesCount--;
	}
}
