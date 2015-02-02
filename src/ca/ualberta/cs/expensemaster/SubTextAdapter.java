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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * A prettier looking array adapter that allows subtext in listviews.
 * This class requires an object T that implements the SubtextListable
 * interface. It puts the object's getText() call result to the 
 * main text and getSubText() to the text underneath. 
 *
 * Issues:
 * It might be better to use a {@link BaseAdapter} instead of an
 * {@link ArrayAdapter} because it reduces the amount of work
 * we need to do. For now, an ArrayAdapter is fine.
 *  
 *  Heavily adapted from
 *  http://www.softwarepassion.com/android-series-custom-listview-items-and-adapters/
 *    Jan 31, 2015
 *  It might be better to extend a BaseAdapter here, but for straight-forward
 *  substitution reasons, I'm extending ArrayAdapter here.
 *
 * @author ItsTristan (Tristan Meleshko)
 * @param <T> 
 */
public class SubTextAdapter<T extends SubtextListable> extends ArrayAdapter<T> {
	private ArrayList<T> items;
	/**
	 * ViewHolder patterns are more efficient then calling findById
	 * for every list item every time we want to update the list.
	 */
	private static class ViewHolder {
	    public final TextView top_text;
	    public final TextView bottom_text;
	    
		public ViewHolder(TextView top_text, TextView bottom_text) {
			super();
			this.top_text = top_text;
			this.bottom_text = bottom_text;
		}
	}
	
	public SubTextAdapter(Context context, int textViewResourceId, ArrayList<T> items) {
		// Basically doing the same thing as before, but keeping a local copy
		// of items so we can reference it here
        super(context, textViewResourceId, items);
        this.items = items;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		TextView top_text;
		TextView bottom_text;

		// Get the item we want to display
        T item = items.get(position);
        
        if (convertView == null) {
        	// Get a new view from the inflater
        	convertView = LayoutInflater.from(getContext()).inflate(
        			R.layout.list_item_subtext, parent, false);
        	// Fetch our resources...
	        top_text = (TextView) convertView.findViewById(R.id.subtext_text);
	        bottom_text = (TextView) convertView.findViewById(R.id.subtext_subtext);
	        // ...remember them so we don't have to re-fetch
	        convertView.setTag(new ViewHolder(top_text, bottom_text));
        } else {
        	// This view was recycled, so it should still have our ViewHolder
        	ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        	top_text = viewHolder.top_text;
        	bottom_text = viewHolder.bottom_text;
        }
        // Make sure we're not displaying null things for some reason
        if (item != null) {
        	// Put out data in the views (Yay!)
	        top_text.setText(item.getText());
	        bottom_text.setText(item.getSubText());
        }
        return convertView;
    }
}