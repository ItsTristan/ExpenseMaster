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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class models a Claim object.
 * Methods help manage its own attributes, expenses
 * contained within it, and display information.
 *  
 * @author ItsTristan (Tristan Meleshko)
 */
public class Claim extends EMModel implements Comparable<Claim>, SubtextListable {
	
	private String name;
	private ClaimStatus status;
	private Date start_date;
	private Date end_date;
	private ArrayList<Expense> expenses;

	public Claim(String name, ClaimStatus status,
			Date start_date, Date end_date) {
		this.name = name;
		this.status = status;
		this.start_date = start_date;
		this.end_date = end_date;
		expenses = new ArrayList<Expense>();
		notifyViews();
	}
	
	public Claim(String name) {
		this(name, ClaimStatus.IN_PROGRESS, new Date(), null);
	}
	
	public Claim() {
		this("", ClaimStatus.IN_PROGRESS, new Date(), null);
	}

	@Override
	public int compareTo(Claim another) {
		return this.getStartDate().compareTo(another.getStartDate());
	}

	public int addExpense(Expense e) {
		if (! expenses.contains(e)) {
			expenses.add(e);
			notifyViews();
		}
		// If the expense was added previously, still return the expense position.
		return expenses.indexOf(e);
	}
	
	public void deleteExpense(Expense e) {
		if (expenses.contains(e)) {
			expenses.remove(e);
			notifyViews();
		}
	}

	public Expense getExpense(int index) {
		return expenses.get(index);
	}

	public void setName(String name) {
		this.name = name;
		notifyViews();
	}

	public void setStatus(ClaimStatus status) {
		this.status = status;
		notifyViews();
	}

	public void setStartDate(Date start_date) {
		this.start_date = start_date;
		notifyViews();
	}

	public void setEndDate(Date end_date) {
		this.end_date = end_date;
		notifyViews();
	}

	public String getName() {
		return name;
	}

	public ClaimStatus getStatus() {
		return status;
	}

	public Date getStartDate() {
		return start_date;
	}

	public Date getEndDate() {
		return end_date;
	}

	public String toString() {
		return getName() + "(" + start_date.getTime() + ")";
	}

	public String getDateString() {
		// Prints "<StartDate> - <EndDate>", or just "<StartDate>" if no end specified
		if (end_date == null) {
			return ExpenseMasterApplication.global_date_format.format(start_date);
		} else {
			return ExpenseMasterApplication.global_date_format.format(start_date) 
					+ " - " + ExpenseMasterApplication.global_date_format.format(end_date);
		}
	}

	@Override
	public String getText() {
		return name;
	}

	@Override
	public String getSubText() {
		// Format text for subtext list view.
		SimpleDateFormat df = ExpenseMasterApplication.global_date_format;
		if (end_date != null) {
			return "Status: " + status +
			"\nStart Date: " + df.format(start_date) +
			"\nEnd Date: " + df.format(end_date);
		} else {
			return "Status: " + status +
			"\nStart Date: " + df.format(start_date);
		}
	}

	public ArrayList<Money> getExpenseSummary() {
		ArrayList<Money> results = new ArrayList<Money>();
		Map<Currency, Money> sums = new HashMap<Currency, Money>();
		// Iterate over own expenses and sum.
		for (Expense e : expenses) {
			Money value = e.getValue();
			Currency key = value.getCurrencyType();
			// Add Moneys together.
			if (sums.containsKey(key)) {
				sums.put(key, value.add(sums.get(key)));
			} else {
				sums.put(key, value);
			}
		}
		
		// Merge results into a list. 
		for (Currency key : sums.keySet()) {
			results.add(sums.get(key));
		}
		
		return results;
	}
	
	public int getExpenseCount() {
		return expenses.size();
	}

	public ArrayList<Expense> getExpenseList() {
		return expenses;
	}
}



