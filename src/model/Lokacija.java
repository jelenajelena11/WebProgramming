package model;

public class Lokacija {

	private Long width;
	private Long length;
	private Adresa adresa;
	
	public Lokacija() {
		// TODO Auto-generated constructor stub
	}

	public Lokacija(Long width, Long length, Adresa adresa) {
		super();
		this.width = width;
		this.length = length;
		this.adresa = adresa;
	}

	public Long getWidth() {
		return width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public Adresa getAdresa() {
		return adresa;
	}

	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
	}

	@Override
	public String toString() {
		return "Lokacija [width=" + width + ", length=" + length + ", adresa=" + adresa + "]";
	}
	
}
