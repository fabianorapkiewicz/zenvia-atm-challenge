package com.zenvia.atm.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zenvia.atm.controller.dto.WadCashDTO;
import com.zenvia.atm.exception.CashAmountInvalidException;
import com.zenvia.atm.exception.CashMachineException;
import com.zenvia.atm.exception.CashMachineValidateException;
import com.zenvia.atm.model.CashAmount;
import com.zenvia.atm.model.CashMachine;

@RestController
@RequestMapping(value = "/atm/operacoes/", produces = "application/json;charset=UTF-8")
public class ATMOperationController {

	private CashMachine cashMachine;
	
	@Autowired
	public ATMOperationController(CashMachine cashMachine) {
		this.cashMachine = cashMachine;
	}
	
	@ResponseBody
	@RequestMapping("/saque/{amount}")
	public ResponseEntity<?> withdrawMoney(@PathVariable("amount") String amount){
		try {
			
			var cash = cashMachine.dispense(CashAmount.from(amount));
			return ResponseEntity.accepted().body( new WadCashDTO(cash));
			
		} catch (CashMachineException exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(exception.getMessage());
		} catch (CashAmountInvalidException exception) {
			return ResponseEntity.badRequest()
					.body("Valor informado para saque é inválido");
		} catch(CashMachineValidateException exception) {
			return ResponseEntity.badRequest()
					.body(exception.getMessage());
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Valor informado para saque é inválido");
		}
	}
}