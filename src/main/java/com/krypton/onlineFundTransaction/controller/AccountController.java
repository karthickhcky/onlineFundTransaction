package com.krypton.onlineFundTransaction.controller;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.krypton.onlineFundTransaction.service.AccountsService;
import com.krypton.onlineFundTransaction.vo.AccountsVO;

@RestController
@RequestMapping
public class AccountController {

	private static final Logger log = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountsService accountService;

	/***
	 *  Method to fetch Account Details based on cifID and Account Number
	 *  
	 * @param cifID
	 * @param accountNumber
	 * @return
	 */
	@GetMapping(path="/v1/accounts")
	public ResponseEntity<?> getAccountDetails(@RequestParam("cifID") String cifID, @RequestParam("accountNumber") String accountNumber) {
		log.info("Request received to fetch accounts details");
		String response = null;
		try {
			HashMap<String, String> responseMap = new HashMap<>();
			Gson gson = new GsonBuilder().serializeNulls().create();
			Type accountRespType = new TypeToken<AccountsVO>() {}.getType();
			Type responseMapType = new TypeToken<HashMap<String, String>>() {}.getType();
			Optional<AccountsVO> account = Optional.ofNullable(accountService.getAccountDetails(cifID, accountNumber));

			if(account.get().getAccount_number() == null) {
				responseMap.put("httpCode", String.valueOf(HttpStatus.BAD_REQUEST.value()));
				responseMap.put("httpReason", "Account Not Found");
				log.info("Account Number not found.");
			}
			response = gson.toJson(account.get(), accountRespType);
			if(responseMap.size() > 0) {
				response =  gson.toJson(responseMap, responseMapType);
			}
		} catch(Exception e) {
			log.error("Exception occurred while fetching account details:"+e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}