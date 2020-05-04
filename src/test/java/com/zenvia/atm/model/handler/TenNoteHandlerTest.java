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
import com.zenvia.atm.model.WadOfCash;
import com.zenvia.atm.model.CashAmount;
import com.zenvia.atm.model.CashMachineCollector;

class TenNoteHandlerTest {

private CashHandler nextHandler;	
	
	@BeforeEach
	public void setUp() {
		nextHandler = Mockito.mock(CashHandler.class);
	}
	
	@ParameterizedTest
	@MethodSource("provideAlotOfValidMultiplesOfTen")
	public void givenInfinitiveNotesWhenCashAmountIsValidThenGetCash(
			CashAmount amount, List<WadOfCash> expected) throws CashMachineException {
		
		CashMachineCollector collector = new CashMachineCollector();
		getInfinitiveHandler().dispense(amount, collector);
		
		assertEquals(expected, collector.getWadOfCash());
		
		verify(nextHandler, never()).dispense(any(), any());
	}
	
	@Test
	public void givenFinitiveNotesWhenCashAmounIsGreaterThanLimitThenCallNext()
			throws CashMachineException {
		
		List<WadOfCash> expected = List.of(new WadOfCash(10, 7));
		
		CashMachineCollector collector = new CashMachineCollector();
		getFinitiveHandler(7).dispense(CashAmount.from(90), collector);
		
		assertEquals(expected, collector.getWadOfCash());
		
		verify(nextHandler, times(1)).dispense(CashAmount.from(20), collector);
	}
	
	private CashHandler getInfinitiveHandler() {
		Optional<Integer> numberOfNotes = Optional.ofNullable(null);
		CashHandler infinitiveHandler = new TenNoteHandler(numberOfNotes);
		infinitiveHandler.appendHandler(nextHandler);
		return infinitiveHandler;
	}
	
	private CashHandler getFinitiveHandler(Integer numberOfNotes) {
		Optional<Integer> limit = Optional.ofNullable(numberOfNotes);
		CashHandler finitiveHandler = new TenNoteHandler(limit);
		finitiveHandler.appendHandler(nextHandler);
		return finitiveHandler;
	}

	private static Stream<Arguments> provideAlotOfValidMultiplesOfTen() {
	    return Stream.of(
	      Arguments.of(CashAmount.from(10), List.of(new WadOfCash(10, 1))),
	      Arguments.of(CashAmount.from(500), List.of(new WadOfCash(10, 50))),
	      Arguments.of(CashAmount.from(1000), List.of(new WadOfCash(10, 100))),
	      Arguments.of(CashAmount.from(3700), List.of(new WadOfCash(10, 370))),
	      Arguments.of(CashAmount.from(47200), List.of(new WadOfCash(10, 4720)))
	    );
	}

}
