package com.krypton.onlineFundTransaction.vo;


import java.sql.Timestamp;

import com.krypton.onlineFundTransaction.constants.EnumFailureReasons;
import com.krypton.onlineFundTransaction.constants.EnumTransactionStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class TransactionResponseVO {

	private Timestamp timeStamp;
	private String transactionNumber;
	
	@Enumerated(EnumType.STRING)
	private EnumTransactionStatus transactionStatus;

	@Enumerated(EnumType.STRING)
	private EnumFailureReasons failureReason;
}
