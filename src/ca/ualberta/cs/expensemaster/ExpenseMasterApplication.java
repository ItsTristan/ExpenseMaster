/* This file is part of the ExpenseMaster Android Application
 * See github.com/ItsTristan/ExpenseMaster for more information
 * 
 * Copyright (C) 2015 Tristan Meleshko
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA. */

package ca.ualberta.cs.expensemaster;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class ExpenseMasterApplication extends Application {
	private static final String FILENAME = "save.dat";
	private transient static ClaimsList claims;

	// This date format is to be consistent across all dates
	public static final SimpleDateFormat global_date_format = new SimpleDateFormat(
			"yyyy/MM/dd", Locale.CANADA);

	public static ArrayList<Claim> getClaimsList(Context ctx) {
		if (claims == null) {
			claims = new ClaimsList();
			loadFromFile(ctx);
		}
		return claims.getClaims();
	}
	
	/*
	 * Fake addView for bridging.
	 */
	public static void addView(EMView<ClaimsList> v) {
		claims.addView(v);
	}

	/* This class acts as a bridge between ClaimsList and the  
	 * outside world. ClaimsList doesn't need to be aware of
	 * "context", so the application is in charge of displaying
	 * errors and messages to the user, if needed.
	 */
	/**
	 * Adds a claim to the application.
	 * 
	 * @param ctx
	 * @param c
	 * @return The index of the newly added claim.
	 */
	public static int addClaim(Context ctx, Claim c) {
		claims.add(c);
		saveToFile(ctx);
		return claims.findClaim(c);
	}

	public static void deleteClaim(Context ctx, Claim c) {
		claims.remove(c);
		saveToFile(ctx);
	}

	public static void deleteClaim(Context ctx, int index) {
		// May throw an OOB exception if the caller doesn't validate.
		claims.remove(index);
		saveToFile(ctx);
	}

	public static void updateClaim(Context ctx, int index, Claim c) {
		claims.update(index, c);
		saveToFile(ctx);
	}

	public static int findClaim(Claim c) {
		return claims.findClaim(c);
	}

	public static Claim getClaim(int index) {
		return claims.getClaim(index);
	}

	private static void loadFromFile(Context ctx) {
		try {
			FileInputStream fis = ctx.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			String error = claims.importClaims(in);
			// Print error message if it occurs
			if (error!= null) {
				Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
			}
			// Close the stream
			fis.close();
		} catch (FileNotFoundException e) {
			// long hair don't care;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(ctx, "Load error occured.", Toast.LENGTH_SHORT).show();
		}
	}

	private static void saveToFile(Context ctx) {
		try {
			// Need a context because we are calling in context free environment
			FileOutputStream fos = ctx.openFileOutput(FILENAME, 0);
			OutputStreamWriter out = new OutputStreamWriter(fos);
	
			String error = claims.exportClaims(out);
			// Print error message if it occurs
			if (error!= null) {
				Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
			}
			// Force Linux to properly close its dang streams!
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(ctx, "Save error occured.", Toast.LENGTH_SHORT).show();
		}
	}
}
