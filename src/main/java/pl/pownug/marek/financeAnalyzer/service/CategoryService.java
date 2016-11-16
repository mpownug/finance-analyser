package pl.pownug.marek.financeAnalyzer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.pownug.marek.financeAnalyzer.domain.Category;
import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.repository.CategoryRepository;
import pl.pownug.marek.financeAnalyzer.repository.TransactionRepository;

@Service
@Transactional
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	public Category findById(int id) {
		return categoryRepository.findById(id);
	}
	
	public void updateCategory(Category category) {
		Category entity = categoryRepository.findById(category.getId());
		if(entity != null) {
			entity.setName(category.getName());
			entity.setUser(User.getAuthenticatedUser());
		}
	}
	
	public void persistCategory(Category category) {
		categoryRepository.persist(category);		
	}
	
	public void deleteCategoryById(int id) {
		categoryRepository.deleteCategoryById(id);
    }
	
	public List<Category> findUserCategories(User user) {
		return categoryRepository.findUserCategories(user);
	}
}
