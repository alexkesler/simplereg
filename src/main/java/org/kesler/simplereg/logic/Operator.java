package org.kesler.simplereg.logic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.dao.AbstractEntity;

@Entity
@Proxy(lazy=false)
@Table(name="Operators")
public class Operator extends AbstractEntity{

	@Column(name="FIO", length=255)
	private String fio;

	@Column(name="FIOShort", length=255)
	private String fioShort;

	@Column(name="Password", length=50)
	private String password;

	@Column(name="IsControler")
	private boolean isControler;

	@Column(name="IsAdmin")
	private boolean isAdmin;

	@Column(name="Enabled")
	private boolean enabled;

	public Operator() {
		// for Hibernate
	}

	public Operator(String fio, 
					String fioShort, 
					String password,
					boolean isControler,
					boolean isAdmin,
					boolean enabled) { 
		this.fio = fio;
		this.fioShort = fioShort;
		this.password = password;
		this.isControler = isControler;
		this.isAdmin = isAdmin;
		this.enabled = enabled;
	}

	public Operator(String fio) {
		this(fio,fio,"",false,false,true);
	}

	public String getFIO() {
		return fio;
	}

	public void setFIO(String fio) {
		this.fio = fio;
	}

	public String getFIOShort() {
		return fioShort;
	}

	public void setFIOShort(String fioShort) {
		this.fioShort = fioShort;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getIsControler() {
		return isControler;
	}

	public void setIsControler(boolean isControler) {
		this.isControler = isControler;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}