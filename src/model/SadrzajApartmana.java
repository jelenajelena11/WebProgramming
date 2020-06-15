package model;

import java.util.UUID;

public class SadrzajApartmana {

	private UUID id;
	private boolean imaParking;
	private boolean imaKuhinja;
	private boolean imaPegla;
	private boolean imaVesMasina;
	
	public SadrzajApartmana() {
	}

	public SadrzajApartmana(UUID id, boolean imaParking, boolean imaKuhinja, boolean imaPegla, boolean imaVesMasina) {
		super();
		this.id = id;
		this.imaParking = imaParking;
		this.imaKuhinja = imaKuhinja;
		this.imaPegla = imaPegla;
		this.imaVesMasina = imaVesMasina;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public boolean isImaParking() {
		return imaParking;
	}

	public void setImaParking(boolean imaParking) {
		this.imaParking = imaParking;
	}

	public boolean isImaKuhinja() {
		return imaKuhinja;
	}

	public void setImaKuhinja(boolean imaKuhinja) {
		this.imaKuhinja = imaKuhinja;
	}

	public boolean isImaPegla() {
		return imaPegla;
	}

	public void setImaPegla(boolean imaPegla) {
		this.imaPegla = imaPegla;
	}

	public boolean isImaVesMasina() {
		return imaVesMasina;
	}

	public void setImaVesMasina(boolean imaVesMasina) {
		this.imaVesMasina = imaVesMasina;
	}

	@Override
	public String toString() {
		return "SadrzajApartmana [id=" + id + ", imaParking=" + imaParking + ", imaKuhinja=" + imaKuhinja
				+ ", imaPegla=" + imaPegla + ", imaVesMasina=" + imaVesMasina + "]";
	}
	
}
