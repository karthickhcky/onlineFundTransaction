package com.krypton.onlineFundTransaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krypton.onlineFundTransaction.repository.AccountsRepository;
import com.krypton.onlineFundTransaction.vo.AccountsVO;

@Service
public class AccountsService {

	private static final Logger log = LoggerFactory.getLogger(AccountsService.class);
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	public AccountsVO getAccountDetails(String cifID, String accountNumber) {
		log.info("Inside AccountsService getAccountDetails method");
		AccountsVO account = accountsRepository.findByAccountNumber(cifID, accountNumber);
		log.info("Received response from accountsRepository");
		return account;
	}
}
