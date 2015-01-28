package ca.ualberta.cs.expensemaster;

import java.util.ArrayList;

public abstract class EMModel {
	private final ArrayList<EMView<EMModel>> views;
	
	public EMModel() {
		views = new ArrayList<EMView<EMModel>>();
	}

	public void notifyViews() {
		for (EMView<EMModel> view : views)
			view.update(this);
	}
	
	public void addView(EMView<EMModel> view) {
		if (! views.contains(view))
			views.add(view);
	}
	
	public void deleteView(EMView<EMModel> view) {
		if (views.contains(view))
			views.add(view);
	}
}
