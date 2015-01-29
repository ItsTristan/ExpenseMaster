package ca.ualberta.cs.expensemaster;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Application;

public class ExpenseMasterApplication extends Application {
	@SuppressWarnings("unused")
	private static final String FILENAME = "save.dat";
	
	private transient static ArrayList<Claim> claims;
	
	public static ArrayList<Claim> getClaims() {
		if (claims == null) {
			loadClaims();
		}
		return claims;
	}

	public static void addClaim(Claim c) {
		if (! claims.contains(c)) {
			claims.add(c);
			Collections.sort(claims);
			saveClaims();
		}
	}

	public static void deleteClaim(Claim c) {
		assert(claims.contains(c));
		claims.remove(c);
		saveClaims();
	}

	public static void deleteClaim(int index) {
		assert (0 <= index && index < claims.size());
		claims.remove(index);
		saveClaims();
	}

	public static void updateClaim(int index, Claim c) {
		claims.set(index, c);
		saveClaims();
	}
	
	public static Claim getClaim(int index) {
		return claims.get(index);
	}

	private static void loadClaims() {
		// if file exists, load claim
		claims = new ArrayList<Claim>();
	}
	
	private static void saveClaims() {
		// TODO
	}
}
