package pl.pownug.marek.financeAnalyzer.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pl.pownug.marek.financeAnalyzer.helpers.UserDetailsImpl;

@Entity
@Table(name="users")
public class User  implements Comparable<User>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull @NotBlank
	@Column(unique = true)
	@Email
	private String email;
	
	@NotNull
	private String password = "";
	
	@OneToMany(
			cascade=CascadeType.ALL, 
			mappedBy="user", 
			fetch=FetchType.EAGER, 
			orphanRemoval=true
		)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<UserRole> roles = null;
	
	@NotNull @NotBlank
	@Size(min=2, max=30)
	private String firstName;
	
	@NotNull @NotBlank
	@Size(min=2, max=30)
	private String lastName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public boolean isNew() {
		return (this.id == null);
	}

	/////////////////////////////////////////////////////////////////
	// Helper properties
	/////////////////////////////////////////////////////////////////
	
	@Transient
	private Boolean isAdmin = null;
	
	@Transient
	private Boolean isUser = null;
	
	public boolean isAdmin() {
		if(isAdmin == null) {
			checkRoles();
		}		
		return isAdmin;
	}

	public boolean isUser() {
		if(isUser == null) {
			checkRoles();
		}		
		return isUser;
	}	
	
	public boolean isUser(User user) {
		return this.id != null && this.id == user.getId();
	}
	
	private void checkRoles() {
		this.isAdmin = false;
		this.isUser = false;
		
		if(getRoles() == null) {
			return;
		}
		
		for(UserRole role: getRoles()) {
			if(role.getRole().equals("ROLE_ADMIN")) {
				this.isAdmin = true;
			
			} else if(role.getRole().equals("ROLE_USER")) {
				this.isUser = true;
			}
		}
	}

	static public User getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
			return userDetails.getUser();
		} else {
			return null;
		}
	}
	
	
	@Override
	public int compareTo(User user) {
		int ret = this.getLastName().compareTo(user.getLastName());
		if(ret == 0) {
			ret = this.getFirstName().compareTo(user.getFirstName());
		}
		return ret;
	}

	@Override
	public String toString()
	{
		return this.getLastName() + " " + this.getFirstName();
	}
}
