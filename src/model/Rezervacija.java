package model;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Rezervacija {

	private UUID id;
	private UUID apartman;
	private String pocetakIznajmljivanja;
	private int brojNocenja = 1;
	private int ukupnaCena;
	private String poruka;
	private User gost;
	private int status; //0 - Kreirana; 1 - Odbijena; 
						//2 - Odustanak; 3 - Prihvacena
						//4 - Zavrsena
	
	public Rezervacija() {
	
	}
	
	public Rezervacija(UUID apartman, String pocetakIznajmljivanja, int brojNocenja,
			int ukupnaCena, String poruka, User gost, int status) {
		super();
		this.id = UUID.randomUUID();
		this.apartman = apartman;
		this.pocetakIznajmljivanja = pocetakIznajmljivanja;
		this.brojNocenja = brojNocenja;
		this.ukupnaCena = ukupnaCena;
		this.poruka = poruka;
		this.gost = gost;
		this.status = status;
	}
	
	public Rezervacija(UUID id, UUID apartman, String pocetakIznajmljivanja, int brojNocenja,
			int ukupnaCena, String poruka, User gost, int status) {
		super();
		this.id = id;
		this.apartman = apartman;
		this.pocetakIznajmljivanja = pocetakIznajmljivanja;
		this.brojNocenja = brojNocenja;
		this.ukupnaCena = ukupnaCena;
		this.poruka = poruka;
		this.gost = gost;
		this.status = status;
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UUID getApartman() {
		return apartman;
	}
	public void setApartman(UUID apartman) {
		this.apartman = apartman;
	}
	public String getPocetakIznajmljivanja() {
		return pocetakIznajmljivanja;
	}
	public void setPocetakIznajmljivanja(String pocetakIznajmljivanja) {
		this.pocetakIznajmljivanja = pocetakIznajmljivanja;	
	}
	public int getBrojNocenja() {
		return brojNocenja;
	}
	public void setBrojNocenja(int brojNocenja) {
		this.brojNocenja = brojNocenja;
	}
	public int getUkupnaCena() {
		return ukupnaCena;
	}
	public void setUkupnaCena(int ukupnaCena) {
		this.ukupnaCena = ukupnaCena;
	}
	public String getPoruka() {
		return poruka;
	}
	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}
	public User getGost() {
		return gost;
	}
	public void setGost(User gost) {
		this.gost = gost;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Rezervacija [id=" + id + ", apartman=" + apartman + ", pocetakIznajmljivanja=" + pocetakIznajmljivanja
				+ ", brojNocenja=" + brojNocenja + ", ukupnaCena=" + ukupnaCena + ", poruka=" + poruka + ", gost="
				+ gost + ", status=" + status + "]";
	}
	
}
