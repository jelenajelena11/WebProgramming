package dao;

import java.util.HashMap;

import model.Apartman;

public class ApartmanDAO {

	private HashMap<String, Apartman> users = new HashMap<String, Apartman>();
	
	public ApartmanDAO() {
		
	}

	public HashMap<String, Apartman> getUsers() {
		return users;
	}

	public void setUsers(HashMap<String, Apartman> users) {
		this.users = users;
	}
	
	
}
