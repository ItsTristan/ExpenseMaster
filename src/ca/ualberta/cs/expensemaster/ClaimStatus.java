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

import android.content.Context;

public enum ClaimStatus {
	IN_PROGRESS(R.string.status_in_progress),
	SUBMITTED(R.string.status_submitted),
	RETURNED(R.string.status_returned),
	APPROVED(R.string.status_approved);

	private String text;
	private int serialID;
	private int resId;
	
	static {
		IN_PROGRESS.text = "In Progress";
		SUBMITTED.text = "Submitted";
		RETURNED.text = "Returned";
		APPROVED.text = "Approved";
		
		// Used to read and write into serial streams
		IN_PROGRESS.serialID = 0;
		SUBMITTED.serialID = 1;
		RETURNED.serialID = 2;
		APPROVED.serialID = 3;
	}
	
	ClaimStatus(int resId) {
		this.resId = resId;
	}

	/**
	 * English-only toString.
	 */
	public String toString() {
		return text;
	}
	/**
	 * Locale-safe toString. Must provide external context
	 */
	public String toString(Context c) {
		return c.getString(resId);
	}
	
	/**
	 * Gets the resource ID associated with the enumeration.
	 * This can be used to get a locale-safe string.
	 * @return
	 */
	public int getResId() {
		return resId;
	}

	public int getSerialID() {
		return serialID;
	}
}
