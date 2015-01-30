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

	public static void addClaim(Context ctx, Claim c) {
		if (! claims.contains(c)) {
			claims.add(c);
			Collections.sort(claims);
			saveToFile(ctx);
		}
	}

	public static void deleteClaim(Context ctx, Claim c) {
		if (BuildConfig.DEBUG & claims.contains(c)) {
			throw new AssertionError("Claim not found");
		}
		claims.remove(c);
		saveToFile(ctx);
	}

	public static void deleteClaim(Context ctx, int index) {
		if (BuildConfig.DEBUG & 0 <= index && index < claims.size()) {
			throw new AssertionError("Index out of bounds");
		}
		claims.remove(index);
		saveToFile(ctx);
	}

	public static void updateClaim(Context ctx, int index, Claim c) {
		claims.set(index, c);
		saveToFile(ctx);
	}
	
	public static Claim getClaim(int index) {
		return claims.get(index);
	}
	
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
				c.setStartDate(global_date_format.parse(reader.nextString()));
			} else if (name.equals("claim_end_date")) {
				c.setEndDate(global_date_format.parse(reader.nextString()));
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

		reader.beginObject(); // new Expense
		while (reader.hasNext()) {
			String name = reader.nextName();
			
			if (name.equals("expense_name")) {
				expense_name = reader.nextString();
			} else if (name.equals("currency")) {
				c = Currency.getInstance(reader.nextString());
			} else if (name.equals("amount")) {
				amount = reader.nextString();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject(); // end Expense
		
		// Make sure all the data is here...
		if (expense_name == null || c == null || amount == null) {
			throw new ParseException("Expense is missing data", 0);
		} else {
			return new Expense(expense_name, new Money(c, amount));
		}
	}
	
	private static void writeClaim(JsonWriter writer, Claim c) throws IOException {
		
		writer.beginObject(); // new Claim
		writer.name("claim_name").value(c.getName());
		writer.name("claim_status").value(c.getStatus().name());
		writer.name("claim_start_date").value(global_date_format.format(c.getStartDate()));
		if (c.getEndDate() != null)
			writer.name("claim_end_date").value(global_date_format.format(c.getEndDate()));
		
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
		writer.name("currency").value(e.getValue().getCurrencyType().getSymbol());
		writer.name("amount").value(e.getValue().toValueString());
		writer.endObject(); // end Expense
	}

	private static void loadFromFile(Context ctx) {
		// If the file exists, try to read it.
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
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(ctx, "Failed to write: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(ctx, "Failed to write: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(ctx, "Failed to write: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private static void saveToFile(Context ctx) {
		try {
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
