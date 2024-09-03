package com.krypton.onlineFundTransaction.constants;

public interface TransactionConstants {

	String SELECT_ACCOUNTS_QUERY = "SELECT account_number, cif, balance, currency, monthly_limit, daily_limit, is_active FROM krypton_accounts WHERE cif = ? AND account_number = ?";
	String UPDATE_ACCOUNTS_QUERY = "UPDATE krypton_accounts SET balance = ?1, daily_limit = ?2, monthly_limit = ?3 WHERE account_number = ?4 AND cif = ?5";
	String INSERT_TRANSACTION_QUERY = "INSERT INTO krypton_transactiondetails VALUES (?,?,?,?,?,?,?,?,?,?)";
	
}
