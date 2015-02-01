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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements EMView<ClaimsList> {
	
	private ArrayAdapter<Claim> adapter;
	private ListView claims_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// == Add Claim Button ==
		Button newClaim;
		newClaim = (Button) findViewById(R.id.add_claim_button);
        newClaim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	// Make a request for a new claim through an activity
        		Intent intent = new Intent(MainActivity.this, EditClaimActivity.class);
        		// Does not pass a list position because it's a new claim.
        		startActivityForResult(intent, RequestCode.REQUEST_NEW_CLAIM);
        		
            }
        });
        
        // == Claims List (Tap) ==
        claims_list = (ListView) findViewById(R.id.claims_list_view);
        claims_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// On tap, open claim summary
        		Intent intent = new Intent(MainActivity.this, ClaimSummaryActivity.class);
        		
                // Pass list index through intent
        		intent.putExtra("claim_position", position);
				Toast.makeText(MainActivity.this, "position: " + position, Toast.LENGTH_SHORT).show();

        		// Activity is responsible for the update
        		startActivityForResult(intent, RequestCode.REQUEST_CLAIM_SUMMARY);
			}
        });
        
        // == Claims List (Long Press) ==
        claims_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// position is set to final so that the delegate doesn't
				// complain about position changing in the outer body.
				// FIXME The Id from claims_list isn't pointing to the right
				//		 object. Because of this, we have to ensure that
				//		 the list in the adapter is sync'd with the one in
				//		 the application at all times.
				Claim c = ExpenseMasterApplication.getClaim(position);
				
				// If APPROVED or SUBMITTED, don't allow delete
				if (c.getStatus() == ClaimStatus.APPROVED || c.getStatus() == ClaimStatus.SUBMITTED) {
					// Notify and don't consume the hold so it can be edited.
					Toast.makeText(MainActivity.this, "Claim cannot be deleted (already " + 
							c.getStatus().toString() +")", Toast.LENGTH_SHORT).show();
					return false;
				} else {
					// Display alert for delete
					// http://stackoverflow.com/questions/2115758/how-to-display-alert-dialog-in-android
					//  Creates the alert dialog and immediately discards after displaying
					new AlertDialog.Builder(MainActivity.this)
				    	.setTitle("Delete entry")
				    	.setMessage("Are you sure you want to delete this entry?")
				    	.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				    		public void onClick(DialogInterface dialog, int which) { 
					    		// Delete claim using position (see note above)
				            	ExpenseMasterApplication.deleteClaim(MainActivity.this, 
				            			position);
								updateDisplay();
				    		}
				    	})
				    	.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				    		public void onClick(DialogInterface dialog, int which) { 
				    			// do nothing
				    		}
				    	})
				    	.setIcon(android.R.drawable.ic_dialog_alert)
				    	.show();
				}
				
				// Consume the long click
				return true;
			}
        });
        
		adapter = new SubTextAdapter<Claim>(this, R.layout.list_item, 
				ExpenseMasterApplication.getClaimsList(this));

        if (adapter == null) {
        	throw new RuntimeException("adapter list not initialized");
        }
        if (claims_list == null) {
        	throw new RuntimeException("claims list not initialized");
        }
        if (ExpenseMasterApplication.getClaimsList(this) == null) {
        	throw new RuntimeException("Class failed");
        }

        claims_list.setAdapter(adapter);

        // Receive updates from claims
        ExpenseMasterApplication.addView(this);     
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		updateDisplay();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	// XXX This probably isn't necessary anymore.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// Do things if result OK
			switch (requestCode) {
			case RequestCode.REQUEST_NEW_CLAIM:
	    		break;
			case RequestCode.REQUEST_EDIT_CLAIM:
				break;
				
			default:
				throw new RuntimeException("Unknown request code");
			}
		} else if (resultCode == RESULT_CANCELED) {
			switch (requestCode) {
			case RequestCode.REQUEST_EDIT_CLAIM:
			case RequestCode.REQUEST_NEW_CLAIM:
				// Action was canceled. Don't add.
				break;
			case RequestCode.REQUEST_CLAIM_SUMMARY:
				break;
			}
		}
	}
	
	private void updateDisplay() {
		// Ensure claims are sorted when we display
		// Java uses a TimSort so there shouldn't be a big
		// performance hit.
		adapter.notifyDataSetChanged();
	}

	@Override
	public void update(ClaimsList model) {
		// TODO Auto-generated method stub
		updateDisplay();
	}
}
