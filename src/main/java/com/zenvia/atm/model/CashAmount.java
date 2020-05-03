package com.zenvia.atm.model;

import java.util.regex.Pattern;

import com.zenvia.atm.exception.CashAmountInvalidException;

public class CashAmount {

	private Integer amountValue;
	
	private CashAmount(Integer amountValue) {
		this.amountValue = amountValue;
	}
	
	public Integer getValue() {
		return amountValue;
	}
	
	public static CashAmount from(String amount) {
		if( amount == null || amount.isBlank())
			throw new CashAmountInvalidException("Um objeto do tipo CashAmount não "
					+ "pode ser criado a partir de um valor nulo ou vazio");
		
		Pattern pattern = Pattern.compile("^[1-9]\\d*$");
		Boolean isNumberGreaterThenZero = pattern.matcher(amount).matches();
		
		if(isNumberGreaterThenZero)
			return new CashAmount(Integer.valueOf(amount));
		else {
			throw new CashAmountInvalidException("Um objeto do tipo CashAmount "
					+ "só pode ser criado a partir de uma representação númerica positiva");
		}
	}
	
	public static CashAmount from(Integer amount) {
		if(amount > 0)
			return new CashAmount(amount);
		else {
			throw new CashAmountInvalidException("Um objeto do tipo CashAmount "
					+ "não pode ser criado a partir de um valor menor que zero");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amountValue == null) ? 0 : amountValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CashAmount other = (CashAmount) obj;
		if (amountValue == null) {
			if (other.amountValue != null)
				return false;
		} else if (!amountValue.equals(other.amountValue))
			return false;
		return true;
	}
	
}
