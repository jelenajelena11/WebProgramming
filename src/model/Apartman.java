package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Apartman implements Serializable{

	private UUID id;
	private int tipSobe; //0 - Ceo apartman; 1 - Soba
	private int brojSoba;
	private int brojGostiju;
	private Lokacija lokacija;
	private String datePocetakVazenja;
	private String krajPocetakVazenja;
	
	private User domacin;
	private List<Komentar> komentari = new ArrayList<Komentar>();
	private String slika;	//Putanja samo 1 slike
	private int cenaPoNoci;
	private String vremeZaPrijavu;
	private String vremeZaOdjavu;
	private int status;			//0 - Aktivan; 1 - Neaktivan
	
	private List<SadrzajApartmana> sadrzajApartmana = new ArrayList<SadrzajApartmana>();
	private List<Rezervacija> rezervacije = new ArrayList<Rezervacija>();
	
	public Apartman(int tipSobe, int brojSoba, int brojGostiju, Lokacija lokacija,
			String datePocetakVazenja, String krajPocetakVazenja, User domacin, List<Komentar> komentari,
			String slika, int cenaPoNoci, String vremeZaPrijavu, String vremeZaOdjavu, int status,
			List<SadrzajApartmana> sadrzajApartmana, List<Rezervacija> rezervacije) {
		super();
		this.id = UUID.randomUUID();
		this.tipSobe = tipSobe;
		this.brojSoba = brojSoba;
		this.brojGostiju = brojGostiju;
		this.lokacija = lokacija;
		this.datePocetakVazenja = datePocetakVazenja;
		this.krajPocetakVazenja = krajPocetakVazenja;
		this.domacin = domacin;
		this.komentari = komentari;
		this.slika = slika;
		this.cenaPoNoci = cenaPoNoci;
		this.vremeZaPrijavu = vremeZaPrijavu;
		this.vremeZaOdjavu = vremeZaOdjavu;
		this.status = status;
		this.sadrzajApartmana = sadrzajApartmana;
		this.rezervacije = rezervacije;
	}
	
	public Apartman(UUID id, int tipSobe, int brojSoba, int brojGostiju, Lokacija lokacija,
			String datePocetakVazenja, String krajPocetakVazenja, User domacin, List<Komentar> komentari,
			String slika, int cenaPoNoci, String vremeZaPrijavu, String vremeZaOdjavu, int status,
			List<SadrzajApartmana> sadrzajApartmana, List<Rezervacija> rezervacije) {
		super();
		this.id = id;
		this.tipSobe = tipSobe;
		this.brojSoba = brojSoba;
		this.brojGostiju = brojGostiju;
		this.lokacija = lokacija;
		this.datePocetakVazenja = datePocetakVazenja;
		this.krajPocetakVazenja = krajPocetakVazenja;
		this.domacin = domacin;
		this.komentari = komentari;
		this.slika = slika;
		this.cenaPoNoci = cenaPoNoci;
		this.vremeZaPrijavu = vremeZaPrijavu;
		this.vremeZaOdjavu = vremeZaOdjavu;
		this.status = status;
		this.sadrzajApartmana = sadrzajApartmana;
		this.rezervacije = rezervacije;
	}
	
	public Apartman(Apartman a) {
		this.id = a.getId();
		this.tipSobe = a.getTipSobe();
		this.brojSoba = a.getBrojSoba();
		this.datePocetakVazenja = a.getDatePocetakVazenja();
		this.krajPocetakVazenja = a.getKrajPocetakVazenja();
		this.slika = a.getSlika();
		this.cenaPoNoci = a.getCenaPoNoci();
		this.status = a.getStatus();
		this.vremeZaPrijavu = a.getVremeZaPrijavu();
		this.vremeZaOdjavu = a.getVremeZaOdjavu();
		this.lokacija = a.getLokacija();
		//this.rezervacije = a.getRezervacije();
	}

	public Apartman() {
		this.id = UUID.randomUUID();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getTipSobe() {
		return tipSobe;
	}

	public void setTipSobe(int tipSobe) {
		this.tipSobe = tipSobe;
	}

	public int getBrojSoba() {
		return brojSoba;
	}

	public void setBrojSoba(int brojSoba) {
		this.brojSoba = brojSoba;
	}

	public int getBrojGostiju() {
		return brojGostiju;
	}

	public void setBrojGostiju(int brojGostiju) {
		this.brojGostiju = brojGostiju;
	}

	public Lokacija getLokacija() {
		return lokacija;
	}

	public void setLokacija(Lokacija lokacija) {
		this.lokacija = lokacija;
	}

	public String getDatePocetakVazenja() {
		return datePocetakVazenja;
	}

	public void setDatePocetakVazenja(String datePocetakVazenja) {
		this.datePocetakVazenja = datePocetakVazenja;
	}

	public String getKrajPocetakVazenja() {
		return krajPocetakVazenja;
	}

	public void setKrajPocetakVazenja(String krajPocetakVazenja) {
		this.krajPocetakVazenja = krajPocetakVazenja;
	}

	public String getVremeZaPrijavu() {
		return vremeZaPrijavu;
	}

	public void setVremeZaPrijavu(String vremeZaPrijavu) {
		this.vremeZaPrijavu = vremeZaPrijavu;
	}

	public String getVremeZaOdjavu() {
		return vremeZaOdjavu;
	}

	public void setVremeZaOdjavu(String vremeZaOdjavu) {
		this.vremeZaOdjavu = vremeZaOdjavu;
	}

	public User getDomacin() {
		return domacin;
	}

	public void setDomacin(User domacin) {
		this.domacin = domacin;
	}

	public List<Komentar> getKomentari() {
		return komentari;
	}

	public void setKomentari(List<Komentar> komentari) {
		this.komentari = komentari;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public int getCenaPoNoci() {
		return cenaPoNoci;
	}

	public void setCenaPoNoci(int cenaPoNoci) {
		this.cenaPoNoci = cenaPoNoci;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<SadrzajApartmana> getSadrzajApartmana() {
		return sadrzajApartmana;
	}

	public void setSadrzajApartmana(List<SadrzajApartmana> sadrzajApartmana) {
		this.sadrzajApartmana = sadrzajApartmana;
	}

	public List<Rezervacija> getRezervacije() {
		return rezervacije;
	}

	public void setRezervacije(List<Rezervacija> rezervacije) {
		this.rezervacije = rezervacije;
	}

	@Override
	public String toString() {
		return "Apartman [id=" + id + ", tipSobe=" + tipSobe + ", brojSoba=" + brojSoba + ", brojGostiju=" + brojGostiju
				+ ", lokacija=" + lokacija + ", datePocetakVazenja=" + datePocetakVazenja + ", krajPocetakVazenja="
				+ krajPocetakVazenja + ", domacin=" + domacin + ", komentari=" + komentari + ", slika=" + slika
				+ ", cenaPoNoci=" + cenaPoNoci + ", vremeZaPrijavu=" + vremeZaPrijavu + ", vremeZaOdjavu="
				+ vremeZaOdjavu + ", status=" + status + ", sadrzajApartmana=" + sadrzajApartmana + ", rezervacije="
				+ rezervacije + "]";
	}

	
	
}
