package com.krypton.onlineFundTransaction.repository;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.krypton.onlineFundTransaction.constants.EnumTransactionStatus;
import com.krypton.onlineFundTransaction.constants.TransactionConstants;
import com.krypton.onlineFundTransaction.vo.TransactionRequestVO;
import com.krypton.onlineFundTransaction.vo.TransactionResponseVO;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Component
public class TransactionRepository {

	private static final Logger log = LoggerFactory.getLogger(TransactionRepository.class);

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public TransactionResponseVO saveTransaction(TransactionRequestVO transactionRequestVO, TransactionResponseVO transactionResponseVO) {
		log.info("Inside SaveTransaction method");
		try {
			transactionResponseVO.setTransactionNumber(transactionRequestVO.getTransactionNumber());
			transactionResponseVO.setTimeStamp(new Timestamp(new Date().getTime()));
			if(transactionResponseVO.getTransactionStatus() == EnumTransactionStatus.PENDING) {
				log.info("Transaction status is Updated as CREDITED");
				transactionResponseVO.setTransactionStatus(EnumTransactionStatus.CREDITED);
			}

			if(transactionResponseVO.getFailureReason() == null) {
				int updateResult = entityManager.createQuery(TransactionConstants.UPDATE_ACCOUNTS_QUERY)
						.setParameter(1, transactionRequestVO.getUpdatedBalance())
						.setParameter(2, transactionRequestVO.getUpdatedDailyLimit())
						.setParameter(3, transactionRequestVO.getUpdatedMonthlyLimit())
						.setParameter(4, transactionRequestVO.getSourceAccount())
						.setParameter(5, transactionRequestVO.getCifID())
						.executeUpdate();
				if(updateResult != 1) {
					log.info("Transaction status is Updated as FAILED");
					transactionResponseVO.setTransactionStatus(EnumTransactionStatus.FAILED);
				}
			}
			String transactionStatus = transactionResponseVO.getTransactionStatus() != null ? transactionResponseVO.getTransactionStatus().toString() : null;
			String failureReason = transactionResponseVO.getFailureReason() != null ? transactionResponseVO.getFailureReason().toString() : null;
			int insertResult = entityManager.createNativeQuery(TransactionConstants.INSERT_TRANSACTION_QUERY)
					.setParameter(1, transactionResponseVO.getTransactionNumber())
					.setParameter(2, transactionRequestVO.getSourceAccount())
					.setParameter(3, transactionRequestVO.getDestinationAccount())
					.setParameter(4, transactionRequestVO.getAmount())
					.setParameter(5, transactionRequestVO.getCurrency())
					.setParameter(6, transactionRequestVO.getReason())
					.setParameter(7, transactionStatus)
					.setParameter(8, failureReason)
					.setParameter(9, transactionRequestVO.getNotes())
					.setParameter(10, transactionResponseVO.getTimeStamp())
					.executeUpdate();
			if(insertResult != 1) {
				log.info("Transaction status is Updated as FAILED");
				transactionResponseVO.setTransactionStatus(EnumTransactionStatus.FAILED);
			}
			log.info("Completed saveTransaction method");
		} catch(Exception e) {
			log.error("Exception while saving transaction data in DB:"+e.getMessage());
		}
		return transactionResponseVO;
	}
}