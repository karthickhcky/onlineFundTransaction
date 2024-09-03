package com.krypton.onlineFundTransaction.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.krypton.onlineFundTransaction.constants.TransactionConstants;
import com.krypton.onlineFundTransaction.vo.AccountsVO;
import com.krypton.onlineFundTransaction.vo.Limits;

import jakarta.persistence.EntityManager;

@Component
@Transactional(readOnly = true)
public class AccountsRepository {

	private static final Logger log = LoggerFactory.getLogger(AccountsRepository.class);

	@Autowired
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public AccountsVO findByAccountNumber(String cifID, String accountNumber) {
		log.info("Inside AccountsRepository findByAccountNumber method");
		List<Object[]> accountList = entityManager.createNativeQuery(TransactionConstants.SELECT_ACCOUNTS_QUERY)
		.setParameter(1, cifID)
		.setParameter(2, accountNumber)
		.getResultList();
		log.info("fetched accountList size:"+accountList.size());
		AccountsVO accountsVO = new AccountsVO();
		for(Object[] value : accountList) {
			accountsVO.setAccount_number((String) value[0]);
			accountsVO.setCif((String) value[1]);
			accountsVO.setBalance((String) value[2]);
			accountsVO.setCurrency((String) value[3]);
			Limits limitsVO = new Limits();
			limitsVO.setMonthly((String) value[4]);
			limitsVO.setDaily((String) value[5]);
			accountsVO.setLimits(limitsVO);
			accountsVO.setIs_active(Character.toString((Character) value[6]));
		}
		log.info("Completed findByAccountNumber method");
		return accountsVO;
	}
}
