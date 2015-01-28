package ca.ualberta.cs.expensemaster;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ClaimSummaryActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_summary);
		
		Button editClaim = (Button) findViewById(R.id.edit_claim_button);
        editClaim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(ClaimSummaryActivity.this,
                        "edit_claim EditClaimActivity", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        
        
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}

}
