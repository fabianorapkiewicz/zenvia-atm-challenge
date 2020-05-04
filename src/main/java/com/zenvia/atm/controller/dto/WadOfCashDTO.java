package com.zenvia.atm.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zenvia.atm.model.WadOfCash;

public class WadOfCashDTO {
	
	private List<CashDTO> banknotes;

	public WadOfCashDTO() {
		this.banknotes = new ArrayList<CashDTO>();
	}
	
	public WadOfCashDTO(List<WadOfCash> wadOfCash) {
		this.banknotes = wadOfCash.stream()
				.map( cash ->  new CashDTO(cash.getValue(), cash.getAmount()))
				.collect(Collectors.toList());
	}

	public List<CashDTO> getBanknotes() {
		return banknotes;
	}

	public void setBanknotes(List<CashDTO> cash) {
		this.banknotes = cash;
	}
	
	
}
