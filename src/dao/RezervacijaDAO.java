package dao;

import java.util.HashMap;
import java.util.UUID;

import model.Rezervacija;

public class RezervacijaDAO {

	private HashMap<UUID, Rezervacija> rezervacije = new HashMap<UUID, Rezervacija>();
	
	public RezervacijaDAO() {
	
	}

	public HashMap<UUID, Rezervacija> getRezervacije() {
		return rezervacije;
	}

	public void setRezervacije(HashMap<UUID, Rezervacija> rezervacije) {
		this.rezervacije = rezervacije;
	}

	
	
}
