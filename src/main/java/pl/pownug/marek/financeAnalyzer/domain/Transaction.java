package pl.pownug.marek.financeAnalyzer.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="transaction")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date date;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	private Category category;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	private Account account;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	private User user;
	
	@NotNull
	private Float amount;
	
	@NotNull
	private Integer type;
	
	@Size(min=3, max=30)
	private String title;
	
	private String comment;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}
	
	public String getFormattedDate() {
	    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	    if(date == null)
	    	return "";
	    else
	    	return df.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isNew() {
		return (this.id == null);
	}
}
