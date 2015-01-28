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

	public void addClaim(Claim c) {
		if (! claims.contains(c)) {
			claims.add(c);
			Collections.sort(claims);
			saveClaims();
		}
	}

	public void deleteClaim(Claim c) {
		if (! claims.contains(c)) {
			claims.remove(c);
			saveClaims();
		}
	}

	public void updateClaim(int index, Claim c) {
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
