package models;

import UsersManager.dal;

public class User {

	private int id;
	private String regId;

	public User(String regId) {
		this.regId = regId;
		this.id = dal.GenerateUserId();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

}
