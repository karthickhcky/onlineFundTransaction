package com.krypton.onlineFundTransaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krypton.onlineFundTransaction.repository.TransactionRepository;
import com.krypton.onlineFundTransaction.vo.TransactionRequestVO;
import com.krypton.onlineFundTransaction.vo.TransactionResponseVO;

@Service
public class TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
	
	@Autowired
	TransactionRepository transactionRepository;
	
	public TransactionResponseVO transferAmount(TransactionRequestVO transferRequest, TransactionResponseVO responseVO) {
		log.info("Inside TransactionService transferAmount method");
		TransactionResponseVO transferResponseVO = transactionRepository.saveTransaction(transferRequest, responseVO);
		log.info("Received response from transactionRepository");
		return transferResponseVO;
	}
}
