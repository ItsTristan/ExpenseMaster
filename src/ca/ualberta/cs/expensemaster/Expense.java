package ca.ualberta.cs.expensemaster;

import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class Expense extends EMModel {
	private String name;
	private Date date;
	private Money value;

	public Expense(String name, Money value, Date date) {
		this.setName(name);
		this.setValue(value);
		this.setDate(date);
	}

	public Expense() {
		// Empty expense is default currency @ $0 on current date.
		this("", new Money(Currency.getInstance(Locale.getDefault()), 0), new Date());
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

}
