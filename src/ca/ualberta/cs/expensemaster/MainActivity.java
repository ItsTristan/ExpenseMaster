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

public class MainActivity extends Activity {
	
	private ArrayAdapter<Claim> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// == Add Claim Button ==
		Button newClaim = (Button) findViewById(R.id.add_claim_button);
        newClaim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	// Make a request for a new claim through an activity
        		Intent intent = new Intent(MainActivity.this, EditClaimActivity.class);
        		startActivityForResult(intent, RequestCode.REQUEST_NEW_CLAIM);
        		
            }
        });
        
        // == Claims List View ==
        ListView claims_list = (ListView) findViewById(R.id.claims_list_view);
        claims_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// On tap, open claim summary
        		Intent intent = new Intent(MainActivity.this, ClaimSummaryActivity.class);
        		
                // Pass list index through intent
        		intent.putExtra("position", position);

        		// Activity is responsible for the update
        		startActivityForResult(intent, RequestCode.REQUEST_CLAIM_SUMMARY);
			}
        });

        claims_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// On long click, delete
				// http://stackoverflow.com/questions/2115758/how-to-display-alert-dialog-in-android
				//  Creates the alert dialog and immediately discards after displaying
				
				// position is set to final so that the delegate doesn't
				// complain about position changing in the outer body.
				new AlertDialog.Builder(MainActivity.this)
			    	.setTitle("Delete entry")
			    	.setMessage("Are you sure you want to delete this entry?")
			    	.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			    		public void onClick(DialogInterface dialog, int which) { 
				    		// Delete claim
			            	ExpenseMasterApplication.deleteClaim(MainActivity.this, position);
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
				
				// Consume the long click
				return true;
			}
        });
        
        
		adapter = new ArrayAdapter<Claim>(this, R.layout.list_item, 
				ExpenseMasterApplication.getClaims(MainActivity.this));

		// XXX: claims_list is unsorted.
        claims_list.setAdapter(adapter);
        
        // On list view tap, goto ClaimSummaryActivity[index]
        // On list view hold tap, alert delete?
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
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
				Toast.makeText(this, "Action was canceled", Toast.LENGTH_SHORT).show();
				break;
			case RequestCode.REQUEST_CLAIM_SUMMARY:
				break;
			}
		}
		
		updateDisplay();
	}
	
	private void updateDisplay() {
//		this.claims = ExpenseMasterApplication.getClaims();
		adapter.notifyDataSetChanged();
	}
	
	protected void onStart() {
		super.onStart();
		// TODO try read. If fail, set new.
        
	}
}
