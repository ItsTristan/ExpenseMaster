package ca.ualberta.cs.expensemaster;

public enum ClaimStatus {
	IN_PROGRESS,
	SUBMITTED,
	RETURNED,
	APPROVED;

	private String text;
	
	// Sneaky, easier-to-read way of doing toString
	static {
		IN_PROGRESS.text = "In Progress";
		SUBMITTED.text = "Submitted";
		RETURNED.text = "Returned";
		APPROVED.text = "Approved";
	}
	public String toString() {
		return text;
	}
}
