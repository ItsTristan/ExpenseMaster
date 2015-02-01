package ca.ualberta.cs.expensemaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Claim extends EMModel implements Comparable<Claim>, SubtextListable {
	
	private String name;
	private ClaimStatus status;
	private Date start_date;
	private Date end_date;
	private ArrayList<Expense> expenses;

	public Claim(String name, ClaimStatus status,
			Date start_date, Date end_date) {
		this.name = name;
		this.status = status;
		this.start_date = start_date;
		this.end_date = end_date;
		expenses = new ArrayList<Expense>();
		notifyViews();
	}
	
	public Claim(String name) {
		this(name, ClaimStatus.IN_PROGRESS, new Date(), null);
	}
	
	public Claim() {
		this("", ClaimStatus.IN_PROGRESS, new Date(), null);
	}

	public int addExpense(Expense e) {
		if (! expenses.contains(e)) {
			expenses.add(e);
			notifyViews();
		}
		// If the expense was added previously, still return the expense position.
		return expenses.indexOf(e);
	}
	
	public void deleteExpense(Expense e) {
		if (expenses.contains(e)) {
			expenses.remove(e);
			notifyViews();
		}
	}

	public Expense getExpense(int index) {
		return expenses.get(index);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		notifyViews();
	}
	
	public String toString() {
		return getName() + "(" + start_date.getTime() + ")";
	}
	
	public String getDateString() {
		if (end_date == null) {
			return ExpenseMasterApplication.global_date_format.format(start_date);
		} else {
			return ExpenseMasterApplication.global_date_format.format(start_date) 
					+ " - " + ExpenseMasterApplication.global_date_format.format(end_date);
		}
	}

	public ClaimStatus getStatus() {
		return status;
	}
	

	public void setStatus(ClaimStatus status) {
		this.status = status;
		notifyViews();
	}

	public Date getStartDate() {
		return start_date;
	}

	public void setStartDate(Date start_date) {
		this.start_date = start_date;
		notifyViews();
	}

	public Date getEndDate() {
		return end_date;
	}

	public void setEndDate(Date end_date) {
		this.end_date = end_date;
		notifyViews();
	}

	@Override
	public int compareTo(Claim another) {
		return this.getStartDate().compareTo(another.getStartDate());
	}
	
	// FIXME Does not work
	public ArrayList<Money> getExpenseSummary() {
		ArrayList<Money> results = new ArrayList<Money>();
		Map<Currency, Money> sums = new HashMap<Currency, Money>();
		// Iterate over own expenses and sum.
		for (Expense e : expenses) {
			Money value = e.getValue();
			Currency key = value.getCurrencyType();
			// Add Moneys.
			if (sums.containsKey(key)) {
				sums.put(key, value.add(sums.get(key)));
			} else {
				sums.put(key, value);
			}
		}
		
		for (Currency key : sums.keySet()) {
			results.add(sums.get(key));
		}
		
		return results;
	}
	
	public int getExpenseCount() {
		return expenses.size();
	}

	public ArrayList<Expense> getExpenseList() {
		// TODO Auto-generated method stub
		return expenses;
	}

	@Override
	public String getText() {
		return name;
	}

	@Override
	public String getSubText() {
		// TODO Auto-generated method stub
		SimpleDateFormat df = ExpenseMasterApplication.global_date_format;
		if (end_date != null) {
			return "Status: " + status +
			"\nStart Date: " + df.format(start_date) +
			"\nEnd Date: " + df.format(end_date);
		} else {
			return "Status: " + status +
			"\nStart Date: " + df.format(start_date);
		}
	}
}



