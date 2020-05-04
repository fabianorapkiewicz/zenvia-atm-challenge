package com.zenvia.atm.model;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import com.zenvia.atm.exception.CashMachineException;
import com.zenvia.atm.exception.CashMachineValidateException;
import com.zenvia.atm.model.handler.CashCollector;
import com.zenvia.atm.model.handler.GenericCashHandler;
import com.zenvia.atm.validation.CashMachineValidator;

@SpringBootTest
class CashMachineTest {

	@Mock
	private GenericCashHandler cashHandler;

	@Mock
	private CashMachineValidator validator;

	@InjectMocks
	private CashMachine cashMachine;

	@ParameterizedTest
	@MethodSource("provideInputAndOutputValidToDispense")
	public void whenCashAmountIsValidThenCashMachineReturnsCashList(
			CashAmount amount, List<WadOfCash> expected) throws CashMachineException {
		
		whenCashHandlerDispenseIsCalledThenDelivery(expected);
		
		Assertions.assertEquals(expected, cashMachine.withdraw(amount));

		verify(validator, times(1)).validate(amount);
		verify(cashHandler, times(1)).dispense(any(CashAmount.class), any(CashCollector.class));
	}
	
	@ParameterizedTest
	@ValueSource( ints = {12, 17, 145})
	public void whenCashAmountIsInvalidThenCashMachineThrowsException(Integer amount ) 
			throws CashMachineException {
		
		doThrow(CashMachineValidateException.class)
		.when(validator).validate(CashAmount.from(amount));
		
		Assertions.assertThrows(CashMachineValidateException.class, 
				() -> cashMachine.withdraw(CashAmount.from(amount)));
		
		verify(validator, times(1)).validate(CashAmount.from(amount));
		
	}
	
	private void whenCashHandlerDispenseIsCalledThenDelivery(List<WadOfCash> expected) throws CashMachineException {
		doAnswer((Answer<Void>) invocation -> {
			CashCollector cashController = invocation.getArgument(1);
			
			for(WadOfCash cash : expected)
				for(int i = 0; i < cash.getAmount(); i++)
					cashController.addCash(CashAmount.from(cash.getValue()));
			
			return null;
		}).when(cashHandler).dispense(any(CashAmount.class), any(CashMachineCollector.class));
	}
	
	private static Stream<Arguments> provideInputAndOutputValidToDispense() {
	    return Stream.of(
	      Arguments.of(CashAmount.from(10), List.of(new WadOfCash(10, 1))),
	      Arguments.of(CashAmount.from(20), List.of(new WadOfCash(20, 1))),
	      Arguments.of(CashAmount.from(50), List.of(new WadOfCash(50, 1))),
	      Arguments.of(CashAmount.from(100), List.of(new WadOfCash(100, 1))),
	      Arguments.of(CashAmount.from(30), List.of(new WadOfCash(10, 1), new WadOfCash(20, 1))),
	      Arguments.of(CashAmount.from(80), List.of(new WadOfCash(10, 1), new WadOfCash(20, 1), new WadOfCash(50, 1))),
	      Arguments.of(CashAmount.from(500), List.of(new WadOfCash(100, 5)))
	    );
	}

}
