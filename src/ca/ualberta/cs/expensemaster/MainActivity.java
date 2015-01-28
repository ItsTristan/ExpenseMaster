package ca.ualberta.cs.expensemaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ClaimsList claims; 
	
	private static final String FILENAME = "save.dat";

	private static final int REQUEST_NEW_CLAIM = 1;
	private static final int REQUEST_EDIT_CLAIM = 2;
	
	private SimpleAdapter adapter;
	private ListView claims_list;

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
                // XXX startActivityForResult(intent, requestCode);
                // TODO: Open Activity. Use result to add claims
                // TODO: Pass new claim through messenger
                Claim new_claim = new Claim("sasdf");

        		Intent intent = new Intent(MainActivity.this, EditClaimActivity.class);
                // Pass claim through activity as parcel
        		intent.putExtra("claim", new_claim);
        		startActivityForResult(intent, REQUEST_NEW_CLAIM);
        		        		
        		claims.addClaim(new_claim);
        		
        		adapter.notifyDataSetChanged();
            }
        });
        
        claims_list = (ListView) findViewById(R.id.claims_list_view);
        claims_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

        		Intent intent = new Intent(MainActivity.this, EditClaimActivity.class);
        		
                // Pass claim through activity as parcel
        		intent.putExtra("claim", claims.getClaim(position));
        		startActivityForResult(intent, REQUEST_EDIT_CLAIM);
				
				Toast.makeText(MainActivity.this,
					      position + ": " + claims.getClaim(position), Toast.LENGTH_LONG)
					      .show();
			}
        });
        

		claims = new ClaimsList();
		
		// http://stackoverflow.com/questions/7916834/android-adding-listview-sub-item-text
		//  Jan 27, 2015
		adapter = new SimpleAdapter(this, claims.getData(),
				R.layout.claims_list_item, 
				new String[] {"title", "subtitle"},
				new int[] {R.id.title, R.id.subtitle});

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
		switch (request_code) {
		case REQUEST_NEW_CLAIM:
			break;
		case REQUEST_EDIT_CLAIM:
			break;
		default:
			throw new RuntimeException("Unknown request code");
		}
	}
	
	protected void onStart() {
		super.onStart();
		// TODO try read. If fail, set new.
        
	}
	
	private void loadData() {
		// TODO: Restore data
	}
	
	private void saveData() {
		// TODO: Store status
	}

}
