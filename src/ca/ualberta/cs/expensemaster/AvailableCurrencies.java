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

/**
 * This file was generated from a CSV file at
 * 	 https://developers.google.com/adsense/management/appendix/currencies
 * 
 * Sane people using android API 19 should use Currency.getAvailableCurrencies()
 * rather than building giant enums from CSVs.
 * 
 * Jan 30, 2015
 */
public enum AvailableCurrencies {
	AED("United Arab Emirates Dirham"),
	ARS("Argentine Peso"),
	AUD("Australian Dollar"),
	BGN("Bulgarian Lev"),
	BND("Brunei Dollar"),
	BOB("Bolivian Boliviano"),
	BRL("Brazilian Real"),
	CAD("Canadian Dollar"),
	CHF("Swiss Franc"),
	CLP("Chilean Peso"),
	CNY("Chinese Yuan Renminbi"),
	COP("Colombian Peso"),
	CZK("Czech Republic Koruna"),
	DKK("Danish Krone"),
	EGP("Egyptian Pound"),
	EUR("Euro"),
	FJD("Fijian Dollar"),
	GBP("British Pound Sterling"),
	HKD("Hong Kong Dollar"),
	HRK("Croatian Kuna"),
	HUF("Hungarian Forint"),
	IDR("Indonesian Rupiah"),
	ILS("Israeli New Sheqel"),
	INR("Indian Rupee"),
	JPY("Japanese Yen"),
	KES("Kenyan Shilling"),
	KRW("South Korean Won"),
	LTL("Lithuanian Litas"),
	MAD("Moroccan Dirham"),
	MXN("Mexican Peso"),
	MYR("Malaysian Ringgit"),
	NOK("Norwegian Krone"),
	NZD("New Zealand Dollar"),
	PEN("Peruvian Nuevo Sol"),
	PHP("Philippine Peso"),
	PKR("Pakistani Rupee"),
	PLN("Polish Zloty"),
	RON("Romanian Leu"),
	RSD("Serbian Dinar"),
	RUB("Russian Ruble"),
	SAR("Saudi Riyal"),
	SEK("Swedish Krona"),
	SGD("Singapore Dollar"),
	THB("Thai Baht"),
	TRY("Turkish Lira"),
	TWD("New Taiwan Dollar"),
	UAH("Ukrainian Hryvnia"),
	USD("US Dollar"),
	VEF("Venezuelan Bolí­var Fuerte"),
	VND("Vietnamese Dong"),
	ZAR("South African Ran");
	
	private String text;
	
	private AvailableCurrencies(String text) {
		this.text = text;
	}
	
	public Currency getCurrency() {
		// Return an equivalent Currency object
		return Currency.getInstance(name());
	}
	
	public String toString() {
		return name() + " (" + text + ")";
	}
}
