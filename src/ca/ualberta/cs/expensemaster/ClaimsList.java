package ca.ualberta.cs.expensemaster;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Extracted from the Application class file. This
 * object manages a list of claims. Manages import
 * and export of Claims by de/serializing Json
 * via Gson. 
 * 
 * Issues:
 * Currently, the application class acts as a bridge
 * between this class and the world application.
 * 
 * @author ItsTristan (Tristan Meleshko)
 * 
 */
public class ClaimsList extends EMModel {
	private static ArrayList<Claim> claims;
	
	public ClaimsList() {
		claims = new ArrayList<Claim>();
	}
	
	public ArrayList<Claim> getClaims() {
		return claims;
	}
	
	public boolean contains(Claim c) {
		return claims.contains(c);
	}
	
	public void add(Claim c) {
		if (!claims.contains(c)) {
			claims.add(c);
			sort();
			notifyViews();
		}
	}
	
	public void remove(Claim c) {
		claims.remove(c);
		sort();
		notifyViews();
	}
	
	public void remove(int i) {
		claims.remove(i);
		sort();
		notifyViews();
	}
	
	public void update(int i, Claim c) {
		claims.set(i, c);
		sort();
		notifyViews();
	}

	public int findClaim(Claim c) {
		return claims.indexOf(c);
	}
	
	public Claim getClaim(int i) {
		return claims.get(i);
	}

	public String importClaims(Reader in) {
		// If the file exists, try to read it.
		claims = new ArrayList<Claim>();
		try {
			JsonClaimImporter reader = new JsonClaimImporter(in);
			// Safe to downcast here because it's a List<Claim>
			claims = (ArrayList<Claim>) reader.readAll();
			reader.close();

			// Sort and notify
			sort();
			notifyViews();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// We don't care if the file wasn't found.
			// It might've been their first time opening the file.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Cannot open file.";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Cannot read from file.";
		}
		return null;
	}
	
	public String exportClaims(Writer out) {
		try {
			JsonClaimExporter writer = new JsonClaimExporter(out);
			writer.writeAll(claims);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "File not found.";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Saving failed.";
		}
		// Nothing went wrong when exporting.
		return null;
	}
	
	private static void sort() {
		Collections.sort(claims);
	}
	
}
