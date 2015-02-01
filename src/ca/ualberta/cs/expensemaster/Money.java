package ca.ualberta.cs.expensemaster;

import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

public class Money {
	
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
	
	/**
	 * Tries to create a new money instance. Assumes string is
	 * a decimal formatted string and checks for the right number
	 * of digits.
	 * @param currency_type
	 * @param amount
	 * @throws ParseException
	 */
	public Money(Currency currency_type, String value) throws ParseException {
		this.currency_type = currency_type;
		determineDenominationSize();
		
		if (value == null || value.isEmpty()) {
			throw new ParseException("Amount cannot be blank", -1);
		}
		String value_parts[];
		
		// Split should return 1 or 2 things.
		value_parts = value.split("\\.");
		if (value_parts.length == 1) {
			// This shouldn't throw errors if it's a decimal format
			// since it's non-empty
			whole = Integer.parseInt(value_parts[0]);
			cents = 0;
		} else if (value_parts.length == 2) {
			// This might throw errors if either side of the decimal is blank
			// So, we handle these cases appropriately.
			
			// If whole portion is empty, assume it is supposed to be 0.
			whole = (value_parts[0].isEmpty()) ? 0 :Integer.parseInt(value_parts[0]);
			
			// "cents" portion must have the right number of digits for
			// the given currency. The hint for amount is changed when the 
			// currency type changes to make entry easier.
			if (value_parts[1].length() != currency_type.getDefaultFractionDigits()) {
				throw new ParseException("Amount must have " + 
						currency_type.getDefaultFractionDigits() + " decimal places.", 0);
			}
			cents = Integer.parseInt(value_parts[1]);
		} else {
			// Should never happen if using android's decimal format.
			throw new ParseException("Malformatted string", -1);
		}
	}

	private void determineDenominationSize() {
		denom_size = 1;
		// Denomination size is (almost) always powers of 10, but varies by currency.
		// These cases aren't covered by the built-in Currency. 
		for (int i = 0; i < currency_type.getDefaultFractionDigits(); i++) {
			denom_size *= 10;
		}
	}
	
	public int getDenominationDigits() {
		return currency_type.getDefaultFractionDigits();
	}
	
	public int getDenominationSize() {
		return denom_size;
	}
	
	/**
	 * Returns a sample string describing a zero-cents display
	 * Example, for CAD, this will return ".00"
	 * For JPY, this will return ""
	 * @return
	 */
	public String getZeroDenominationString() {
		if (currency_type.getDefaultFractionDigits() == 0) {
			return "";
		} else {
			String s = ".";
			// multiply string "0" by n.
			for (int i = 0; i < currency_type.getDefaultFractionDigits(); i++) {
				s += "0";
			}
			return s;
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

	public void setCurrencyType(Currency currency_type) {
		this.currency_type = currency_type;
		determineDenominationSize();
	}
	
	public void setCurrencyType(AvailableCurrencies currency_type) {
		this.currency_type = currency_type.getCurrency();
		determineDenominationSize();
	}
	
	public String toString() {
		// Formats the string decimal with the appropriate number of fraction digits.
		// Need to check the case when numDigs <= 0, because these are valid
		// return values and they will mess up the string formatter.
		int numDigs = currency_type.getDefaultFractionDigits();
		if (numDigs > 0) {
			return String.format(Locale.US, "%d.%0"
				+currency_type.getDefaultFractionDigits()
				+"d %s", whole, cents, currency_type.toString());
		} else {
			return String.format(Locale.CANADA, "%d %s", whole,
					currency_type.toString());
		}
	}
	
	/**
	 * Returns only the decimal formatted value for the object,
	 * without the locale attached to the end.
	 * @return
	 */
	public String toValueString() {
		// Same as toString but without Currency names
		int numDigs = currency_type.getDefaultFractionDigits();
		if (numDigs > 0) {
			return String.format(Locale.CANADA, "%d.%0"
					+currency_type.getDefaultFractionDigits()
					+"d", whole, cents);
		} else {
			return String.format(Locale.CANADA, "%d", whole);
		}
	}

	/**
	 * Returns an AvailableCurrencies value for the object's
	 * currency value
	 * @return
	 */
	public AvailableCurrencies toAvailableCurrency() {
		return AvailableCurrencies.valueOf(currency_type.toString());
	}
}

