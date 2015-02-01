package ca.ualberta.cs.expensemaster;

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
	private static int claim_position;

    EditText claim_name;
    EditText claim_start_date;
    EditText claim_end_date;
    
    ListView expense_list;
    SubTextAdapter<Expense> adapter;
	
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
        		if (saveClaim())
        			finish();
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
							updateViews();
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
        
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		claim_position = getIntent().getIntExtra("claim_position", -1);
		if (claim_position == -1) {
			// Did not get a position to edit. Will create new claim.
			claim = new Claim();
		} else {
			// Request was to edit existing claim
			claim = ExpenseMasterApplication.getClaim(claim_position); 
		}

        // Expense list view and adapter
		adapter = new SubTextAdapter<Expense>(this, R.layout.list_item,
				claim.getExpenseList());
		expense_list.setAdapter(adapter);
	}

	protected void onResume() {
		super.onResume();
		updateViews();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// Save claim on OK
			switch (requestCode) {
			case RequestCode.REQUEST_NEW_EXPENSE:
			case RequestCode.REQUEST_EDIT_EXPENSE:
				saveClaim();
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

	private String getClaimName() {
		return claim_name.getText().toString().trim();
	}
	
	private Date getClaimStartDate() {
		SimpleDateFormat df = ExpenseMasterApplication.global_date_format;
		
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
			Toast.makeText(EditClaimActivity.this,
					"Name cannot be left blank.",
					Toast.LENGTH_SHORT).show();
			return false;
		// Require start date to be filled
		} else if (startDate == null) {
			Toast.makeText(EditClaimActivity.this,
					"Start Date cannot be left blank.",
					Toast.LENGTH_SHORT).show();
			return false;
			
		} else {
			// Update the claim now.
			claim.setName(name);
			claim.setStartDate(startDate);
			claim.setEndDate(endDate);
			
			// If saving as new, add to list and return.
			// The hosting activity is responsible for updating.
    		if (claim_position == -1) {
				Toast.makeText(EditClaimActivity.this, "Adding claim",
						Toast.LENGTH_SHORT).show();
				// Set the edit position to the one that's just been created.
				claim_position = ExpenseMasterApplication.addClaim(EditClaimActivity.this, claim);
				// Make sure the intent knows that from now on, we're editing
				// at this position.
				getIntent().putExtra("claim_position", claim_position);
				
    		// If saving an old, update entry and return.
    		} else {
				Toast.makeText(EditClaimActivity.this, "Updating claim " +
						claim_position, Toast.LENGTH_SHORT).show();
    			ExpenseMasterApplication.updateClaim(EditClaimActivity.this, claim_position, claim);
    		}
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// Cancel without saving
		super.onBackPressed();
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
	}
	
	public void updateViews() {
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

