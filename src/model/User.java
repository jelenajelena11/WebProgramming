package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User{
	
	private UUID id;
	
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String gender;
	
	private int uloga = 0; // 0 - Korisnik; 1 - Admin; 2 - Domacin
	
	private List<UUID> apartmaniZaIznajmljivanje = new ArrayList<UUID>();	//Domacin
	private List<Rezervacija> zakazaneRezervacije = new ArrayList<Rezervacija>();	//Gost
	
	private List<Apartman> iznajmljeniApartmani = new ArrayList<Apartman>();	//Gost
	
	public User(){
		this.id = UUID.randomUUID();
	}
	
	public User(String userName, String password, String firstName, String lastName, String email, String address, String gender) {
		
		super();
		this.id = UUID.randomUUID();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
		this.gender = gender;
		
	}
	
	public User(String userName, String password, String firstName, String lastName, String email, String address, String gender, int uloga) {
		
		super();
		this.id = UUID.randomUUID();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
		this.gender = gender;
		this.uloga = uloga;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getUloga() {
		return uloga;
	}

	public void setUloga(int uloga) {
		this.uloga = uloga;
	}

	public List<UUID> getApartmaniZaIznajmljivanje() {
		return apartmaniZaIznajmljivanje;
	}

	public void setApartmaniZaIznajmljivanje(List<UUID> apartmaniZaIznajmljivanje) {
		this.apartmaniZaIznajmljivanje = apartmaniZaIznajmljivanje;
	}

	public List<Rezervacija> getZakazaneRezervacije() {
		return zakazaneRezervacije;
	}

	public void setZakazaneRezervacije(List<Rezervacija> zakazaneRezervacije) {
		this.zakazaneRezervacije = zakazaneRezervacije;
	}

	public List<Apartman> getIznajmljeniApartmani() {
		return iznajmljeniApartmani;
	}

	public void setIznajmljeniApartmani(List<Apartman> iznajmljeniApartmani) {
		this.iznajmljeniApartmani = iznajmljeniApartmani;
	}
	
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", address=" + address + ", gender=" + gender
				+ ", uloga=" + uloga + ", apartmaniZaIznajmljivanje=" + apartmaniZaIznajmljivanje
				+ ", zakazaneRezervacije=" + zakazaneRezervacije + ", iznajmljeniApartmani=" + iznajmljeniApartmani
				+ "]";
	}

	
	
}
