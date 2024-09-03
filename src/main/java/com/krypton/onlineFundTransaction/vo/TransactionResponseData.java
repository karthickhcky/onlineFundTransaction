package com.krypton.onlineFundTransaction.vo;

import com.krypton.onlineFundTransaction.constants.EnumTransactionStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class TransactionResponseData {
	
	@Enumerated(EnumType.STRING)
	private EnumTransactionStatus status;
	private TransactionResponseVO data;
}
