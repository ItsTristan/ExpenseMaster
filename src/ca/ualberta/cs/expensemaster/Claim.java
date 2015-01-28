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

	/*
	 == Old. From Parcelable
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	// http://developer.android.com/reference/android/os/Parcelable.html
	// Jan 27, 2015
    public static final Parcelable.Creator<Claim> CREATOR
	    = new Parcelable.Creator<Claim>() {
		public Claim createFromParcel(Parcel in) {
		    return new Claim(in);
		}
		
		public Claim[] newArray(int size) {
		    return new Claim[size];
		}
	};
	
	public Claim(Parcel in) {
		this.name = in.readString();
		this.status = (ClaimStatus) in.readSerializable();
		this.start_date = new Date(in.readLong());
		this.end_date = new Date(in.readLong());
		
		if (this.end_date.getTime() == 0) {
			this.end_date = null;
		}
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeSerializable(status);
		// http://stackoverflow.com/questions/21017404/reading-and-writing-java-util-date-from-parcelable-class
		//  Jan 28, 2015
		// Write dates as longs to improve performance
		out.writeLong(start_date.getTime());
		if (end_date == null) {
			out.writeLong(0);
		} else {
			out.writeLong(end_date.getTime());
		}
	}
	*/
}
