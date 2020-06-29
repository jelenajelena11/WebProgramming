package dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import model.Adresa;
import model.Apartman;
import model.Lokacija;
import model.User;

public class UserDAO {

	private HashMap<String, User> users = new HashMap<String, User>();
	
	public UserDAO() {
		
		User admin = new User("Goku", "goran", "Goran", "Kuljanin", "goku@gmail.com", "Ugljevik", "M", 1);
		User jela = new User("Jela", "goran", "Jelena", "Stojanovic", "jela@gmail.com", "Novi Sad", "F", 2);
		User korisnik = new User("Korisnik", "goran", "Korisnik", "Korisnik", "korisnik@gmail.com", "Bijeljina", "F", 0);
		
		Lokacija l = new Lokacija(14L, 12L, new Adresa("Djordja Jovanovica", 14, "Novi Sad", 21000));
		Lokacija l2 = new Lokacija(15L, 12L, new Adresa("Bulevar Oslobodjenja", 14, "Novi Sad", 21000));
		Lokacija l3 = new Lokacija(16L, 14L, new Adresa("Ulica", 14, "Beograd", 23000));
		
		Apartman a = new Apartman(0, 2, 2, l, LocalDate.of(2020, 6, 28), LocalDate.of(2020, 7, 22), null, null, "./assets/img/rent.jpg", 50, 
						LocalTime.now(), LocalTime.now().plusHours(3), 0, null, null);
		Apartman a2 = new Apartman(0, 2, 2, l2, LocalDate.of(2020, 6, 11), LocalDate.of(2020, 8, 7), null, null, "./assets/img/rent.jpg", 50, 
				LocalTime.now(), LocalTime.now().plusHours(3), 0, null, null);
		
		jela.getApartmaniZaIznajmljivanje().add(a);
		jela.getApartmaniZaIznajmljivanje().add(a2);
		
		this.users.put("Goku", admin);
		this.users.put("Jela", jela);
		this.users.put("Korisnik", korisnik);
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
