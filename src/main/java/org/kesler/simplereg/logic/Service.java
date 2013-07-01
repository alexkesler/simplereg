package org.kesler.simplereg.logic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Proxy(lazy=false)
@Table(name="Services")
public class Service {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;

	@ManyToOne
	@JoinColumn(name="parentID")
	private Service parentService;

	@Column(name="Name", length=255)
	private String name;

	@Column(name="Enabled")
	private Boolean enabled;

	public Service() {} // for Hibernate

	public Service (Long id, Service parentService, String name, Boolean enabled) {
		this.id = id;
		this.parentService = parentService;
		this.name = name;
		this.enabled = enabled;
	}

	public Service(String name) {
		this.name = name;
		this.enabled = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Service getParentService() {
		return parentService;
	}

	public void setParentService(Service parentService) {
		this.parentService = parentService;
	}

	public String getParentName() {
		String name = "";
		if (parentService != null) {
			name = parentService.getName();
		}
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

}