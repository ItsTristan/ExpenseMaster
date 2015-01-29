package ca.ualberta.cs.expensemaster;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final int REQUEST_NEW_CLAIM = 1;
	private static final int REQUEST_EDIT_CLAIM = 2;
	private static final int REQUEST_CLAIM_SUMMARY = 3;
	
	private ArrayAdapter<Claim> adapter;
	
	private ArrayList<Claim> claims;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        claims = ExpenseMasterApplication.getClaims();
		
		// == Add Claim Button ==
		Button newClaim = (Button) findViewById(R.id.add_claim_button);
        newClaim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this,
                        "new_claim EditClaimActivity", Toast.LENGTH_SHORT)
                        .show();

        		Intent intent = new Intent(MainActivity.this, EditClaimActivity.class);
                // Pass claim through activity as parcel
        		startActivityForResult(intent, REQUEST_NEW_CLAIM);
        		
            }
        });
        
        // == Claims List View ==
        ListView claims_list = (ListView) findViewById(R.id.claims_list_view);
        claims_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// XXX This should go to the Claim Summary page
        		Intent intent = new Intent(MainActivity.this, EditClaimActivity.class);
        		
                // Pass list index through activity as parcel
        		intent.putExtra("position", position);
        		
        		startActivityForResult(intent, REQUEST_EDIT_CLAIM);
				
				Toast.makeText(MainActivity.this,
					      position + ": " + claims.get(position), Toast.LENGTH_LONG)
					      .show();
			}
        });
        
		adapter = new ArrayAdapter<Claim>(this, R.layout.claims_list_item, 
				claims);

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
	protected void onActivityResult(int request_code, int result_code, Intent data) {
		if (result_code == RESULT_OK) {
			// Do things if result OK
			switch (request_code) {
			case REQUEST_NEW_CLAIM:
	    		break;
			case REQUEST_EDIT_CLAIM:
				break;
				
				
			default:
				throw new RuntimeException("Unknown request code");
			}
		} else if (result_code == RESULT_CANCELED) {
			switch (request_code) {
			case REQUEST_EDIT_CLAIM:
			case REQUEST_NEW_CLAIM:
				Toast.makeText(this, "Action was canceled", Toast.LENGTH_SHORT).show();
				break;
			case REQUEST_CLAIM_SUMMARY:
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
