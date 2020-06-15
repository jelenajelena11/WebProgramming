package dao;

import java.util.HashMap;

import model.Rezervacija;

public class RezervacijaDAO {

	private HashMap<String, Rezervacija> users = new HashMap<String, Rezervacija>();
	
	public RezervacijaDAO() {
	
	}

	public HashMap<String, Rezervacija> getUsers() {
		return users;
	}

	public void setUsers(HashMap<String, Rezervacija> users) {
		this.users = users;
	}
	
}
