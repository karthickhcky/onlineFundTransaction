package com.krypton.onlineFundTransaction.controller;

import java.lang.reflect.Type;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.krypton.onlineFundTransaction.constants.EnumFailureReasons;
import com.krypton.onlineFundTransaction.constants.EnumTransactionStatus;
import com.krypton.onlineFundTransaction.service.TransactionService;
import com.krypton.onlineFundTransaction.vo.AccountsVO;
import com.krypton.onlineFundTransaction.vo.TransactionRequestVO;
import com.krypton.onlineFundTransaction.vo.TransactionResponseData;
import com.krypton.onlineFundTransaction.vo.TransactionResponseVO;

@RestController
@RequestMapping
public class TransactionController {

	private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountController accountController;
	
	/***
	 * Method to get account details and validate it then transfer amount from source account to another account
	 * 
	 * @param transactionRequestVO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/v1/local-transfer")
	public ResponseEntity<?> transferAmount(@RequestBody TransactionRequestVO transactionRequestVO) {
		log.info("Request received to transfer amount");
		String response = null;
		try {
			TransactionResponseData responseData = new TransactionResponseData();
			TransactionResponseVO responseVO = new TransactionResponseVO();
			responseVO.setTimeStamp(new Timestamp(new Date().getTime()));
			transactionRequestVO.setTransactionNumber(generateRandomNumber());
			responseVO.setTransactionNumber(transactionRequestVO.getTransactionNumber());
			responseVO.setTransactionStatus(EnumTransactionStatus.PENDING);
			EnumFailureReasons failureReason = null;
			String updatedBalance = null;
			double updatedDailyLimit = 0;
			double updatedMonthlyLimit = 0;
			double fromAccountBalance = 0;
			double transferAmount = 0;
			if(transactionRequestVO.getCifID() != null && transactionRequestVO.getCifID().length() != 10) {
				failureReason = EnumFailureReasons.INVALID_CIF;
			} else if(transactionRequestVO.getSourceAccount() != null && transactionRequestVO.getSourceAccount().length() != 11) {
				failureReason = EnumFailureReasons.INVALID_SOURCE_ACCOUNT;
			} else if(transactionRequestVO.getDestinationAccount() != null && transactionRequestVO.getDestinationAccount().length() != 24) {
				failureReason = EnumFailureReasons.INVALID_DESTINATION_ACCOUNT;
			} else if(transactionRequestVO.getReason() != null && !transactionRequestVO.getReason().matches("[A-z]{1}[-]{1}[0-9]{4}")) {
				failureReason = EnumFailureReasons.INVALID_REASON;
			} else if(transactionRequestVO.getNotes() != null && transactionRequestVO.getNotes().length() > 125) {
				failureReason = EnumFailureReasons.INTERNAL_ERROR;
			} else {
				RestTemplate restTemplate = new RestTemplate();
				log.info("Sending Request to fetch account details");
				URI url = new URI("http://localhost:8080/v1/accounts?cifID="+transactionRequestVO.getCifID()+"&accountNumber="+transactionRequestVO.getSourceAccount());
				AccountsVO fromAccountsVO = restTemplate.getForObject(url, AccountsVO.class);
				log.info("Response received from fetch account details");
				if(fromAccountsVO.getAccount_number() == null) {
					failureReason = EnumFailureReasons.INVALID_SOURCE_ACCOUNT;
				} else {
					double dailyLimit = Double.parseDouble(fromAccountsVO.getLimits().getDaily());
					double monthlyLimit = Double.parseDouble(fromAccountsVO.getLimits().getMonthly());
					fromAccountBalance = Double.parseDouble(fromAccountsVO.getBalance());
					transferAmount = Double.parseDouble(transactionRequestVO.getAmount());
					updatedBalance = String.valueOf(fromAccountBalance - transferAmount);
					updatedDailyLimit = dailyLimit - transferAmount;
					updatedMonthlyLimit = monthlyLimit - transferAmount;

					if(!fromAccountsVO.getIs_active().equalsIgnoreCase("Y")) {
						failureReason = EnumFailureReasons.INACTIVE_ACCOUNT;
					} else if(!fromAccountsVO.getCurrency().equalsIgnoreCase(transactionRequestVO.getCurrency())) {
						failureReason = EnumFailureReasons.INVALID_CURRENCY;
					} else if(Double.parseDouble(fromAccountsVO.getBalance()) < Double.parseDouble(transactionRequestVO.getAmount())) {
						failureReason = EnumFailureReasons.INSUFFICIENT_BALANCE;
					} else if(dailyLimit < transferAmount || updatedDailyLimit < 0) {
						failureReason = EnumFailureReasons.DAILY_LIMIT;
					} else if(monthlyLimit < transferAmount || updatedMonthlyLimit < 0) {
						failureReason = EnumFailureReasons.MONTHLY_LIMIT;
					}
				}
			}
			if(failureReason != null) {
				log.info("Failure Reason:"+failureReason);
				responseVO.setFailureReason(failureReason);
				responseVO.setTransactionStatus(EnumTransactionStatus.FAILED);
			}
			transactionRequestVO.setUpdatedBalance(updatedBalance);
			transactionRequestVO.setUpdatedDailyLimit(String.valueOf(updatedDailyLimit));
			transactionRequestVO.setUpdatedMonthlyLimit(String.valueOf(updatedMonthlyLimit));

			responseVO = transactionService.transferAmount(transactionRequestVO, responseVO);

			log.info("Updating Response Status as SUCCESS");
			responseData.setStatus(EnumTransactionStatus.SUCCESS);
			responseData.setData(responseVO);

			Gson gson = new GsonBuilder().serializeNulls().create();
			Type transactionRespType = new TypeToken<TransactionResponseData>() {}.getType();
			response = gson.toJson(responseData, transactionRespType);
			log.info("Sending Fund Transaction Response");
		} catch(Exception e) {
			log.error("Exception in fund Transaction method:"+e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	private String generateRandomNumber() {
	    long smallest = 1000_0000_0000_000L;
	    long biggest =  9999_9999_9999_999L;
	    return String.valueOf(ThreadLocalRandom.current().nextLong(smallest, biggest+1));
	}
}