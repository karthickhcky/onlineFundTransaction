package com.krypton.onlineFundTransaction.vo;

import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class TransactionRequestVO {

	private String cifID;
	private String sourceAccount;
	private String destinationAccount;
	private String amount;
	private String currency;
	private String reason;
	private String notes;
	private String transactionNumber;
	private String updatedBalance;
	private String updatedDailyLimit;
	private String updatedMonthlyLimit;

}
