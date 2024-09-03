package com.krypton.onlineFundTransaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity(name="krypton_transactiondetails")
@Table(name="krypton_transactiondetails")
public class TransactionEntity {
	
	@Id
	private String transaction_number;
	private String debit_accountNo;
	private String credit_accountNo;
	private String debit_amount;
	private String debit_currency;
	private String reason;
	private String transaction_status;
	private String notes;
	private String time;
}
