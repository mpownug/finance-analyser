package pl.pownug.marek.financeAnalyzer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.pownug.marek.financeAnalyzer.domain.Account;
import pl.pownug.marek.financeAnalyzer.domain.Category;
import pl.pownug.marek.financeAnalyzer.domain.Transaction;
import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.helpers.Message;
import pl.pownug.marek.financeAnalyzer.service.AccountService;
import pl.pownug.marek.financeAnalyzer.service.CategoryService;
import pl.pownug.marek.financeAnalyzer.service.TransactionService;

@Controller
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	AccountService accountService;
	
	@RequestMapping(value = "/transactions", method = RequestMethod.GET)
	public String transactions(ModelMap model) {
		List<Transaction> transactions = transactionService.findUserTransactions(User.getAuthenticatedUser());
		transactionService.LoadCategory(transactions);
		transactionService.LoadAccount(transactions);
		model.addAttribute("transactions", transactions);
		return "transactions/list";
	}
	
	@RequestMapping(value = "/transactions/new", method = RequestMethod.GET)
	public String newTransactions(ModelMap model)
	{			
		Transaction transaction = new Transaction();
		model.addAttribute("transaction", transaction);
		model.addAttribute("categories", categoryService.findUserCategories(User.getAuthenticatedUser()));
		model.addAttribute("accounts", accountService.findUserAccounts(User.getAuthenticatedUser()));
		return "transactions/editor";
	}
	
	@RequestMapping(value = "/transactions/edit/{id}", method = RequestMethod.GET)
	public String editTransaction(@PathVariable int id, ModelMap model)
	{
		Transaction transaction = transactionService.findById(id);
		transactionService.loadCategory(transaction);
		transactionService.loadAccount(transaction);
		
		model.addAttribute("transaction", transaction);
		model.addAttribute("formattedDate", transaction.getFormattedDate());
		model.addAttribute("selectedCategory", transaction.getCategory().getId());
		model.addAttribute("selectedAccount", transaction.getAccount().getId());
		model.addAttribute("categories", categoryService.findUserCategories(User.getAuthenticatedUser()));
		model.addAttribute("accounts", accountService.findUserAccounts(User.getAuthenticatedUser()));
		return "transactions/editor";
	}
	
	@RequestMapping(value = "/transactions/save", method = RequestMethod.POST)
	public String saveTransaction(@Valid @ModelAttribute Transaction transaction,
							 BindingResult result, 
							 @RequestParam int categoryId,
							 @RequestParam int accountId,
							 ModelMap model,
							 final RedirectAttributes redirectAttributes)
	{		
		List<Message> messages = new ArrayList<Message>();
		
		Category category = categoryService.findById(categoryId);
		transaction.setCategory(category);
		
		Account account = accountService.findById(accountId);
		transaction.setAccount(account);
		
		if(result.hasErrors()) {
			model.addAttribute("transaction", transaction);
			model.addAttribute("categories", categoryService.findUserCategories(User.getAuthenticatedUser()));
			model.addAttribute("accounts", accountService.findUserAccounts(User.getAuthenticatedUser()));
			model.addAttribute("selectedCategory", transaction.getCategory().getId());
			model.addAttribute("selectedAccount", transaction.getAccount().getId());
			model.addAttribute("formattedDate", transaction.getFormattedDate());
            return "transactions/editor";
        }
		
		if(transaction.isNew()) {
			transaction.setUser(User.getAuthenticatedUser());
			transactionService.persistTransaction(transaction);
			messages.add(new Message("success", "Transaction titled <strong>" + transaction.getTitle() + " </strong> has been successfully created.", "thumb-up"));
			
		} else {
			transactionService.updateTransaction(transaction);
			messages.add(new Message("success", "Transaction titled <strong>" + transaction.getTitle() + " </strong> been successfully updated.", "thumb-up"));
		}	
		
		redirectAttributes.addFlashAttribute("flashMessages", messages);
		return "redirect:/transactions";

	}
	
	@RequestMapping(value = "/transactions/delete/{id}", method = RequestMethod.GET)
	public String deleteTransaction(@PathVariable int id, final RedirectAttributes redirectAttributes) {
		Transaction transaction = transactionService.findById(id);
		transactionService.deleteTransaction(transaction);
		
		List<Message> messages = new ArrayList<Message>();
		messages.add(new Message("success", "Transaction titled <strong>" + transaction.getTitle() + "</strong> has been successfully removed.", "trash"));
		
		redirectAttributes.addFlashAttribute("flashMessages", messages);
		return "redirect:/transactions";
	}
}
