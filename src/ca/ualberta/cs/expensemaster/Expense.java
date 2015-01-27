package ca.ualberta.cs.expensemaster;

public class Expense extends EMModel {
	private String name;
	private Money value;

	public Expense(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		updateViews();
	}

	public Money getValue() {
		return value;
	}

	public void setValue(Money value) {
		this.value = value;
	}

}
