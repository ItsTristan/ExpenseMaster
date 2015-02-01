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
import java.io.Writer;
import java.util.List;

import android.util.JsonWriter;

public class JsonClaimExporter implements ClaimExporter {
	private JsonWriter writer;
	public JsonClaimExporter(Writer out) {
		writer = new JsonWriter(out);
	}

	public void writeClaim(Claim c) throws IOException {
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
			writeExpense(c.getExpense(i) );
		}
		writer.endArray();
		// end Array<Expense>
		
		writer.endObject(); // end Claim
	}
	public void writeExpense(Expense e) throws IOException {
		writer.beginObject(); // new Expense
		writer.name("expense_name").value(e.getName());
		writer.name("currency").value(e.getValue().getCurrencyType().getCurrencyCode());
		writer.name("amount").value(e.getValue().toValueString());
		writer.name("date").value(e.getDate().getTime());
		writer.endObject(); // end Expense
	}

	@Override
	public void writeAll(List<Claim> claims) throws IOException {
		// Write claims as Json using Gson
		writer.beginArray();
		for (Claim c : claims) {
			writeClaim(c);
		}
		writer.endArray();
	}
	
	public void close() throws IOException{
		writer.flush();
		writer.close();
	}
}
