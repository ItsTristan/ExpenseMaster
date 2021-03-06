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

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Activity to display a summary of a claim, allows
 * editing of a claim, and emailing of an individual claim.
 * 
 * @author ItsTristan (Tristan Meleshko)
 *
 */
public class ClaimSummaryActivity extends Activity implements EMView<Claim> {
	private int position = -2;
	private Claim claim;
	
	private TextView claim_name;
	private TextView claim_date;
	private Spinner claim_status;
	private ListView expenses_summary_list;
	private Button edit_claim;
	
	private boolean isFirstSelection;
	
	private ArrayAdapter<Money> adapter;
	private ArrayAdapter<ClaimStatus> spinner_adapter;
		
	private static ArrayList<Money> expense_summary;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_summary);
        
		claim_name = (TextView) findViewById(R.id.claim_name_text);
		claim_date = (TextView) findViewById(R.id.claim_date_text);
		expenses_summary_list = (ListView) findViewById(R.id.expense_summary_list);

		// == Edit Claim ==
		// Flag for skipping saves on spinner initialization
		isFirstSelection = true;
		edit_claim = (Button) findViewById(R.id.edit_claim_button);
        edit_claim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
				// On tap, open claim for editing
        		Intent intent = new Intent(ClaimSummaryActivity.this, EditClaimActivity.class);
        		
                // Pass list index through intent
        		intent.putExtra("claim_position", getIntent().getIntExtra("claim_position", -2));
        		
        		// Activity is responsible for the update
        		startActivityForResult(intent, RequestCode.REQUEST_EDIT_CLAIM);
        		
        		// Close this activity on Edit so they return back to the main menu,
        		finish();
            }
        });
        
        // == Send Email ==
        Button send_email = (Button) findViewById(R.id.email_claim);
        send_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Build an email for this claim and open the email client.
				sendEmail();
			}
		});

        // == Status Spinner ==
		claim_status = (Spinner) findViewById(R.id.status_spinner);
		claim_status.setOnItemSelectedListener(new OnItemSelectedListener() { 
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				// Don't do work if we aren't done initializing the Activity yet.
				// (first selection is always the default selector)
				if (isFirstSelection) {
					isFirstSelection = false;
					return;
				}
				
				// Fetch status value from view
			    ClaimStatus status = (ClaimStatus) parent.getItemAtPosition(pos);
			    
			    // Save status immediately
			    claim.setStatus(status);
			    // Inform Application that the update occurred so it can be saved.
			    ExpenseMasterApplication.updateClaim(ClaimSummaryActivity.this,
			    		position, claim);

			    // Change views on this page to enable/disable as necessary.
			    updateStatusViews();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Shouldn't happen
				throw new RuntimeException("Spinner selected nothing");
			}

		});
        
	}

	@Override
	protected void onStart() {
		super.onStart();
		position = getIntent().getIntExtra("claim_position", -1);
		// Throws a runtime error if position is invalid.
		// We should never get a claim position of -1, since that
		// would imply we came here from somewhere other than 
		// a list view (or anything else with an index)
		claim = ExpenseMasterApplication.getClaim(position);
        claim.addView(this);
		
		// Associate spinner with claim status enum
		spinner_adapter = new ArrayAdapter<ClaimStatus>(this, android.R.layout.simple_spinner_item, ClaimStatus.values());
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		claim_status.setAdapter(spinner_adapter);
		
		// Adapter updates to match money summary.
		expense_summary = claim.getExpenseSummary();
		adapter = new ArrayAdapter<Money>(this, R.layout.list_item, 
				expense_summary);
		expenses_summary_list.setAdapter(adapter);

		update(claim);

	}

	public void update(Claim model) {
		updateTextViews();
		updateSummaryList();		
		updateStatusViews();
	}
	
	private void updateTextViews() {
		// Update TextViews
		claim_name.setText(claim.getName());
		claim_date.setText(claim.getDateString());
	}
	
	private void updateSummaryList() {
		// Force update of array
		expense_summary = claim.getExpenseSummary();
		adapter.notifyDataSetChanged();
	}
	
	private void updateStatusViews() {
		// Match spinner status to current status
		claim_status.setSelection(
				spinner_adapter.getPosition(claim.getStatus()));
		// Update buttons to match the status
		edit_claim.setEnabled(claim.getStatus() == ClaimStatus.IN_PROGRESS ||
							claim.getStatus() == ClaimStatus.RETURNED);
		spinner_adapter.notifyDataSetChanged();
	}
	
	private void sendEmail() {
		// HTML Email doesn't work very well in Android.
		// Because of this, this function outputs markdown instead.
		EmailMessage em = new MarkdownEmail("", "Emailing Claim: '"+claim.getName()+"'");
		try {
			em.writeClaim(claim);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.close();
		// Send the email
		em.send(this);
	}

}
