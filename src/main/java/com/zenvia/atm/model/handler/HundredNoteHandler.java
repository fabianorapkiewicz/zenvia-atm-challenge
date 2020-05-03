package com.zenvia.atm.model.handler;

import java.util.Optional;

public class HundredNoteHandler extends GenericCashHandler {

	private final Integer banknoteValue = 100;
	private Integer banknotesCount;
	private boolean hasBanknotesLimit;
	
	public HundredNoteHandler(Optional<Integer> numberOfNotes) {
		this.banknotesCount = numberOfNotes.orElse(0);
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
