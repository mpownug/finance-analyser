package pl.pownug.marek.financeAnalyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.helpers.UserDetailsImpl;
import pl.pownug.marek.financeAnalyzer.repository.UsersRepositiry;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsersRepositiry usersRepo;	
	
	@Transactional(readOnly=true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = usersRepo.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return new UserDetailsImpl(user);
	}
	
	public void signin(User user) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(user));
	}
	
	private Authentication authenticate(User user) {	
		UserDetails userDetails = new UserDetailsImpl(user);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());		
	}
}
