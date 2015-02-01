package ca.ualberta.cs.expensemaster;

import java.util.Currency;

/**
 * This document was generated from a CSV file at
 * 	 https://developers.google.com/adsense/management/appendix/currencies
 * 
 * Sane people using android API 19 should use Currency.getAvailableCurrencies()
 * rather than building giant enums from CSVs.
 * 
 * Jan 30, 2015
 */
public enum AvailableCurrencies {
	ARS,
	AUD,
	BGN,
	BND,
	BOB,
	BRL,
	CAD,
	CHF,
	CLP,
	CNY,
	COP,
	CZK,
	DKK,
	EGP,
	EUR,
	FJD,
	GBP,
	HKD,
	HRK,
	HUF,
	IDR,
	ILS,
	INR,
	JPY,
	KES,
	KRW,
	LTL,
	MAD,
	MXN,
	MYR,
	NOK,
	NZD,
	PEN,
	PHP,
	PKR,
	PLN,
	RON,
	RSD,
	RUB,
	SAR,
	SEK,
	SGD,
	THB,
	TRY,
	TWD,
	UAH,
	USD,
	VEF,
	VND,
	ZAR;
	
	public Currency getCurrency() {
		// Return an equivalent Currency object
		return Currency.getInstance(toString());
	}
	
}
