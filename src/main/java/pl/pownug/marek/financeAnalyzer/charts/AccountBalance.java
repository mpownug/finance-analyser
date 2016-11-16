package pl.pownug.marek.financeAnalyzer.charts;

import pl.pownug.marek.financeAnalyzer.domain.Account;

public class AccountBalance {

	private Account account;
	private float balance;
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	
	
}
