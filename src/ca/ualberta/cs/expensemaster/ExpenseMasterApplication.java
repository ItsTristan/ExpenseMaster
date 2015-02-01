package ca.ualberta.cs.expensemaster;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class ExpenseMasterApplication extends Application {
	private static final String FILENAME = "save.dat";

	private transient static ArrayList<Claim> claims;

	// This date format is to be consistent across all dates
	public static final SimpleDateFormat global_date_format = new SimpleDateFormat(
			"yyyy/MM/dd", Locale.CANADA);

	public static ArrayList<Claim> getClaims(Context ctx) {
		if (claims == null) {
			loadFromFile(ctx);
		}
		return claims;
	}

	/**
	 * Adds a claim to the application.
	 * 
	 * @param ctx
	 * @param c
	 * @return The index of the newly added claim.
	 */
	public static int addClaim(Context ctx, Claim c) {
		if (!claims.contains(c)) {
			claims.add(c);
			saveToFile(ctx);
		}
		return claims.indexOf(c);
	}

	public static void deleteClaim(Context ctx, Claim c) {
		if (BuildConfig.DEBUG && claims.contains(c)) {
			throw new AssertionError("Claim not found");
		}
		claims.remove(c);
		saveToFile(ctx);
	}

	public static void deleteClaim(Context ctx, int index) {
		// May throw an OOB exception if the caller doesn't validate.
		claims.remove(index);
		saveToFile(ctx);
	}

	public static void updateClaim(Context ctx, int index, Claim c) {
		claims.set(index, c);
		saveToFile(ctx);
	}

	public static int findClaim(Claim c) {
		return claims.indexOf(c);
	}

	public static Claim getClaim(int index) {
		return claims.get(index);
	}

	private static void loadFromFile(Context ctx) {
		// If the file exists, try to read it.
		claims = new ArrayList<Claim>();
		try {
			FileInputStream fis = ctx.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));

			JsonClaimImporter reader = new JsonClaimImporter(in);
			// Safe to downcast here because it's a List<Claim>
			claims = (ArrayList<Claim>) reader.readAll();
			reader.close();

			fis.close();
			// XXX 
			Toast.makeText(ctx, "Loaded from file.", Toast.LENGTH_SHORT).show();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void saveToFile(Context ctx) {
		try {
			// Need a context because we are calling in context free environment
			FileOutputStream fos = ctx.openFileOutput(FILENAME, 0);
			OutputStreamWriter out = new OutputStreamWriter(fos);

			JsonClaimExporter writer = new JsonClaimExporter(out);
			writer.writeAll(claims);
			writer.close();

			// Force Linux to properly close its dang streams!
			fos.close();
			// XXX 
			Toast.makeText(ctx, "Saved!", Toast.LENGTH_SHORT).show();
			// Hopefully this never happens
		} catch (FileNotFoundException e) {
			Toast.makeText(ctx, "File not found.", Toast.LENGTH_SHORT).show();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(ctx, "Saving failed.", Toast.LENGTH_SHORT).show();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sortClaims() {
		Collections.sort(claims);
	}
}
