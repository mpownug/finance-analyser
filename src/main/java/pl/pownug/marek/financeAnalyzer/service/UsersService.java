package pl.pownug.marek.financeAnalyzer.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.domain.UserRole;
import pl.pownug.marek.financeAnalyzer.repository.AccountRepository;
import pl.pownug.marek.financeAnalyzer.repository.CategoryRepository;
import pl.pownug.marek.financeAnalyzer.repository.TransactionRepository;
import pl.pownug.marek.financeAnalyzer.repository.UsersRepositiry;

@Service
@Transactional
public class UsersService {

	@Autowired
	UsersRepositiry usersRepo;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public Set<User> findAllUsers() {
		return usersRepo.findAll();
	}
	
	public User findById(int id) {
		return usersRepo.findById(id);
	}
	
	public Set<User> findById(List<Integer> ids) {
		return usersRepo.findById(ids);
	}
	
	public void persistUser(User user) {
		usersRepo.persist(user);
	}
	
	public void updateUser(User user) {
		User entity = usersRepo.findById(user.getId());
		if(entity!=null) {
			entity.setEmail(user.getEmail());
			entity.setFirstName(user.getFirstName());
			entity.setLastName(user.getLastName());
			if(!user.getPassword().isEmpty()) {
				entity.setPassword(user.getPassword());
			}
			entity.getRoles().clear();
			for(UserRole role: user.getRoles()) {
				role.setId(null);
			}
			entity.getRoles().addAll(user.getRoles());
		}		
	}
	
	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	public boolean checkPassword(User user, String password) {
		return passwordEncoder.matches(password, user.getPassword());
	}
	
	public boolean isMailDuplicated(User user) {	
		User found = usersRepo.findByEmail(user.getEmail());
		return found != null && !user.isUser(found);
	}
	
	public void loadRoles(User user) {
		user.setRoles(usersRepo.findRolesByUser(user));
	}
}
