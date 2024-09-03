DROP TABLE IF EXISTS krypton_accounts;

CREATE TABLE krypton_accounts (
   account_number NVARCHAR(11) PRIMARY KEY,
   cif NVARCHAR(10),
   balance NVARCHAR(20),
   currency NVARCHAR(3),
   monthly_limit NVARCHAR(20),
   daily_limit NVARCHAR(20),
   is_active NVARCHAR(1)
);

DROP TABLE IF EXISTS krypton_transactiondetails;

CREATE TABLE krypton_transactiondetails (
   transaction_number NVARCHAR(15) PRIMARY KEY,
   debit_accountNo NVARCHAR(11),
   credit_accountNo NVARCHAR(24),
   debit_amount NVARCHAR(20),
   debit_currency NVARCHAR(3),
   reason NVARCHAR(6),
   transaction_status NVARCHAR(15),
   failure_reason NVARCHAR(30),
   notes NVARCHAR(125),
   time timestamp
);