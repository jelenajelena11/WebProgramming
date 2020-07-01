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
		UUID id3 = UUID.randomUUID();
		
		User jela = new User("Jela", "goran", "Jelena", "Stojanovic", "jela@gmail.com", "Novi Sad", "F", 2);
		
		Lokacija l = new Lokacija(14L, 12L, new Adresa("Djordja Jovanovica", 14, "Novi Sad", 21000));
		Lokacija l2 = new Lokacija(15L, 12L, new Adresa("Bulevar Oslobodjenja", 14, "Novi Sad", 21000));
		Lokacija l3 = new Lokacija(15L, 12L, new Adresa("Vuka Karadzica", 14, "Bijeljina", 21000));
		
		Apartman a = new Apartman(id1, 0, 2, 2, l, "2020-06-28", "2020-07-22", jela, null, "./assets/img/rent.jpg", 50, 
				"14:00", "10:00", 0, null, null);
		Apartman a2 = new Apartman(id2, 0, 2, 2, l2, "2020-06-11", "2020-08-07", jela, null, "./assets/img/rent.jpg", 50, 
				"15:00", "9:00", 0, null, null);
		Apartman a3 = new Apartman(id3, 0, 2, 2, l3, "2020-06-21", "2020-08-17", jela, null, "./assets/img/rent.jpg", 50, 
				"12:00", "10:00", 0, null, null);
		
		this.apartmani.put(id1, a);
		this.apartmani.put(id2, a2);
		this.apartmani.put(id3, a3);
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
