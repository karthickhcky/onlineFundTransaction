package com.krypton.onlineFundTransaction.vo;

import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class AccountsVO {
	
	private String account_number;
    private String cif;
    private String balance;
    private String currency;
    private String is_active;
    private Limits limits;
    
}
