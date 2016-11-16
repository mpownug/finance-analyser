package pl.pownug.marek.financeAnalyzer.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.pownug.marek.financeAnalyzer.charts.AccountBalance;
import pl.pownug.marek.financeAnalyzer.charts.AccountBalanceBar;
import pl.pownug.marek.financeAnalyzer.domain.Account;
import pl.pownug.marek.financeAnalyzer.domain.Transaction;
import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.service.AccountService;
import pl.pownug.marek.financeAnalyzer.service.CategoryService;
import pl.pownug.marek.financeAnalyzer.service.TransactionService;

@Controller
public class OverviewController {
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	CategoryService categoryService;

	@RequestMapping(value = "/")
	public String index() 
	{
		return "redirect:/overview";
	}
	
	@RequestMapping(value = "/overview", method = RequestMethod.GET)
	public String index(ModelMap model) 
	{		
		
		List<Account> accounts = accountService.findUserAccounts(User.getAuthenticatedUser());
		
		List<AccountBalanceBar> accountBalanceBars = new ArrayList<AccountBalanceBar>();
		List<AccountBalance> accountBalances = new ArrayList<AccountBalance>();
		for (Account account : accounts) {
			AccountBalanceBar accountBalanceBar = new AccountBalanceBar();
			AccountBalance accountBalance = new AccountBalance();
			accountBalanceBar.setAccount(account);
			List<Transaction> accountsTransations= transactionService.findByAccount(account);
			float sum = sumIncome(accountsTransations) + sumExpenditure(accountsTransations);
			float accountIncome = sumIncome(accountsTransations);
			float accountExpenditure = sumExpenditure(accountsTransations);
			accountBalanceBar.setPercentageIncome(Math.round((accountIncome/sum)*100));
			accountBalanceBar.setPercentageExpenditure(Math.round((accountExpenditure/sum)*100));
			accountBalanceBars.add(accountBalanceBar);
			
			accountBalance.setAccount(account);
			accountBalance.setBalance(sumTransactions(accountsTransations));
			accountBalances.add(accountBalance);
		}
		model.addAttribute("accountBalanceBars", accountBalanceBars);
		model.addAttribute("accountBalances", accountBalances);
		model.addAttribute("accounts", accountService.findUserAccounts(User.getAuthenticatedUser()));
		return "overview/overview";
	}
	
	private float sumTransactions(List <Transaction> transactions) {
		float sum = 0;
		for (Transaction transaction : transactions) {
			if(transaction.getType().equals(1)) {
				sum += transaction.getAmount();
			}
			else {
				sum -= transaction.getAmount();
			}
		}
		return sum;
	}
	
	private float sumIncome(List <Transaction> transactions) {
		float sum = 0;
		for (Transaction transaction : transactions) {
			Date given = transaction.getDate();
			Date ref = new Date();
			if((transaction.getType().equals(1)) && given.getMonth() == ref.getMonth() && given.getYear() == ref.getYear()) {
				sum += transaction.getAmount();
			}
		}
		return sum;
	}
	
	private float sumExpenditure(List <Transaction> transactions) {
		float sum = 0;
		for (Transaction transaction : transactions) {
			Date given = transaction.getDate();
			Date ref = new Date();
			if((transaction.getType().equals(0)) && given.getMonth() == ref.getMonth() && given.getYear() == ref.getYear()) {
				sum += transaction.getAmount();
			}
		}
		return sum;
	}
	
}