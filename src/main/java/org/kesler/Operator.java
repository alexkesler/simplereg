package org.kesler;

public class Operator {
	private long id;
	private String fio;
	private String fioShort;
	private boolean enabled;
	private String password;
	private boolean isKontroler;
	private boolean isAdmin;

	public Operator(long id, 
					String fio, 
					String fioShort, 
					boolean enabled, 
					String password,
					boolean isKontroler,
					boolean isAdmin) {
		this.id = id;
		this.fio = fio;
		this.fioShort = fioShort;
		this.enabled = enabled;
		this.password = password;
		this.isKontroler = isKontroler;
		this.isAdmin = isAdmin;
	}

	public Operator(String fio) {
		this(0,fio,fio,true,"",false,false);
	}

	public long getId() {
		return id;
	}

	public String getFIO() {
		return fio;
	}

	public String getFIOShort() {
		return fioShort;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public String getPassword() {
		return password;
	}

	public boolean getIsKontroler() {
		return isKontroler;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}
}