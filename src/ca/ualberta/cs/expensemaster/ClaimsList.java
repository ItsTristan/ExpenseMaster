package ca.ualberta.cs.expensemaster;

import java.util.ArrayList;

public class ClaimsList extends EMModel {
	private ArrayList<Claim> claims;

	public ClaimsList() {
		claims = new ArrayList<Claim>();
	}

	public void addClaim(Claim c) {
		if (! claims.contains(c)) {
			claims.add(c);
			updateViews();
		}
	}

	public void deleteClaim(Claim c) {
		if (! claims.contains(c)) {
			claims.remove(c);
			updateViews();
		}
	}
	
	public Claim getClaim(int index) {
		return claims.get(index);
	}

}
