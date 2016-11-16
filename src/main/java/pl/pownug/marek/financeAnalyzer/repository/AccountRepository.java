package pl.pownug.marek.financeAnalyzer.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import pl.pownug.marek.financeAnalyzer.domain.Account;
import pl.pownug.marek.financeAnalyzer.domain.Transaction;
import pl.pownug.marek.financeAnalyzer.domain.User;

@Repository
public class AccountRepository extends AbstractRepository<Integer, Account>
{
	public Account findById(int id) {
		return getByKey(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Account> findAll() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("name"));
		return (List<Account>) criteria.list();
	}
	
	public Account findByTransaction(Transaction transaction) {
		return (Account) getSession().createQuery(
				"FROM Account AS account WHERE account = (SELECT transaction.account FROM Transaction as transaction WHERE transaction = :transaction)")
				.setEntity("transaction", transaction)
				.uniqueResult();	
	}
	
	@SuppressWarnings("unchecked")
    public List<Account> findUserAccounts(User user) {
    	return new ArrayList<Account>
    		(
    			getSession()
	    			.createQuery("FROM Account AS a WHERE a.user = :user ORDER BY name ASC")
	    		    .setEntity("user", user)
	    		    .list()
	    	);
    }

}
