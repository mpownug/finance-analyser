package pl.pownug.marek.financeAnalyzer.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pl.pownug.marek.financeAnalyzer.domain.Category;
import pl.pownug.marek.financeAnalyzer.domain.Transaction;
import pl.pownug.marek.financeAnalyzer.domain.User;

@Repository
public class CategoryRepository extends AbstractRepository<Integer, Category> {
	
	
	public Category findById(int id) {
		return getByKey(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Category> findNotDeleted() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("name"))
				.add( Restrictions.eq("isDeleted", false) );
		return criteria.list();
	}
	
	public Category findByTransaction(Transaction transaction) {
		return (Category) getSession().createQuery(
				"FROM Category AS category WHERE category = (SELECT transaction.category FROM Transaction as transaction WHERE transaction = :transaction)")
				.setEntity("transaction", transaction)
				.uniqueResult();
				
	}
	
	public void deleteCategoryById(int id) {
        Query query = getSession().createSQLQuery("UPDATE category SET isdeleted=true WHERE id = :id");
        query.setInteger("id", id);
        query.executeUpdate();
    }
	
	@SuppressWarnings("unchecked")
    public List<Category> findUserCategories(User user) {
    	return new ArrayList<Category>
    		(
    			getSession()
	    			.createQuery("FROM Category AS c WHERE c.user = :user AND isDeleted = false ORDER BY name ASC")
	    		    .setEntity("user", user)
	    		    .list()
	    	);
    }
	
}
