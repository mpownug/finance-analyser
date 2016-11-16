package pl.pownug.marek.financeAnalyzer.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import pl.pownug.marek.financeAnalyzer.domain.Account;
import pl.pownug.marek.financeAnalyzer.domain.Category;
import pl.pownug.marek.financeAnalyzer.domain.Transaction;
import pl.pownug.marek.financeAnalyzer.domain.User;

@Repository
public class TransactionRepository extends AbstractRepository<Integer, Transaction> 
{

	public Transaction findById(int id) {
		return getByKey(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> findAll() {
		return getSession().createQuery(
				"FROM Transaction ORDER BY date DESC").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> findByCategory(Category category) {
		return getSession().createQuery(
				"FROM Transaction AS transaction WHERE transaction.category = :category")
				.setEntity("category", category)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> findByAccount(Account account) {
		return getSession().createQuery(
				"FROM Transaction AS transaction WHERE transaction.account = :account")
				.setEntity("account", account)
				.list();
	}
	
	@SuppressWarnings("unchecked")
    public List<Transaction> findUserTransactions(User user) {
    	return new ArrayList<Transaction>
    		(
    			getSession()
	    			.createQuery("FROM Transaction AS t WHERE t.user = :user ORDER BY date DESC")
	    		    .setEntity("user", user)
	    		    .list()
	    	);
    }
}
