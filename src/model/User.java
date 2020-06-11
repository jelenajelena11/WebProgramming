package model;

public class User {
	
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String gender;
	
	private int uloga = 0; // 0 - Korisnik; 1 - Admin; 2 - Agent
	
	public User(){
		
	}
	
	public User(String userName, String password, String firstName, String lastName, String email, String address, String gender) {
		
		super();
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
	
}
