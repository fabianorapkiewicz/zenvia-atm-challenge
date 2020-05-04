package com.zenvia.atm.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.zenvia.atm.model.Cash;
import com.zenvia.atm.model.CashAmount;
import com.zenvia.atm.model.CashMachine;

@WebMvcTest(ATMOperationController.class)
class ATMOperationControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CashMachine cashMachine;

	@ParameterizedTest(name = "Verificando a correspondência de solicitaão HTTP")
	@ValueSource(strings = {"2", "83", "393"})
	public void whenAmountIsIntegerPositive_thenReturns2xx(String amountParam) throws Exception {		
		 get("/terminal/operacoes/saque/{amount}", amountParam)
		 .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}
	
	@ParameterizedTest(name = "Verificando a validação de entrada")
	@ValueSource(strings = {"letras", "9.89", "-12"})
	public void whenAmountIsNotInteger_thenReturns4xx(String amountParam) throws Exception {
		 get("/terminal/operacoes/saque/{amount}", amountParam)
		 .andExpect(MockMvcResultMatchers.status().is4xxClientError())
		 .andExpect(MockMvcResultMatchers.content().string("Valor informado para saque é inválido"));
	}
	
	@ParameterizedTest(name = "Verificando chamada a lógica de negócio")
	@ValueSource(strings = {"12", "80", "10500"})
	public void whenAmountIsIntegerMulitpleOfTen_thenIntegerToWithdraw(
			String amountParam) throws Exception {
			
		get("/terminal/operacoes/saque/{amount}", amountParam);
		
		verify(cashMachine, times(1)).dispense(CashAmount.from(amountParam));
	}
	
	@ParameterizedTest(name = "Verificando serialização da saída")
	@MethodSource("providePerfectWithdrawMoney")
	public void whenAmountIsIntegerMulitpleOfTen_thenReturnsJsonWadOfCash(
			ArgumentsAccessor arguments) throws Exception {
			
		String amountParam = arguments.getString(0);
		String jsonExpected = arguments.getString(1);
		Integer valueCash = arguments.getInteger(2);
		Integer amountCash = arguments.getInteger(3);
		
		when(cashMachine.dispense(CashAmount.from(amountParam)))
		.thenReturn(List.of(new Cash(valueCash, amountCash)));
		
		 get("/terminal/operacoes/saque/{amount}", amountParam)
		 .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		 .andExpect(MockMvcResultMatchers.content().json(jsonExpected));
	}
	
	public ResultActions get(String url, Object params) throws Exception {
		return mockMvc.perform(MockMvcRequestBuilders
				.get(url, params)
				.contentType("application/json"));
	}
	
	private static Stream<Arguments> providePerfectWithdrawMoney() {
	    return Stream.of(
	      Arguments.of("10", "{\"banknotes\":[{\"value\":10,\"amount\":1}]}", 10, 1),
	      Arguments.of("20", "{\"banknotes\":[{\"value\":20,\"amount\":1}]}", 20, 1),
	      Arguments.of("50", "{\"banknotes\":[{\"value\":50,\"amount\":1}]}", 50, 1),
	      Arguments.of("100", "{\"banknotes\":[{\"value\":100,\"amount\":1}]}", 100, 1)
	    );
	}
}
