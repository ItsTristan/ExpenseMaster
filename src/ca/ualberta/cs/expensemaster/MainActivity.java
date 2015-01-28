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
	
	private ArrayAdapter<Claim> adapter;
	
	private ArrayList<Claim> claims;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button newClaim = (Button) findViewById(R.id.add_claim_button);
        newClaim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this,
                        "new_claim EditClaimActivity", Toast.LENGTH_SHORT)
                        .show();
                // TODO: Pass new claim through messenger

        		Intent intent = new Intent(MainActivity.this, EditClaimActivity.class);
                // Pass claim through activity as parcel
        		startActivityForResult(intent, REQUEST_NEW_CLAIM);
        		
            }
        });
        
        claims = ExpenseMasterApplication.getClaims();
        
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
				ExpenseMasterApplication.getClaims());

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
			switch (request_code) {
			case REQUEST_NEW_CLAIM:
		        Claim new_claim = new Claim("sasdf");
				claims.add(new_claim);
				// Fall through
			case REQUEST_EDIT_CLAIM:
	    		adapter.notifyDataSetChanged();
				break;
				
				
			default:
				throw new RuntimeException("Unknown request code");
			}
		} else {
			Toast.makeText(this, "Action was canceled", Toast.LENGTH_SHORT).show();
		}
	}
	
	protected void onStart() {
		super.onStart();
		// TODO try read. If fail, set new.
        
	}
}
