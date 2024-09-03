package com.krypton.onlineFundTransaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity(name="krypton_accounts")
@Table(name="krypton_accounts")
public class AccountEntity {
	
	@Id
	private String account_number;
	private String cif;
	private String balance;
	private String currency;
	private String monthly_limit;
	private String daily_limit;
	private String is_active;
}
