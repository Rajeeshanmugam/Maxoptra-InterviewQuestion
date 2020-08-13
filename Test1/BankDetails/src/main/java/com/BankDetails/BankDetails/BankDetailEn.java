package com.BankDetails.BankDetails;

import java.sql.Date;

public class BankDetailEn {
	private String bankName;
	private String cardNo;
	private String expireDate;

	
	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getCardNo() {
		return cardNo;
	}


	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}





	public String getExpireDate() {
		return expireDate;
	}


	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}


	public String toString() {
		return bankName + cardNo + expireDate;
	}
}
