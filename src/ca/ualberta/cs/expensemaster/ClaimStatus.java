package ca.ualberta.cs.expensemaster;

import android.content.Context;

public enum ClaimStatus {
	IN_PROGRESS(R.string.status_in_progress),
	SUBMITTED(R.string.status_submitted),
	RETURNED(R.string.status_returned),
	APPROVED(R.string.status_approved);

	private String text;
	private int resId;
	
	static {
		IN_PROGRESS.text = "In Progress";
		SUBMITTED.text = "Submitted";
		RETURNED.text = "Returned";
		APPROVED.text = "Approved";
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
}
