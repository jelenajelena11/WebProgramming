package dao;

import java.util.HashMap;

import model.User;

public class UserDAO {

	private HashMap<String, User> users = new HashMap<String, User>();
	
	public UserDAO() {
		this.users.put("Goku", new User("Goku", "goran", "Goran", "Kuljanin", "goku@gmail.com", "Ugljevik", "M", 1));
		this.users.put("Jela", new User("Jela", "goran", "Jelena", "Stojanovic", "jela@gmail.com", "Novi Sad", "F", 2));
		this.users.put("Korisnik", new User("Korisnik", "goran", "Korisnik", "Korisnik", "korisnik@gmail.com", "Bijeljina", "F", 0));
	}
	
	public HashMap<String, User> getUsers(){
		return this.users;
	}
	
	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}

	public User findUser(User user) {
		for(User u : this.users.values()) {
			if(u.getUserName().trim().equals(user.getUserName().trim())) {
				if(u.getPassword().trim().equals(user.getPassword().trim())) {
					return u;
				}else {
					return null;
				}
			}
		}
		return null;
	}
	
	public boolean find(String username) {
		if (users.containsKey(username)) {
			return true;
		}
		return false;
	}
}
