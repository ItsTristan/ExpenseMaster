package ca.ualberta.cs.expensemaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Claim extends EMModel implements SubTitleable, Comparable<Claim> {
	
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

	public void addExpense(Expense e) {
		if (! expenses.contains(e)) {
			expenses.add(e);
			notifyViews();
		}
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
		return getName();
	}

	@Override
	public String getTitle() {
		return getName();
	}

	@Override
	public String getSubTitle() {
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy/mm/dd", Locale.CANADA);
		if (end_date == null) {
			return status + "\n" + date_format.format(start_date);
		} else {
			return status + "\n" +
				date_format.format(start_date) + " - " + date_format.format(end_date);
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
}
