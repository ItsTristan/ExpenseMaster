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

/**
 * This activity allows editing of an individual claim.
 * It must receive a claim index (relative to ClaimsList)
 * to edit, or -1 for a new claim.
 * 
 * Issues:
 * Currently, there are problems with having data display
 * when the screen locks as well as being able to save
 * correctly. The current (minor) hack fixes both issues
 * for now.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class EditClaimActivity extends Activity {
	private static Claim claim;
	private int claim_position;

	// These are static so it's not forgotten if the activity closes.
    private static EditText claim_name;
    private static EditText claim_start_date;
    private static EditText claim_end_date;
    
    private static ListView expense_list;
    SubTextAdapter<Expense> adapter;
	
    boolean needs_save = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);

		// == Add Expense ==
		Button addExpense = (Button) findViewById(R.id.add_expense_button);
        addExpense.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	if (saveClaim()) {
	                // Make a request for a new expense
	        		Intent intent = new Intent(EditClaimActivity.this, EditExpenseActivity.class);
	        		// -1 indicates not in list.
	        		intent.putExtra("claim_position", claim_position);
	        		intent.putExtra("exp_position", -1);
	        		startActivityForResult(intent, RequestCode.REQUEST_NEW_EXPENSE);
            	}
            }
        });
		
        // == Save Button ==
		Button save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View arg0) {
        		Intent resultIntent = new Intent();
        		resultIntent.putExtra("claim_position", claim_position);
        		
        		setResult(Activity.RESULT_OK, resultIntent);
        		
        		// Save and finish
        		if (saveClaim()) {
        			// Clean up so we get a new copy next time.
        			claim = null;
        			finish();
        		}
        	};
        });

        // == Expense List View (Tap)
        // Copied from MainActivity
		expense_list = (ListView) findViewById(R.id.expense_list);
        expense_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// On tap, edit expense
        		Intent intent = new Intent(EditClaimActivity.this, EditExpenseActivity.class);
        		
                // Pass list index through intent
        		intent.putExtra("claim_position", claim_position);
        		intent.putExtra("expense_position", position);

        		// Activity is responsible for the update
        		startActivityForResult(intent, RequestCode.REQUEST_EDIT_EXPENSE);
			}
        });
        
        // == Expense List View (Long Press)
        // Copied from MainActivity
        expense_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// position is set to final so that the delegate doesn't
				// complain about position changing in the outer body.
				// ** Note: the adapter get item here doesn't work in 
				//			MainActivity and I don't know why.
				
				// Display alert for delete
				// http://stackoverflow.com/questions/2115758/how-to-display-alert-dialog-in-android
				//  Creates the alert dialog and immediately discards after displaying
				new AlertDialog.Builder(EditClaimActivity.this)
			    	.setTitle("Delete entry")
			    	.setMessage("Are you sure you want to delete this entry?")
			    	.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			    		public void onClick(DialogInterface dialog, int which) { 
				    		// Delete claim
			            	claim.deleteExpense(adapter.getItem(position));
			            	// XXX Does this need to be here?
							setupViews();
			    		}
			    	})
			    	.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			    		public void onClick(DialogInterface dialog, int which) { 
			    			// do nothing
			    		}
			    	})
			    	.setIcon(android.R.drawable.ic_dialog_alert)
			    	.show();
				
				// Consume the long click
				return true;
			}
        });

		// Not initialized yet
		// We can't do the same thing as in Expenses because
		// Expenses is the topmost activity.
		if (claim == null) {
			claim_position = getIntent().getIntExtra("claim_position", -1);
			if (claim_position == -1) {
				// Did not get a position to edit. Will create new claim.
				claim = new Claim();
			} else {
				// Request was to edit existing claim
				claim = ExpenseMasterApplication.getClaim(claim_position); 
			}
		}

        // Expense list view and adapter
		adapter = new SubTextAdapter<Expense>(this, R.layout.list_item,
				claim.getExpenseList());
		expense_list.setAdapter(adapter);
		
		// Initialize the views with claim data
		setupViews();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	protected void onResume() {
		super.onResume();
		if (needs_save) {
			saveClaim();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// Save claim on OK
			switch (requestCode) {
			case RequestCode.REQUEST_NEW_EXPENSE:
			case RequestCode.REQUEST_EDIT_EXPENSE:
				// We can't save it here because onActivityResult is
				// called before onResume.
				needs_save = true;
				break;
				
			default:
				throw new RuntimeException("Unknown request code");
			}
		} else if (resultCode == RESULT_CANCELED) {
			switch (requestCode) {
			case RequestCode.REQUEST_EDIT_CLAIM:
			case RequestCode.REQUEST_NEW_CLAIM:
				break;
			case RequestCode.REQUEST_CLAIM_SUMMARY:
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		// Cancel without saving
		super.onBackPressed();
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		// Clean up so we get a new copy next time.
		claim = null;
		finish();
	}

	private String getClaimName() {
		if (claim_name.getText().toString().isEmpty()) {
			Toast.makeText(EditClaimActivity.this,
					"Name cannot be left blank.",
					Toast.LENGTH_SHORT).show();
			return null;
		}
		return claim_name.getText().toString().trim();
	}
	
	private Date getClaimStartDate() {
		SimpleDateFormat df = ExpenseMasterApplication.global_date_format;

		if (claim_start_date.getText().toString().isEmpty()) {
			Toast.makeText(EditClaimActivity.this,
					"Start Date cannot be left blank.",
					Toast.LENGTH_SHORT).show();
			return null;
		}
		// Try to convert start date to real date using default format
		try {
			return df.parse(claim_start_date.getText().toString());
		} catch (ParseException e) {
			// Stop if invalid parsing
			Toast.makeText(EditClaimActivity.this, 
					"Start date must be in format " + df.toPattern(),
					Toast.LENGTH_LONG).show();
			return null;
		}
	}
	private Date getClaimEndDate() {
		SimpleDateFormat df = ExpenseMasterApplication.global_date_format;
		
		// Try to convert end date to real date, if data entered
		try {
			String end_date = claim_end_date.getText().toString();
			if (end_date == null || end_date.isEmpty()) {
				return null;
			} else {
				return df.parse(claim_end_date.getText().toString());
			}
		} catch (ParseException e) {
			// Stop on exception
			Toast.makeText(EditClaimActivity.this, 
					"End date must be in format "  + df.toPattern() + " or blank",
					Toast.LENGTH_LONG).show();
			return null;
		}
	}
	
	
	/**
	 * Saves the claim safely, warning users of problems
	 * @param name
	 * @param startDate
	 * @param endDate
	 * @return Returns whether save was successful
	 */
	private boolean saveClaim() {
		String name = getClaimName();
		Date startDate = getClaimStartDate();
		Date endDate = getClaimEndDate();
		
		// Require name must be filled.
		if (name == null || name.isEmpty()) {
			return false;
		// Require start date to be filled
		} else if (startDate == null) {
			return false;
			
		} else {
			// Update the claim now.
			claim.setName(name);
			claim.setStartDate(startDate);
			claim.setEndDate(endDate);
			
			// If saving as new, add to list and return.
			// The hosting activity is responsible for updating.
    		if (claim_position == -1) {
				// Set the edit position to the one that's just been created.
				claim_position = ExpenseMasterApplication.addClaim(EditClaimActivity.this, claim);
				// Make sure the intent knows that from now on, we're editing
				// at this position.
				getIntent().putExtra("claim_position", claim_position);
				
    		// If saving an old, update entry and return.
    		} else {
    			ExpenseMasterApplication.updateClaim(EditClaimActivity.this, claim_position, claim);
    		}
		}
		
		// Update views to match
		setupViews();
		needs_save = false;
		return true;
	}
	
	private void setupViews() {
		// Get the views in question
        claim_name = (EditText) findViewById(R.id.claim_name_text);
        claim_start_date = (EditText) findViewById(R.id.claim_start_date);
        claim_end_date = (EditText) findViewById(R.id.claim_end_date);
        
        // Set their values
        claim_name.setText(claim.getName());
		SimpleDateFormat df = ExpenseMasterApplication.global_date_format;
		
        claim_start_date.setText(df.format(claim.getStartDate()));   
        if (claim.getEndDate() == null) {
        	claim_end_date.setText("");
        } else {
        	claim_end_date.setText(df.format(claim.getEndDate()));
        }
        
        // update the list view
        adapter.notifyDataSetChanged();
        
	}

}

