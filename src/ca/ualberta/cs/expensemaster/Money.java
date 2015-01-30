package ca.ualberta.cs.expensemaster;

import java.io.Serializable;
import java.util.Currency;
import java.util.Locale;

import org.apache.http.ParseException;

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
		determineDenominationSize();
	}
	
	public Money(Currency currency_type, int whole) {
		this.whole = whole;
		this.cents = 0;
		this.currency_type = currency_type;
		determineDenominationSize();
	}
	
	public Money(Currency currency_type, String amount) {
		String[] parts = amount.split("\\.");
		if (parts.length != 2) {
			throw new ParseException("Malformatted amount string");
		}
		this.whole = Integer.parseInt(parts[0]);
		this.cents = Integer.parseInt(parts[1]);
		this.currency_type = currency_type;
		determineDenominationSize();
	}

	private void determineDenominationSize() {
		denom_size = 1;
		// Denomination size is (almost) always powers of 10, but varies by currency.
		// These cases aren't covered by the built-in Currency. 
		for (int i = 0; i < currency_type.getDefaultFractionDigits(); i++) {
			denom_size *= 10;
		}
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
		// Formats the string decimal with the appropriate number of fraction digits.
		return String.format(Locale.US, "%d.%0"
				+currency_type.getDefaultFractionDigits()
				+"d %s", whole, cents, currency_type.toString());
	}
	
	public String toValueString() {
		// Same as toString but without Currency names
		return String.format(Locale.US, "%d.%0"
				+currency_type.getDefaultFractionDigits()
				+"d", whole, cents);
	}
}

