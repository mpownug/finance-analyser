package pl.pownug.marek.financeAnalyzer.helpers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.domain.UserRole;

public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {
	
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	
	private User user = null;
	
	public UserDetailsImpl(
			User user,
			boolean enabled, 
			boolean accountNonExpired, 
			boolean credentialsNonExpired, 
			boolean accountNonLocked)
	{
		super(user.getEmail(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities(user));
		this.user = user;
	}
	
	public UserDetailsImpl(User user) 
	{
		this(user, true, true, true, true);
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	static private Set<GrantedAuthority> getAuthorities(User user) 
	{
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for(UserRole role: user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		return authorities;
	}

}
