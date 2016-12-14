package pl.pownug.marek.financeAnalyzer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.pownug.marek.financeAnalyzer.domain.Account;
import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.helpers.Message;
import pl.pownug.marek.financeAnalyzer.service.AccountService;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
public class AccountController {

	@Autowired
	AccountService accountService;

	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public @ResponseBody List<Account> accounts() {
		List<Account> accounts = accountService.findUserAccounts(User.getAuthenticatedUser());
		return accounts;
	}

	@RequestMapping(value = "/accounts/add", method = RequestMethod.GET)
	public @ResponseBody Account newAccount() {
		return new Account();
	}

	@RequestMapping(value = "/accounts/edit/{id}", method = RequestMethod.GET)
	public @ResponseBody Account editAccount(@PathVariable int id) {
		Account account = accountService.findById(id);
		return account;
	}
	
	@RequestMapping(value = "/accounts/save", method = RequestMethod.POST)
	public String saveAccount(@Valid @ModelAttribute Account account,
			BindingResult result,
			ModelMap model, 
			final RedirectAttributes redirectAttributes) {
		
		List<Message> messages = new ArrayList<Message>();
		
		if(result.hasErrors()) {
			model.addAttribute("account", account);
            return "accounts/editor";
        }
		
		if(account.isNew()) {
			account.setUser(User.getAuthenticatedUser());
			accountService.persistCategory(account);
			messages.add(new Message("success", "Category named <strong>" + account.getName() + "</strong> has been successfully created.", "thumb-up"));
			
		} else {
			accountService.updateCategory(account);
			messages.add(new Message("success", "Category named <strong>" + account.getName() + "</strong> has been successfully updated.", "thumb-up"));
		}	
		redirectAttributes.addFlashAttribute("flashMessages", messages);
		return "redirect:/accounts";
	}
	
	@RequestMapping(value = "/accounts/delete/{id}", method = RequestMethod.GET)
	public @ResponseBody Account deleteAccount(@PathVariable int id) {
		Account account = accountService.findById(id);
		accountService.deleteAccount(account);

		return account;
	}
}
