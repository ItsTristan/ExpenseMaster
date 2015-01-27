package ca.ualberta.cs.expensemaster;

import java.util.ArrayList;

public class Claim extends EMModel {
	private String name;
	private ArrayList<Expense> expenses;

	public Claim(String name) {
		this.setName(name);
		expenses = new ArrayList<Expense>();
	}
	
	public void addExpense(Expense e) {
		if (! expenses.contains(e)) {
			expenses.add(e);
			updateViews();
		}
	}
	
	public void deleteExpense(Expense e) {
		if (expenses.contains(e)) {
			expenses.remove(e);
			updateViews();
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
		updateViews();
	}

}
