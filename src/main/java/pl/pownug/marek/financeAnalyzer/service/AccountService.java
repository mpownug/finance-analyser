package pl.pownug.marek.financeAnalyzer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.pownug.marek.financeAnalyzer.domain.Account;
import pl.pownug.marek.financeAnalyzer.domain.Transaction;
import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.repository.AccountRepository;
import pl.pownug.marek.financeAnalyzer.repository.TransactionRepository;

@Service
@Transactional
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	
	public Account findById(int id) {
		return accountRepository.findById(id);
	}	
	
	public void updateCategory(Account account) {
		Account entity = accountRepository.findById(account.getId());
		if(entity != null) {
			entity.setName(account.getName());
			entity.setUser(User.getAuthenticatedUser());
		}
	}
	
	public void persistCategory(Account account) {
		accountRepository.persist(account);		
	}
	
    public void deleteAccount(Account account) {
    	
    	List<Transaction> accountTransactions = transactionRepository.findByAccount(account);
    	for (Transaction transaction : accountTransactions) {
			transactionRepository.delete(transaction);
		}
    	accountRepository.delete(account);
    }
	
	public List<Account> findUserAccounts(User user) {
		return accountRepository.findUserAccounts(user);
	}
}
