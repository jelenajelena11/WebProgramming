package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import model.Apartman;

public class ApartmanDAO {

	private HashMap<UUID, Apartman> apartmani = new HashMap<UUID, Apartman>();
	
	public ApartmanDAO() {
		
		this.loadApartmani("");
		
//		UUID id1 = UUID.randomUUID();
//		UUID id2 = UUID.randomUUID();
//		UUID id3 = UUID.randomUUID();
//		
//		User jela = new User("Jela", "goran", "Jelena", "Stojanovic", "jela@gmail.com", "Novi Sad", "F", 2);
//		
//		Lokacija l = new Lokacija(14L, 12L, new Adresa("Djordja Jovanovica", 14, "Novi Sad", 21000));
//		Lokacija l2 = new Lokacija(15L, 12L, new Adresa("Bulevar Oslobodjenja", 14, "Novi Sad", 21000));
//		Lokacija l3 = new Lokacija(15L, 12L, new Adresa("Vuka Karadzica", 14, "Bijeljina", 21000));
//		
//		Apartman a = new Apartman(id1, 0, 2, 2, l, "2020-06-28", "2020-07-22", jela, null, "./assets/img/rent.jpg", 50, 
//				"14:00", "10:00", 0, null, null);
//		Apartman a2 = new Apartman(id2, 0, 2, 2, l2, "2020-06-11", "2020-08-07", jela, null, "./assets/img/rent.jpg", 50, 
//				"15:00", "9:00", 0, null, null);
//		Apartman a3 = new Apartman(id3, 0, 2, 2, l3, "2020-06-21", "2020-08-17", jela, null, "./assets/img/rent.jpg", 50, 
//				"12:00", "10:00", 0, null, null);
//		
//		this.apartmani.put(id1, a);
//		this.apartmani.put(id2, a2);
//		this.apartmani.put(id3, a3);
//		
//		this.saveApartmani("");
	}

	public HashMap<UUID, Apartman> getApartmani() {
		return apartmani;
	}

	public void setApartmani(HashMap<UUID, Apartman> apartmani) {
		this.apartmani = apartmani;
	}

	public Apartman findOneApartman(UUID id) {
		return this.apartmani.get(id);
		
	}
	
	public void saveApartmani(String contextPath) {
		File f = new File("C:\\Users\\Korisnik\\Desktop\\projekat\\WebProgramming\\WebContent\\data\\apartman.txt");
		FileWriter fileWriter = null;
		try {
			//f.createNewFile();
			fileWriter = new FileWriter(f);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String sUsers = objectMapper.writeValueAsString(apartmani);
			fileWriter.write(sUsers);
			fileWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadApartmani(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File("C:\\Users\\Korisnik\\Desktop\\projekat\\WebProgramming\\WebContent\\data\\apartman.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibilityChecker(
					VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, UUID.class, Apartman.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			apartmani = ((HashMap<UUID, Apartman>) objectMapper.readValue(file, type));
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringApartman = objectMapper.writeValueAsString(apartmani);
				fileWriter.write(stringApartman);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
