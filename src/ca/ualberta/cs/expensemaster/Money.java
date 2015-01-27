package ca.ualberta.cs.expensemaster;

import java.util.Currency;

public class Money {
	private int whole;
	private int cents;
	private Currency currency_type;
	
	public Money(Currency currency_type, int whole, int cents) {
		this.whole = whole;
		this.cents = cents;
		this.currency_type = currency_type;
	}
	
	public Money(Currency currency_type, int whole) {
		this.whole = whole;
		this.cents = 0;
		this.currency_type = currency_type;
	}
	
	public int getWhole() {
		return whole;
	}

	public int getCents() {
		return cents;
	}

	public Money add(Money m) {
		// Make sure we're adding the same types of things
		if (this.getCurrencyType() != m.getCurrencyType()) {
			throw new RuntimeException("Can't add different currency types.");
		}
		
		// Add cents portions
		int cents = this.getCents() + m.getCents();
		// Divmod. Carry goes to whole portions
		int remainder = cents % 100;
		int carry = cents / 100;
		int whole = this.getWhole() + m.getWhole();
		
		return new Money(currency_type, whole + carry, remainder);		
	}

	public Currency getCurrencyType() {
		return currency_type;
	}
}

