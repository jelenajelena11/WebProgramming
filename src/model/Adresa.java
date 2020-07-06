package model;

import java.util.UUID;

public class Adresa {
	
	private UUID id;
	private String ulica;
	private int broj = 0;
	private String mesto;
	private int postanskiBroj = 0;
	
	public Adresa() {
		this.id = UUID.randomUUID();
	}

	public Adresa(String ulica, int broj, String mesto, int postanskiBroj) {
		super();
		this.id = UUID.randomUUID();
		this.ulica = ulica;
		this.broj = broj;
		this.mesto = mesto;
		this.postanskiBroj = postanskiBroj;
	}
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUlica() {
		return ulica;
	}

	public void setUlica(String ulica) {
		this.ulica = ulica;
	}

	public int getBroj() {
		return broj;
	}

	public void setBroj(int broj) {
		this.broj = broj;
	}

	public String getMesto() {
		return mesto;
	}

	public void setMesto(String mesto) {
		this.mesto = mesto;
	}

	public int getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(int postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}

	@Override
	public String toString() {
		return "Adresa [id=" + id + ", ulica=" + ulica + ", broj=" + broj + ", mesto=" + mesto + ", postanskiBroj="
				+ postanskiBroj + "]";
	}
	
	
}
