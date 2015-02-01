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
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.widget.Toast;

public class ExpenseMasterApplication extends Application {
	private static final String FILENAME = "save.dat";
	
	private transient static ArrayList<Claim> claims;
	
	// This date format is to be consistent across all dates 
	public static final SimpleDateFormat global_date_format =
			new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
	
	public static ArrayList<Claim> getClaims(Context ctx) {
		if (claims == null) {
			loadFromFile(ctx);
		}
		return claims;
	}

	/**
	 * Adds a claim to the application.
	 * @param ctx
	 * @param c
	 * @return The index of the newly added claim.
	 */
	public static int addClaim(Context ctx, Claim c) {
		if (! claims.contains(c)) {
			claims.add(c);
			Collections.sort(claims);
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
	
	
	// FIXME: This should be moved to its own class for serializing stuff.
	//			This way, we can use the same interface for exporting HTML
	//			when we make the Email portion.
	private static Claim readClaim(JsonReader reader) throws IOException, ParseException {
		reader.beginObject(); // new Claim
		Claim c = new Claim();
		while (reader.hasNext()) {
			// Find out what we're reading
			String name = reader.nextName();
			
			// ...then work out how to interpret it
			if (name.equals("claim_name")) {
				c.setName(reader.nextString());
			} else if (name.equals("claim_status")) {
				c.setStatus(ClaimStatus.valueOf(reader.nextString()));
			} else if (name.equals("claim_start_date")) {
				c.setStartDate(new Date(reader.nextLong()));
			} else if (name.equals("claim_end_date")) {
				c.setEndDate(new Date(reader.nextLong()));
			} else if (name.equals("expenses")) {
				// Array<Expense>
				reader.beginArray();
				while (reader.hasNext()) {
					c.addExpense( readExpense( reader ) );
				}
				reader.endArray();
				// end Array<Expense>
			} else {
				reader.skipValue();
			}
		}
		reader.endObject(); // end Claim
		
		return c;
	}
	
	private static Expense readExpense(JsonReader reader) throws IOException, ParseException {
		String expense_name = null;
		Currency c = null;
		String amount = null;
		Date date = null;

		reader.beginObject(); // new Expense
		while (reader.hasNext()) {
			String name = reader.nextName();
			
			if (name.equals("expense_name")) {
				expense_name = reader.nextString();
			} else if (name.equals("currency")) {
				c = Currency.getInstance(reader.nextString());
			} else if (name.equals("amount")) {
				amount = reader.nextString();
			} else if (name.equals("date")) {
				date = new Date(reader.nextLong());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject(); // end Expense
		
		// Make sure all the data is here...
		if (expense_name == null || c == null || amount == null || date == null) {
			throw new ParseException("Expense is missing data", 0);
		} else {
			return new Expense(expense_name, new Money(c, amount), date);
		}
	}
	
	private static void writeClaim(JsonWriter writer, Claim c) throws IOException {
		
		writer.beginObject(); // new Claim
		writer.name("claim_name").value(c.getName());
		writer.name("claim_status").value(c.getStatus().name());
		// Date is written as milliseconds from epoch to be independent of
		// the global date format
		writer.name("claim_start_date").value(c.getStartDate().getTime());
		if (c.getEndDate() != null)
			writer.name("claim_end_date").value(c.getEndDate().getTime());
		
		// Array<Expense>
		writer.name("expenses").beginArray();
		for (int i = 0; i < c.getExpenseCount(); i++) {
			writeExpense( writer, c.getExpense(i) );
		}
		writer.endArray();
		// end Array<Expense>
		
		writer.endObject(); // end Claim
	}
	private static void writeExpense(JsonWriter writer, Expense e) throws IOException {
		writer.beginObject(); // new Expense
		writer.name("expense_name").value(e.getName());
		writer.name("currency").value(e.getValue().getCurrencyType().getCurrencyCode());
		writer.name("amount").value(e.getValue().toValueString());
		writer.name("date").value(e.getDate().getTime());
		writer.endObject(); // end Expense
	}

	private static void loadFromFile(Context ctx) {
		// If the file exists, try to read it.
		claims = new ArrayList<Claim>();
		try {
			FileInputStream fis = ctx.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			claims = new ArrayList<Claim>();
			
			// Try to read an array of stuff
			{
				JsonReader reader = new JsonReader(in); {
					reader.beginArray(); {
						while (reader.hasNext()) {
							claims.add(readClaim(reader));
						}
					} reader.endArray();
				} reader.close();
			}
			
			fis.close();
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
			Toast.makeText(ctx, "Saving...", Toast.LENGTH_SHORT).show();
			// Need a context because we are calling in context free environment
			FileOutputStream fos = ctx.openFileOutput(FILENAME, 0);
			OutputStreamWriter out = new OutputStreamWriter(fos);
			
			// Write claims as Json using Gson
			JsonWriter writer = new JsonWriter(out); {
				writer.beginArray(); {
					for (Claim c : claims) {
						writeClaim(writer, c);
					}
				} writer.endArray();
			} writer.flush();
			writer.close();
			
			// Force Linux to properly close its dang streams!
			//out.flush();
			fos.close();
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
}
