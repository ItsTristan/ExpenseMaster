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

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import android.util.JsonReader;

/**
 * Deserializer for saved data.
 * 
 * @author ItsTristan (Tristan Meleshko)
 *
 */
public class JsonClaimImporter implements ClaimImporter {
	private JsonReader reader;
	public JsonClaimImporter(Reader in) {
		reader= new JsonReader(in);
	}

	public Claim readClaim() throws IOException, ParseException {
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

	private Expense readExpense(JsonReader reader) throws IOException, ParseException {
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

	@Override
	public Expense readExpense() throws IOException, ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Claim> readAll() throws IOException, ParseException {
		List<Claim> claims = new ArrayList<Claim>();
		
		// Try to read an array of stuff
		reader.beginArray();
		while (reader.hasNext()) {
			claims.add(readClaim());
		}
		reader.endArray();
		
		return claims;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
}
