package com.zenvia.atm.validation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.zenvia.atm.exception.CashMachineValidateException;
import com.zenvia.atm.model.CashAmount;

class CashMachineValidatorTest {

	private CashMachineValidator validator;
	
	@BeforeEach
	public void setUp() {
		this.validator = new CashMachineValidator();
	}
	
	@AfterEach
	public void tearDown() {
		this.validator = null;
	}
	
	@Test
	void whenCashAmountIsNullThenThrowException() {
		Assertions.assertThrows(CashMachineValidateException.class, 
				() -> validator.validate(null));
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 1, 7, 18, 38, 123})
	void whenCashAmountIsIntegerNotMultipleByTenThenThrowException(Integer amount) {
		Assertions.assertThrows(CashMachineValidateException.class, 
				() -> validator.validate(CashAmount.from(amount)));
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 10, 20, 80, 120, 250})
	void whenCashAmountIsIntegerMultipleByTenThenValidationOk(Integer amount) {
		Assertions.assertDoesNotThrow(() -> validator.validate(CashAmount.from(amount)));
	}
}
