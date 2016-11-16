package pl.pownug.marek.financeAnalyzer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.pownug.marek.financeAnalyzer.helpers.Message;

@Controller
public class SecurityController 
{	
	@RequestMapping(value = "/login")
	public String login(ModelMap model, HttpServletRequest request) 
	{
		List<Message> messages = new ArrayList<Message>();
		Map<String, String[]> params = request.getParameterMap();
		System.out.println(params.toString());
		if(params.containsKey("error")) {
			messages.add(new Message("danger", "Log in error. Please try again.", "alert"));
			
		} else if(params.containsKey("logout")) {
			messages.add(new Message("success", "You have successfully logged out.", "thumbs-up"));
		}
		
		model.addAttribute("flashMessages", messages);
        return "login/login";
    }
	
	// for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accesssDenied() {
		return "403";
	}
}
