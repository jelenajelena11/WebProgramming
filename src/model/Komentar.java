package model;

public class Komentar {

	private User gost;
	private Apartman apartman;
	private String text;
	private int ocena;
	
	public Komentar() {
	}
	
	public Komentar(User gost, Apartman apartman, String text, int ocena) {
		super();
		this.gost = gost;
		this.apartman = apartman;
		this.text = text;
		this.ocena = ocena;
	}

	public User getGost() {
		return gost;
	}

	public void setGost(User gost) {
		this.gost = gost;
	}

	public Apartman getApartman() {
		return apartman;
	}

	public void setApartman(Apartman apartman) {
		this.apartman = apartman;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getOcena() {
		return ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

	@Override
	public String toString() {
		return "Komentar [gost=" + gost + ", apartman=" + apartman + ", text=" + text + ", ocena=" + ocena + "]";
	}
	
	
}
