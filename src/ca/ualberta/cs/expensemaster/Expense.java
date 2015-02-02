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

import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * This class represents an Expense. It cannot exist
 * without a Claim object attached to it.
 * 
 * @author ItsTristan (Tristan Meleshko)
 *
 */
public class Expense extends EMModel implements SubtextListable {
	private String name;
	private Date date;
	private Money value;

	public Expense(String name, Money value, Date date) {
		this.setName(name);
		this.setValue(value);
		this.setDate(date);
	}

	public Expense() {
		// Empty expense is CAD @ $0 on current date.
		// getDefault() for Locale causes problems because, for some
		// reason, it's not ISO compliant.
		this("", new Money(Currency.getInstance(Locale.CANADA), 0), new Date());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		notifyViews();
	}

	public Money getValue() {
		return value;
	}

	public void setValue(Money value) {
		this.value = value;
		notifyViews();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
		notifyViews();
	}
	
	public String toString() {
		return this.name + " : " + this.value.toString();
	}

	@Override
	public String getText() {
		return name;
	}

	@Override
	public String getSubText() {
		return value.toString() + " on " +
				ExpenseMasterApplication.global_date_format.format(date);
	}

}
