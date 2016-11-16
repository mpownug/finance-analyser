package pl.pownug.marek.financeAnalyzer.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.domain.UserRole;

@Repository
public class UsersRepositiry extends AbstractRepository<Integer, User> {
	
	@SuppressWarnings("unchecked")
	public Set<User> findAll() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("lastName"));
		return new TreeSet<User>(criteria.list());
	}
	
	@SuppressWarnings("unchecked")
    public Set<User> findById(List<Integer> ids) {
    	String list = ids.toString();
    	list = list.substring(1, list.length()-1);
    	return new TreeSet<User>(
    			getSession()
    				.createQuery("FROM User AS u WHERE u.id IN (" + list +  ") ORDER BY u.lastName,u.firstName ASC")
    				.list()
    		);
    }
	
	public User findById(int id) {
		return getByKey(id);
	}
	
	public User findByEmail(String email) {
    	return (User) getSession()
    			.createQuery("FROM User AS user WHERE user.email = :email")
    		    .setString("email", email)
    		    .uniqueResult();
    }
	
	@SuppressWarnings("unchecked")
	public Set<UserRole> findRolesByUser(User user) {
		return new HashSet<UserRole>(
    			getSession()
	    			.createQuery("SELECT u.roles FROM User AS u  WHERE u = :user")
	    		    .setEntity("user", user)
	    		    .list()
	    	);
	}
}