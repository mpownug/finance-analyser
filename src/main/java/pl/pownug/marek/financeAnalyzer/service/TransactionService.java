package pl.pownug.marek.financeAnalyzer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.pownug.marek.financeAnalyzer.domain.Account;
import pl.pownug.marek.financeAnalyzer.domain.Category;
import pl.pownug.marek.financeAnalyzer.domain.Transaction;
import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.repository.AccountRepository;
import pl.pownug.marek.financeAnalyzer.repository.CategoryRepository;
import pl.pownug.marek.financeAnalyzer.repository.TransactionRepository;

@Service
@Transactional
public class TransactionService {
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	public Transaction findById(int id) {
		return transactionRepository.findById(id);
	}
	
	public void LoadCategory(List<Transaction> transactions) {
		for(Transaction transaction: transactions) {
			loadCategory(transaction);
		}
	}
	
	public void loadCategory(Transaction transaction) {
		transaction.setCategory(categoryRepository.findByTransaction(transaction));
	}
	
	public void LoadAccount(List<Transaction> transactions) {
		for(Transaction transaction: transactions) {
			loadAccount(transaction);
		}
	}
	
	public void loadAccount(Transaction transaction) {
		transaction.setAccount(accountRepository.findByTransaction(transaction));
	}
	
	public void persistTransaction(Transaction transaction) {
		transactionRepository.persist(transaction);
	}
	
	public void updateTransaction(Transaction transaction) {
		Transaction entity = transactionRepository.findById(transaction.getId());
		if(entity != null) {
			entity.setDate(transaction.getDate());
			entity.setCategory(transaction.getCategory());
			entity.setAccount(transaction.getAccount());
			entity.setAmount(transaction.getAmount());
			entity.setType(transaction.getType());
			entity.setTitle(transaction.getTitle());
			entity.setComment(transaction.getComment());
			entity.setUser(User.getAuthenticatedUser());
		}
	}
	
	public List<Transaction> findByCategory(Category category) {
		return transactionRepository.findByCategory(category);
	}
	
	public List<Transaction> findByAccount(Account account) {
		return transactionRepository.findByAccount(account);
	}
	
	public void deleteTransaction(Transaction transaction) {	
		transactionRepository.delete(transaction);
	}
	
	public List<Transaction> findUserTransactions(User user) {
		return transactionRepository.findUserTransactions(user);
	}
}
