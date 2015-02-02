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

public class MarkdownEmail extends EmailMessage {
	private Stack<Integer> listStack;
	
	public MarkdownEmail(String address, String subject) {
		super(address, subject);
		listStack = new Stack<Integer>();
	}
	
	public void putH1(String text) {
		put(text);
		repeat("=", text.length());
		body.append("\n");
	}
	public void putH2(String text) {
		body.append(text + "\n");
		repeat("-", text.length());
		body.append("\n");
	}
	public void putHeader(String text, int depth) {
		repeat("#", depth);
		body.append(" " + text + "\n");
	}
	@Override
	public void newList() {
		setNumberedList(null);
	}

	@Override
	public void newNumberedList() {
		setNumberedList(1);
	}

	public void putListItem(String text) {
		putListItem(text, 0);
	}

	public void putListItem(String text, int depth) {
		repeat("\t", depth);
		put("*\t" + text);
	}
	
	private void setNumberedList(Integer start) {
		// Nested lists are managed by a stack.
		// start = null indecates an unordered list
		listStack.push(start);
	}

	public void putNumberedListItem(String text) {
		repeat("\t", listStack.size());
		// Get the numbering value at the end of the stack
		int last = listStack.size() - 1;
		Integer list_number = listStack.get(last);
		// Put the list item in
		put(list_number + ".\t" + text);
		listStack.set(last, list_number + 1);
	}

	@Override
	public void endList() {
		listStack.pop();
	}

	public void putQuote(String text, int depth) {
		repeat("> ", depth);
		put(text + "\n");
	}

	@Override
	public void putQuote(String text) {
		if (text.contains("\n")) {
			// block quote
			for (String line : text.split("\n")) {
				put("> " + line);
			}
		} else {
			put("> " + text);
		}
	}

	public void putHorizontalRule() {
		putHorizontalRule("- ");
	}

	public void putHorizontalRule(String style) {
		if (!style.isEmpty())
			repeat(style, 60 / style.length());
		body.append("\n");
	}

	private void repeat(String text, int numTimes) {
		for (int i = 0; i < numTimes; i++)
			body.append(text);
	}
	
	@Override
	public void writeClaim(Claim c) throws IOException {
		SimpleDateFormat df = ExpenseMasterApplication.global_date_format;
		// Put in some boring details...
		putH1(c.getName());
		put("Start Date: " + df.format(c.getStartDate()));
		if (c.getEndDate() != null)
			put("End Date: " + df.format(c.getEndDate()));
		put(""); // Insert a gap

		if (c.getExpenseCount() > 0) {
			// Put a big ol' summary in there
			putH2("Total Value");
			for (Money m : c.getExpenseSummary()) {
				putListItem(m.toString());
			}
			put("");
		
			// Throw in the details about all of the expenses.
			putH2("Details");
			put("(Dates are in " + df.toPattern() + " format)\n");
			for (Expense e : c.getExpenseList()) {
				writeExpense(e);
			}
			put("");
		} else {
			putParagraph("No expenses to list.");
		}
	}

	@Override
	public void writeExpense(Expense e) throws IOException {
		put(e.getName());
		newList();
		{
			putListItem("Amount: " + e.getValue().toString());
			putListItem("Date: " + 
					ExpenseMasterApplication.global_date_format.format(e.getDate()));
		}
		endList();
		put("");
	}

	@Override
	protected Intent getMessageIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_EMAIL, address);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		// Convert the body to an HTML body with MIME
		intent.putExtra(Intent.EXTRA_TEXT, body.toString());
		return intent;
	}

}
