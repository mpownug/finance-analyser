package pl.pownug.marek.financeAnalyzer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.helpers.Message;
import pl.pownug.marek.financeAnalyzer.service.UsersService;

@Controller
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin(origins = "http://localhost:8080")
public class ProfileController {

	@Autowired
	UsersService usersService;
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public @ResponseBody User profile()
	{
		User user = User.getAuthenticatedUser();
		return user;
	}
	
//	@RequestMapping(value = "/profile/changepassword", method = RequestMethod.GET)
//	public String changePassword()
//	{return "profile/change-password";}
	
	@RequestMapping(value = "/profile/changepassword", method = RequestMethod.POST)
	public String changePassword(final RedirectAttributes redirectAttributes,
								 @RequestParam String currentPassword,
								 @RequestParam String newPassword,
			   					 @RequestParam String repeatPassword)
	{	
		List<Message> messages = new ArrayList<Message>();
		
		User user = User.getAuthenticatedUser();
		String redirect = "redirect:/profile/changepassword";
		
		if(!newPassword.equals(repeatPassword)) {
			messages.add(new Message("danger", "The passwords do not match.", "remove-sign"));
		
		} else if(newPassword.isEmpty() || newPassword.length() < 6) {
			messages.add(new Message("danger", "New password may not be empty and must conatin at least 6 characters.", "remove-sign"));
			
		} else if(!usersService.checkPassword(user, currentPassword)) {
			messages.add(new Message("danger", "The current password is incorrect.", "remove-sign"));
			
		} else {
			user.setPassword(usersService.encodePassword(newPassword));
			usersService.updateUser(user);
			
			redirect = "redirect:/profile";
			messages.add(new Message("success", "Your password has been changed.", "thumbs-up"));
		}
		
		redirectAttributes.addFlashAttribute("flashMessages", messages);
		return redirect;
	}
}
