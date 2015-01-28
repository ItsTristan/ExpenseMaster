package ca.ualberta.cs.expensemaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClaimsList extends EMModel {
	private ArrayList<Claim> claims;
	private ArrayList<Map<String, String>> list_data;

	public ClaimsList() {
		claims = new ArrayList<Claim>();
		list_data = new ArrayList<Map<String, String>>();
	}

	public void addClaim(Claim c) {
		if (! claims.contains(c)) {
			claims.add(c);
			// http://stackoverflow.com/questions/7916834/android-adding-listview-sub-item-text
			//  Jan 27, 2015
			
			// FIXME: Need to watch if c changes
			Map<String, String> datum = new HashMap<String, String>(2);
		    datum.put("title", c.getTitle());
		    datum.put("subtitle", c.getSubTitle());
		    list_data.add(datum);
		    
			notifyViews();
		}
	}

	public void deleteClaim(Claim c) {
		if (! claims.contains(c)) {

			Map<String, String> datum = new HashMap<String, String>(2);
		    datum.put("title", c.getTitle());
		    datum.put("subtitle", c.getSubTitle());
		    
		    // FIXME: datum may not be the same as c requested
		    list_data.remove(datum);
		    
			claims.remove(c);
			notifyViews();
		}
	}
	
	public Claim getClaim(int index) {
		return claims.get(index);
	}
	
	public ArrayList<Map<String, String>> getData() {
		return list_data;
	}
}
