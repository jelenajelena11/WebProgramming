package dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import model.Adresa;
import model.Apartman;
import model.Lokacija;
import model.Rezervacija;
import model.User;

public class ApartmanDAO {

	private HashMap<UUID, Apartman> apartmani = new HashMap<UUID, Apartman>();
	
	public ApartmanDAO() {
		
		UUID id1 = UUID.randomUUID();
		UUID id2 = UUID.randomUUID();
		
		User jela = new User("Jela", "goran", "Jelena", "Stojanovic", "jela@gmail.com", "Novi Sad", "F", 2);
		
		Lokacija l = new Lokacija(14L, 12L, new Adresa("Djordja Jovanovica", 14, "Novi Sad", 21000));
		Lokacija l2 = new Lokacija(15L, 12L, new Adresa("Bulevar Oslobodjenja", 14, "Novi Sad", 21000));
		
		List<Rezervacija> r1 = new ArrayList<Rezervacija>();
		List<Rezervacija> r2 = new ArrayList<Rezervacija>();
		
		Apartman a = new Apartman(id1, 0, 2, 2, l, LocalDate.of(2020, 6, 28), LocalDate.of(2020, 7, 22), jela, null, "./assets/img/rent.jpg", 50, 
						LocalTime.now(), LocalTime.now().plusHours(3), 0, null, null);
		Apartman a2 = new Apartman(id2, 0, 2, 2, l2, LocalDate.of(2020, 6, 11), LocalDate.of(2020, 8, 7), jela, null, "./assets/img/rent.jpg", 50, 
				LocalTime.now(), LocalTime.now().plusHours(3), 0, null, null);
		
		this.apartmani.put(id1, a);
		this.apartmani.put(id2, a2);
	}

	public HashMap<UUID, Apartman> getApartmani() {
		return apartmani;
	}

	public void setApartmani(HashMap<UUID, Apartman> apartmani) {
		this.apartmani = apartmani;
	}

	public Apartman findOneApartman(UUID id) {
		return this.apartmani.get(id);
		
	}
	
	
	
}
