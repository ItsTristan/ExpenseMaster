package ca.ualberta.cs.expensemaster;

import java.io.Serializable;
import java.util.Currency;
import java.util.Locale;

public class Money implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int denom_size = 100;
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
		int remainder = cents % denom_size;
		int carry = cents / denom_size;
		int whole = this.getWhole() + m.getWhole();
		
		return new Money(currency_type, whole + carry, remainder);		
	}

	public Currency getCurrencyType() {
		return currency_type;
	}
	
	public String toString() {
		return String.format(Locale.US, "%d.%02d %s", whole, cents, currency_type.toString());
	}
}

