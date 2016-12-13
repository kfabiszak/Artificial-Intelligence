package com.sample;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");

        	Samochod samochod1 = new Samochod("A");
            kSession.insert(samochod1);
            Samochod samochod2 = new Samochod("A");
            kSession.insert(samochod2);
            Samochod samochod3 = new Samochod("B");
            kSession.insert(samochod3);
            Samochod samochod4 = new Samochod("D");
            kSession.insert(samochod4);
            Samochod samochod5 = new Samochod("D");
            kSession.insert(samochod5);
        	kSession.fireAllRules();
        	
        	Klient klient1 = new Klient(true);
        	kSession.insert(klient1);
        	Rezerwacja rezerwacja1 = new Rezerwacja(klient1, "11/11/2016", "12/11/2016", true, true);
        	kSession.insert(rezerwacja1);
        	kSession.fireAllRules();
        	
        	Zwrot zwrot1 = new Zwrot(rezerwacja1, 66, 0, false);
        	kSession.insert(zwrot1);
        	kSession.fireAllRules();
        	
        	Klient klient2 = new Klient(false);
        	kSession.insert(klient2);
        	Rezerwacja rezerwacja2a = new Rezerwacja(klient2, "10/11/2016", "13/11/2016", false, true, "B");
        	kSession.insert(rezerwacja2a);
        	kSession.fireAllRules();
        	Rezerwacja rezerwacja2b = new Rezerwacja(klient2, "11/11/2016", "14/11/2016", false, false, "A");
        	kSession.insert(rezerwacja2b);
        	kSession.fireAllRules();
        	
        	Zwrot zwrot2a = new Zwrot(rezerwacja2a, 100, 0, false);
        	kSession.insert(zwrot2a);
        	kSession.fireAllRules();
        	Zwrot zwrot2b = new Zwrot(rezerwacja2b, 100, 0, false);
        	kSession.insert(zwrot2b);
        	kSession.fireAllRules();
        	
        	Klient klient3 = new Klient(false);
            kSession.insert(klient3);
            Rezerwacja rezerwacja3 = new Rezerwacja(klient3, "11/11/2016", "16/11/2016", false, false, "B");
            kSession.insert(rezerwacja3);
            kSession.fireAllRules();
            
            Zwrot zwrot3 = new Zwrot(rezerwacja3, 1000, 2, true, "18/11/2016");
        	kSession.insert(zwrot3);
        	kSession.fireAllRules();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        
    }
    
    public static int licznikKlientow = 0; //zmienna do tworzenia id klientów
    
    public static int okres(LocalDate dataPoczatku, LocalDate dataKonca) { //funkcja oblicza ró¿nicê pomiêdzy dwoma datami
		int okres = (int) ChronoUnit.DAYS.between(dataPoczatku, dataKonca) + 1;
		return okres;
	}
    
    public static boolean zajetosc(Rezerwacja rezerwacja1, Rezerwacja rezerwacja2){ //funkcja sprawdza czy daty s¹ konfliktowe (czy siê pokrywaj¹)
    	boolean zajetosc = rezerwacja1.dataPobrania.isAfter(rezerwacja2.dataZwrotu) ||
    			rezerwacja1.dataZwrotu.isBefore(rezerwacja2.dataPobrania);
    	return !zajetosc;
    }
    
    public static class Samochod {
    	    	
    	public String typ; //typ samochodu
    	
    	public double taryfaM; //op³ata za dzieñ powy¿ej 3 dni
    	public double taryfaD; //op³ata za dzieñ poni¿ej lub przy 3 dniach
    	
    	public Samochod(String typ) {
    		this.typ = typ;
    	}
    	
    }
    
    public static class Klient {
    	
    	public int id; //unikalny identyfikator klienta
    	
    	public boolean czlonek; //przynale¿noœæ klienta do programu lojalnoœciowego
    	
    	public Klient(boolean czlonek){
    		this.czlonek = czlonek;
    		licznikKlientow += 1;
    		this.id = licznikKlientow;
    	}
    }
    
    public static class Rezerwacja {
    	
    	public Samochod samochod;
    	public Klient klient;
    	public String typ; //typ wypo¿yczanego samochodu
    	
    	//wyposa¿enie dodatkowe
    	public boolean fotelik;
    	public boolean bagaznik;
    	
    	//okres wypo¿yczenia
    	public LocalDate dataPobrania;
    	public LocalDate dataZwrotu;
    	
    	public double oplataKM; //op³ata za przejechany kilometr
    	public double oplata; //op³ata ca³kowita
    	
    	public double taryfaM; //op³ata za dzieñ powy¿ej 3 dni
    	public double taryfaD; //op³ata za dzieñ poni¿ej lub przy 3 dniach
    	public double taryfa; //ostatecznie wybrana taryfa dla danej rezerwacji (op³ata za dzieñ u¿ytkowania)
    	
    	public int czasRezerwacji; //okres wypo¿yczenia samochodu (zak³adany przy rezerwacji)
    	
    	public boolean zatwierdzona = false; //czy rezerwacja zosta³a przyjêta czy te¿ zabrak³o samochodu
    	
    	public Rezerwacja(Klient klient, String dataPobrania, String dataZwrotu, boolean fotelik, boolean bagaznik, String typ) {
    		this.klient = klient;
    		try {
    			this.dataPobrania = LocalDate.parse(dataPobrania, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    			this.dataZwrotu = LocalDate.parse(dataZwrotu, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    		} catch (Exception e) {}
    		this.typ = typ;
    		this.fotelik = fotelik;
    		this.bagaznik = bagaznik;
    		this.czasRezerwacji = okres(this.dataPobrania, this.dataZwrotu);
    	}
    	
    	public Rezerwacja(Klient klient, String dataPobrania, String dataZwrotu, boolean fotelik, boolean bagaznik) {
    		this.klient = klient;
    		try {
    			this.dataPobrania = LocalDate.parse(dataPobrania, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    			this.dataZwrotu = LocalDate.parse(dataZwrotu, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    		} catch (Exception e) {}
    		this.fotelik = fotelik;
    		this.bagaznik = bagaznik;
    		this.typ = "A"; //domyœlny typ samochodu
    		this.czasRezerwacji = okres(this.dataPobrania, this.dataZwrotu);
    	}
    	
    	public boolean sprawdzZajetosc(Rezerwacja rezerwacja) { //sprawdza czy istnieje rezerwacja w tym samym terminie
    		return zajetosc(this, rezerwacja);
    	}
    	
    }
    
    public static class Zwrot {
    	
    	public Rezerwacja rezerwacja;
    	
    	public double dodatkoweKoszty; //kary + op³ata za przejechane kilometry - naliczane przy zwrocie
    	public int licznik; //liczba przejechanych kilometrów
    	public int kolpaki; //liczba zgubionych ko³paków
    	public boolean dowod; //czy zgubiono dowód rejestracyjny
    	public LocalDate dataZwrotu; //realna data oddania samochodu
    	
    	public int czasWypozyczenia; //realny czas wypo¿yczenia samochodu
    	
    	public Zwrot(Rezerwacja rezerwacja, int licznik, int kolpaki, boolean dowod, String dataZwrotu) {
    		this.rezerwacja = rezerwacja;
    		this.licznik = licznik;
    		try {
    			this.dataZwrotu = LocalDate.parse(dataZwrotu, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    		} catch (Exception e) {}
    		this.kolpaki = kolpaki;
    		this.dowod = dowod;
    		this.czasWypozyczenia = okres(rezerwacja.dataPobrania, this.dataZwrotu);
    	}
    	
    	public Zwrot(Rezerwacja rezerwacja, int licznik, int kolpaki, boolean dowod) {
    		this.rezerwacja = rezerwacja;
    		this.licznik = licznik;
    		this.dataZwrotu = rezerwacja.dataZwrotu;
    		this.kolpaki = kolpaki;
    		this.dowod = dowod;
    		this.czasWypozyczenia = okres(rezerwacja.dataPobrania, this.dataZwrotu);
    	}
    	
    }

}
