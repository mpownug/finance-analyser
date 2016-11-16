package pl.pownug.marek.financeAnalyzer.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.domain.UserRole;
import pl.pownug.marek.financeAnalyzer.helpers.Message;
import pl.pownug.marek.financeAnalyzer.service.UsersService;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UsersController {
	
	@Autowired
	UsersService usersService;	
	
	@RequestMapping(value = "/users",  method = RequestMethod.GET)
	public String usersList(ModelMap model) 
	{
		Set<User> users = usersService.findAllUsers();
		model.addAttribute("users", users);
		return "users/list";
	}
	
	@RequestMapping(value = "/users/new")
	public String newUser(ModelMap model)
	{			
		model.addAttribute("user", new User());
		return "users/editor";
	}
	
	@RequestMapping(value = "/users/edit/{id}", method = RequestMethod.GET)
	public String editUser(@PathVariable int id, ModelMap model)
	{
		User user = usersService.findById(id);
		model.addAttribute("user", user);
		return "users/editor";
	}
	
	@RequestMapping(value = "/users/save", method = RequestMethod.POST)
	public String saveUser(@RequestParam(value="apply", required=false, defaultValue="false") boolean isApply,
						   @RequestParam(required=false, defaultValue="false") boolean isModerator,
						   @Valid @ModelAttribute User user,
						   BindingResult result, 
						   ModelMap model,
						   final RedirectAttributes redirectAttributes)
	{
		
		List<Message> messages = new ArrayList<Message>();
		
		if(!user.isNew()) {
			usersService.loadRoles(user);
		}
		
		String password = user.getPassword();
		if(!password.isEmpty()) {
			if(password.length() >= 6) {
				user.setPassword(usersService.encodePassword(password));
				
			} else {
				result.addError(new FieldError("user", "password", password, true, null, null, "password must contain at least 6 characters"));
			}
		}
		
		if(!user.isAdmin()) {
			user.setRoles(new HashSet<UserRole>());
			user.getRoles().add(new UserRole(user, "ROLE_USER"));
		}
		
		if(usersService.isMailDuplicated(user)) {
			result.addError(new FieldError("user", "email", user.getEmail(), true, null, null, "email address is already used"));
		}
		
		if(result.hasErrors()) {
			model.addAttribute("user", user);
			return "users/editor";
		}
		
		if(user.isNew()) {	
			usersService.persistUser(user);
			messages.add(new Message("success", "User <strong>" + user.getFirstName() + " " + user.getLastName() + "</strong> has been successfully created.", "thumbs-up"));
			
		} else {
			usersService.updateUser(user);
			messages.add(new Message("success", "User <strong>" + user.getFirstName() + " " + user.getLastName() + "</strong> has been successfully updated.", "thumbs-up"));
		}	
		redirectAttributes.addFlashAttribute("flashMessages", messages);
		if(!isApply) {
			return "redirect:/users";
		} else {
			return "redirect:/users/edit/" + user.getId();
		}

	}
}
