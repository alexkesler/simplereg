package org.kesler.simplereg.dao;

import java.util.UUID;

import javax.persistence.MappedSuperclass;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

import org.hibernate.annotations.GenericGenerator;

/**
* Определяет свойства и методы для классов, сохраняемых в базу данных
* переопределяет equals и hashCode в зависимости от UUID
*/
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

	@Column(name="UUID",length=36)
	private String uuid = UUID.randomUUID().toString();
	
	private transient int state = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUUID() {
		return uuid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof AbstractEntity)) return false;

		AbstractEntity other = (AbstractEntity) o;

		if (uuid == null) return false;

		return uuid.equals(other.getUUID());
	} 

	@Override
	public int hashCode() {
		if (uuid != null) {
			return uuid.hashCode();
		} else {
			return super.hashCode();
		}
		
	}

}