package com.zenvia.atm.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zenvia.atm.model.Cash;

public class WadCashDTO {
	
	private List<BanknoteDTO> banknotes;

	public WadCashDTO() {
		this.banknotes = new ArrayList<BanknoteDTO>();
	}
	
	public WadCashDTO(List<Cash> cash) {
		this.banknotes = cash.stream()
				.map( c ->  new BanknoteDTO(c.getValue(), c.getAmount()))
				.collect(Collectors.toList());
	}

	public List<BanknoteDTO> getBanknotes() {
		return banknotes;
	}

	public void setBanknotes(List<BanknoteDTO> cash) {
		this.banknotes = cash;
	}
	
	
}
