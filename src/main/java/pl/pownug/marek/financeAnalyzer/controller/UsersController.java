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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.domain.UserRole;
import pl.pownug.marek.financeAnalyzer.helpers.Message;
import pl.pownug.marek.financeAnalyzer.service.UsersService;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@CrossOrigin(origins = "http://localhost:8080")
public class UsersController {
	
	@Autowired
	UsersService usersService;	
	
	@RequestMapping(value = "/users",  method = RequestMethod.GET)
	public @ResponseBody Set<User> usersList()
	{
		Set<User> users = usersService.findAllUsers();

		return users;
	}
	
	@RequestMapping(value = "/users/new")
	public @ResponseBody User newUser()
		{
			return new User();}
	
	@RequestMapping(value = "/users/edit/{id}", method = RequestMethod.GET)
	public @ResponseBody User editUser(@PathVariable int id)
	{
		User user = usersService.findById(id);
		return user;
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
