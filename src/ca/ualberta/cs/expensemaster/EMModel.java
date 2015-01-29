package ca.ualberta.cs.expensemaster;

import java.util.ArrayList;

public abstract class EMModel {
	private final ArrayList<EMView> views;
	
	public EMModel() {
		views = new ArrayList<EMView>();
	}

	public void notifyViews() {
		for (EMView view : views) {
			view.update(this);
		}
	}
	
	public void addView(EMView view) {
		if (! views.contains(view))
			views.add(view);
	}
	
	public void deleteView(EMView view) {
		views.remove(view);
	}
}
