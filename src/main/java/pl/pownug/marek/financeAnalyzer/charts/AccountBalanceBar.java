package pl.pownug.marek.financeAnalyzer.charts;

import pl.pownug.marek.financeAnalyzer.domain.Account;

public class AccountBalanceBar {
	
	private Account account;
	private float percentageIncome;
	private float percentageExpenditure;
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public float getPercentageIncome() {
		return percentageIncome;
	}
	public void setPercentageIncome(float percentageIncome) {
		this.percentageIncome = percentageIncome;
	}
	public float getPercentageExpenditure() {
		return percentageExpenditure;
	}
	public void setPercentageExpenditure(float percentageExpenditure) {
		this.percentageExpenditure = percentageExpenditure;
	}
	
	
}
