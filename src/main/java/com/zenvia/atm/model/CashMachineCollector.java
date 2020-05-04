package com.zenvia.atm.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zenvia.atm.model.handler.CashCollector;

public class CashMachineCollector implements CashCollector{
	private Map<Integer, Integer> cash;

	public CashMachineCollector() {
		cash = new HashMap<Integer, Integer>();
	}
	
	public List<WadOfCash> getWadOfCash() {
		return cash.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				.map( item -> new WadOfCash(item.getKey(), item.getValue()))
				.collect(Collectors.toList());
		}


	@Override
	public void addCash(CashAmount amount) {
		cash.compute(amount.getValue(), (k,v) -> v == null? 1 : v+1);		
	}
		
}
