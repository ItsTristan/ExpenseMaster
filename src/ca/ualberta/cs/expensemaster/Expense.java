package ca.ualberta.cs.expensemaster;

import java.util.Currency;
import java.util.Date;
import java.util.Locale;

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
