package org.kesler.simplereg.dao;

import javax.persistence.MappedSuperclass;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
//import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


@MappedSuperclass
public abstract class AbstractEntity {
	
	public static final int SAVED_STATE = 0;
	public static final int NEW_STATE = 1;
	public static final int EDITED_STATE = 2;
	public static final int DELETED_STATE = 3;

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	@Column(name="ID")
	private Long id;
	
	private transient int state = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}