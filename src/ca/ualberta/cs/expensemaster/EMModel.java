/* This file is part of the ExpenseMaster Android Application
 * See github.com/ItsTristan/ExpenseMaster for more information
 * 
 * Copyright (C) 2015 Tristan Meleshko
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA. */

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
