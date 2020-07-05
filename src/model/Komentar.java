package model;

import java.util.UUID;

public class Komentar {

	private UUID id = UUID.randomUUID();
	private User gost;
	private UUID apartman;
	private String text;
	private int ocena;
	private boolean odobren;
	
	public Komentar() {
	}
	
	public Komentar(User gost, UUID apartman, String text, int ocena, boolean odobren) {
		super();
		this.id = UUID.randomUUID();
		this.gost = gost;
		this.apartman = apartman;
		this.text = text;
		this.ocena = ocena;
		this.odobren = odobren;
	}
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public User getGost() {
		return gost;
	}

	public void setGost(User gost) {
		this.gost = gost;
	}

	public UUID getApartman() {
		return apartman;
	}

	public void setApartman(UUID apartman) {
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

	
	public boolean isOdobren() {
		return odobren;
	}

	public void setOdobren(boolean odobren) {
		this.odobren = odobren;
	}

	@Override
	public String toString() {
		return "Komentar [id=" + id + ", gost=" + gost + ", apartman=" + apartman + ", text=" + text + ", ocena="
				+ ocena + ", odobren=" + odobren + "]";
	}

	
	
}
