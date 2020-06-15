package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Apartman {

	private UUID id;
	private int tipSobe; //0 - Ceo apartman; 1 - Soba
	private int brojSoba;
	private int brojGostiju;
	private Lokacija lokacija;
	private Date datePocetakVazenja;
	private Date krajPocetakVazenja;
	
	private User domacin;
	private List<Komentar> komentari = new ArrayList<Komentar>();
	private String slika;	//Putanja samo 1 slike
	private int cenaPoNoci;
	private Date vremeZaPrijavu;
	private Date vremeZaOdjavu;
	private int status;			//0 - Aktivan; 1 - Neaktivan
	
	private List<SadrzajApartmana> sadrzajApartmana = new ArrayList<SadrzajApartmana>();
	private List<Rezervacija> rezervacije = new ArrayList<Rezervacija>();
	
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

	

	public Date getDatePocetakVazenja() {
		return datePocetakVazenja;
	}

	public void setDatePocetakVazenja(Date datePocetakVazenja) {
		this.datePocetakVazenja = datePocetakVazenja;
	}

	public Date getKrajPocetakVazenja() {
		return krajPocetakVazenja;
	}

	public void setKrajPocetakVazenja(Date krajPocetakVazenja) {
		this.krajPocetakVazenja = krajPocetakVazenja;
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

	public Date getVremeZaPrijavu() {
		return vremeZaPrijavu;
	}

	public void setVremeZaPrijavu(Date vremeZaPrijavu) {
		this.vremeZaPrijavu = vremeZaPrijavu;
	}

	public Date getVremeZaOdjavu() {
		return vremeZaOdjavu;
	}

	public void setVremeZaOdjavu(Date vremeZaOdjavu) {
		this.vremeZaOdjavu = vremeZaOdjavu;
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
