package org.kesler;

public class Service {
	private long id;
	private long parentId;
	private String name;
	private boolean enabled;

	public Service (long id, long parentId, String name, boolean enabled) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.enabled = enabled;
	}

	public Service(String name) {
		this(0,0,name,true);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}