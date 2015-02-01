package ca.ualberta.cs.expensemaster;

public abstract class RequestCode {
	/**
	 * This class is for global constants that don't
	 * nicely fit into the scope of Enums.
	 */
	public static final int REQUEST_NEW_CLAIM = 1;
	public static final int REQUEST_EDIT_CLAIM = 2;
	
	public static final int REQUEST_CLAIM_SUMMARY = 3;

	public static final int REQUEST_NEW_EXPENSE = 4;
	public static final int REQUEST_EDIT_EXPENSE = 5;
}
