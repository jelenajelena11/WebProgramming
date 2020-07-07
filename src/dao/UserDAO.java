package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import model.Adresa;
import model.Apartman;
import model.Lokacija;
import model.User;

public class UserDAO {

	private HashMap<String, User> users = new HashMap<String, User>();
	private String path;
	
	public UserDAO(String path) {
		this.path = path;
		this.loadUsers("");
		
//		User admin = new User("Goku", "goran", "Goran", "Kuljanin", "goku@gmail.com", "Ugljevik", "M", 1);
//		User jela = new User("Jela", "goran", "Jelena", "Stojanovic", "jela@gmail.com", "Novi Sad", "F", 2);
//		User korisnik = new User("Korisnik", "goran", "Korisnik", "Korisnik", "korisnik@gmail.com", "Bijeljina", "F", 0);
		
//		Lokacija l = new Lokacija(14L, 12L, new Adresa("Djordja Jovanovica", 14, "Novi Sad", 21000));
//		Lokacija l2 = new Lokacija(15L, 12L, new Adresa("Bulevar Oslobodjenja", 14, "Novi Sad", 21000));
//		Lokacija l3 = new Lokacija(16L, 14L, new Adresa("Ulica", 14, "Beograd", 23000));
//		
//		Apartman a = new Apartman(0, 2, 2, l, "2020-06-28", "2020-07-22", null, null, "./assets/img/rent.jpg", 50, 
//						"14:00", "10:00", 0, null, null);
//		Apartman a2 = new Apartman(0, 2, 2, l2, "2020-06-11", "2020-08-07", null, null, "./assets/img/rent.jpg", 50, 
//				"15:00", "9:00", 0, null, null);
		
//		jela.getApartmaniZaIznajmljivanje().add(UUID.randomUUID());
//		jela.getApartmaniZaIznajmljivanje().add(UUID.randomUUID());
//		jela.getApartmaniZaIznajmljivanje().add(UUID.randomUUID());
//		
//		this.users.put("Goku", admin);
//		this.users.put("Jela", jela);
//		this.users.put("Korisnik", korisnik);
		
		//this.saveUsers("");
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
	
	public User findUser(UUID id) {
		for(User u : this.users.values()) {
			if(u.getId().equals(id)) {
				return u;
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
	
	public void saveUsers(String contextPath) {
		File f = new File(this.path + "\\data\\user.txt");
		FileWriter fileWriter = null;
		try {
			//f.createNewFile();
			fileWriter = new FileWriter(f);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String sUsers = objectMapper.writeValueAsString(users);
			fileWriter.write(sUsers);
			fileWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void loadUsers(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(this.path + "\\data\\user.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibilityChecker(
					VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, String.class, User.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			users = ((HashMap<String, User>) objectMapper.readValue(file, type));
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringRestaurants = objectMapper.writeValueAsString(users);
				fileWriter.write(stringRestaurants);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
