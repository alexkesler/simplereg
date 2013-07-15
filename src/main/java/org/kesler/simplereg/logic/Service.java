package org.kesler.simplereg.logic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.dao.AbstractEntity;


@Entity
@Proxy(lazy=false)
@Table(name="Services")
public class Service extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name="parentID")
	private Service parentService;

	@Column(name="Name", length=255)
	private String name;

	@Column(name="Enabled")
	private Boolean enabled;

	public Service() {} // for Hibernate

	public Service (Service parentService, String name, Boolean enabled) {
		this.parentService = parentService;
		this.name = name;
		this.enabled = enabled;
	}

	public Service(String name) {
		this.name = name;
		this.enabled = true;
	}

	public Service getParentService() {
		return parentService;
	}

	public void setParentService(Service parentService) {
		this.parentService = parentService;
	}

	public String getParentServiceName() {
		String name = "Родительская услуга не определена";
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Service)) {
			return false;
		}

		Service service = (Service)obj;
		if (id == service.getId() &&
			name == service.getName() &&
			parentService.getId() == service.getParentService() &&
			enabled == service.getEnabled()) {
			return true;
		} else {
			return false;
		}
	}

}