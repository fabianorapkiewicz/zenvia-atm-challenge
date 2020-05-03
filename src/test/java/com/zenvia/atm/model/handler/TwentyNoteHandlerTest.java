package com.zenvia.atm.model.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.zenvia.atm.exception.CashMachineException;
import com.zenvia.atm.model.Cash;
import com.zenvia.atm.model.CashAmount;
import com.zenvia.atm.model.WadCash;

class TwentyNoteHandlerTest {

private CashHandler nextHandler;	
	
	@BeforeEach
	public void setUp() {
		nextHandler = Mockito.mock(CashHandler.class);
	}
	
	@ParameterizedTest
	@MethodSource("provideAlotOfValidMultiplesOfTwenty")
	public void givenInfinitiveNotesWhenCashAmountIsValidThenGetCash(
			CashAmount amount, List<Cash> expected) throws CashMachineException {
		
		WadCash collector = new WadCash();
		getInfinitiveHandler().dispense(amount, collector);
		
		assertEquals(expected, collector.getCash());
		
		verify(nextHandler, never()).dispense(any(), any());
	}
	
	@Test
	public void givenFinitiveNotesWhenCashAmounIsGreaterThanLimitThenCallNext()
			throws CashMachineException {
		
		List<Cash> expected = List.of(new Cash(20, 3));
		
		WadCash collector = new WadCash();
		getFinitiveHandler(3).dispense(CashAmount.from(100), collector);
		
		assertEquals(expected, collector.getCash());
		
		verify(nextHandler, times(1)).dispense(CashAmount.from(40), collector);
	}
	
	@ParameterizedTest
	@MethodSource("provideDataToDifferentNotes")
	public void whentCashAmountNeedsDifferentNotesThenNextHandlerIsCalled(
			CashAmount amount, List<Cash> expected, Integer difference) throws CashMachineException {
		
		WadCash collector = new WadCash();
		getInfinitiveHandler().dispense(amount, collector);
		
		assertEquals(expected, collector.getCash());
		
		if(difference > 0)
			verify(nextHandler, times(1)).dispense(CashAmount.from(difference), collector);
		else
			verify(nextHandler, never()).dispense(any(), any());
	}
	
	private CashHandler getInfinitiveHandler() {
		Optional<Integer> numberOfNotes = Optional.ofNullable(null);
		CashHandler infinitiveHandler = new TwentyNoteHandler(numberOfNotes);
		infinitiveHandler.appendHandler(nextHandler);
		return infinitiveHandler;
	}
	
	private CashHandler getFinitiveHandler(Integer numberOfNotes) {
		Optional<Integer> limit = Optional.ofNullable(numberOfNotes);
		CashHandler finitiveHandler = new TwentyNoteHandler(limit);
		finitiveHandler.appendHandler(nextHandler);
		return finitiveHandler;
	}

	private static Stream<Arguments> provideAlotOfValidMultiplesOfTwenty() {
	    return Stream.of(
	      Arguments.of(CashAmount.from(100), List.of(new Cash(20, 5))),
	      Arguments.of(CashAmount.from(500), List.of(new Cash(20, 25))),
	      Arguments.of(CashAmount.from(1000), List.of(new Cash(20, 50))),
	      Arguments.of(CashAmount.from(3700), List.of(new Cash(20, 185))),
	      Arguments.of(CashAmount.from(47200), List.of(new Cash(20, 2360)))
	    );
	}
	
	private static Stream<Arguments> provideDataToDifferentNotes() {
	    return Stream.of(
	      Arguments.of(CashAmount.from(10), List.of(), 10),
	      Arguments.of(CashAmount.from(30), List.of(new Cash(20, 1)), 10),
	      Arguments.of(CashAmount.from(130), List.of(new Cash(20, 6)), 10),
	      Arguments.of(CashAmount.from(270), List.of(new Cash(20, 13)), 10)
	    );
	}

}
