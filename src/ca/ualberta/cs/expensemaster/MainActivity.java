package ca.ualberta.cs.expensemaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@SuppressWarnings("unused")
public class MainActivity extends Activity {
	private ClaimsList claims; 
	
	private static final String FILENAME = "save.dat";
	
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
                // TODO: Open Activity. Use result to add claims
        		claims.addClaim(new Claim("Test"));
        		adapter.notifyDataSetChanged();
            }
        });
        
        claims_list = (ListView) findViewById(R.id.claims_list_view);
        
        // On list view tap, goto ClaimSummaryActivity[index]
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected void onStart() {
		super.onStart();
		// TODO try read. If fail, set new.
		claims = new ClaimsList();
		
		// http://stackoverflow.com/questions/7916834/android-adding-listview-sub-item-text
		//  Jan 27, 2015
		adapter = new SimpleAdapter(this, claims.getData(),
				R.layout.claims_list_item, 
				new String[] {"title", "subtitle"},
				new int[] {R.id.title, R.id.subtitle});

        claims_list.setAdapter(adapter);
        
	}
	
	private void loadData() {
		// TODO: Restore data
	}
	
	private void saveData() {
		// TODO: Store status
	}

}
