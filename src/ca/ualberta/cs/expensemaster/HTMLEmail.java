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
import java.text.SimpleDateFormat;
import java.util.Stack;

import android.content.Intent;
import android.text.Html;

/* This class was built so that emails could be sent in HTML,
 * ...but it turns out Android doesn't support HTML and MIME very well,
 * as well as too many clients (c'mon, what year is it anyway?!).
 */
public class HTMLEmail extends EmailMessage { 
	private Stack<String> tagStack;

	public HTMLEmail(String address, String subject) {
		super(address, subject);

		tagStack = new Stack<String>();
		startTag("html");
		startTag("body");
	}
	
	// Tag starters
	public void startTag(String tag, String properties) {
		put("<" + tag + " " + properties + " >");
		tagStack.add("</" + tag + ">");
	}
	public void startTag(String tag) {
		put("<" + tag + ">");
		tagStack.add("</" + tag + ">");
	}
	// Tag ender
	public void endTag() {
		put(tagStack.pop());
	}
	
	/**
	 * Put some things into the email
	 * @param p
	 */
	@Override
	public void putParagraph(String data) {
		putTag("p", data);
	}
	@Override
	public void put(String text) {
		body.append(text);
	}
	public void putTag(String tag, String data) {
		startTag(tag);
		put(data);
		endTag();
	}
	public void putSmallTag(String tag, String data) {
		put("<" + tag + " " + data + " />");
	}
	public void putSmallTag(String tag) {
		put("<" + tag + " />");
	}

	@Override
	public void putListItem(String text) {
		putTag("li", text);
	}

	@Override
	public void putNumberedListItem(String text) {
		putTag("li", text);
	}

	@Override
	public void putHeader(String text, int depth) {
		putTag("h"+depth, text);
	}

	@Override
	public void putH1(String text) {
		putTag("h1", text);
	}

	@Override
	public void putH2(String text) {
		putTag("h2", text);
	}

	@Override
	public void putQuote(String text) {
		if (text.contains("\n")) {
			putTag("blockquote", text);
		} else {
			putTag("q", text);
		}
	}

	@Override
	public void putHorizontalRule() {
		putSmallTag("hr");
	}

	@Override
	public void newList() {
		startTag("ul");
	}

	@Override
	public void newNumberedList() {
		startTag("ol");
	}

	@Override
	/**
	 * This function should be called in the correct stack order.
	 */
	public void endList() {
		endTag();
	}

	@Override
	public void writeClaim(Claim c) throws IOException {
		SimpleDateFormat df = ExpenseMasterApplication.global_date_format;
		startTag("p");
		// Write data about this claim
		putH1(c.getName());
		putTag("br", c.getStatus().toString());
		putTag("br", "Start Date: " + df.format(c.getStartDate()));
		if (c.getEndDate() != null) {
			putTag("br", "End Date: " + df.format(c.getEndDate()));
		}
		if (c.getExpenseCount() > 0) {
			// List the summary totals
			putH2("Total Value");
			// Not using lists here because most of
			// HTML gets stripped out of email clients
			for (Money m : c.getExpenseSummary()) {
				putTag("br", m.toString());
			}
			
			// List all of the expenses
			putH2("Details");
			putParagraph("(Dates are in " + 	df.toPattern() + " format)");
			for (Expense e : c.getExpenseList()) {
				writeExpense(e);
			}
		} else {
			putParagraph("No expenses to list.");
		}
		endTag();
	}

	@Override
	public void writeExpense(Expense e) throws IOException {
		putHeader(e.getName(), 3);
		
		putTag("br", "Amount: " + e.getValue().toString());
		putTag("br", "Date: " + 
				ExpenseMasterApplication.global_date_format.format(e.getDate()));
	}

	@Override
	public Intent getMessageIntent() {
		if (isOpen()) {
			throw new IllegalStateException("Email hasn't been closed.");
		}
		// Create the intent and start the activity using the given context.
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_EMAIL, address);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		// Convert the body to an HTML body with MIME
		intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body.toString()));
		return intent;
	}

}
