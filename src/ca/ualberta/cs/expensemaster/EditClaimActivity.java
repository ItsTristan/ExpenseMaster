package ca.ualberta.cs.expensemaster;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditClaimActivity extends Activity {
	private transient Claim claim;
	private transient int edit_position;

    EditText claim_name;
    TextView claim_start_date;
    TextView claim_end_date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);

		Button addExpense = (Button) findViewById(R.id.add_expense_button);
        addExpense.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(EditClaimActivity.this,
                        "edit_expense EditExpenseActivity", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        
		Button save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
        		Intent resultIntent = new Intent();
        		resultIntent.putExtra("position", edit_position);
        		
        		setResult(Activity.RESULT_OK, resultIntent);
        		
        		claim.setName(claim_name.getText().toString().trim());
        		// Require name must be filled.
        		if (claim.getName() == null || claim.getName().isEmpty()) {
    				Toast.makeText(EditClaimActivity.this,
    						"Name field cannot be left blank.",
    						Toast.LENGTH_SHORT).show();
    			// Require start date to be filled
        		} else if (claim.getStartDate() == null) {
    				Toast.makeText(EditClaimActivity.this,
    						"Start Date filed cannot be left blank.",
    						Toast.LENGTH_SHORT).show();
        		} else {
        			// If saving as new, add to list and return.
        			// The hosting activity is responsible for updating.
	        		if (edit_position == -1) {
	    				Toast.makeText(EditClaimActivity.this, "Adding claim",
	    						Toast.LENGTH_SHORT).show();
	        			ExpenseMasterApplication.addClaim(claim);
	        		// If saving an old, update entry and return.
	        		} else {
	    				Toast.makeText(EditClaimActivity.this, "Updating claim " +
	    						edit_position, Toast.LENGTH_SHORT).show();
	        			ExpenseMasterApplication.updateClaim(edit_position, claim);
	        		}
	        		
	        		finish();
        		}
            }
        });
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		edit_position = getIntent().getIntExtra("position", -1);
		if (edit_position == -1) {
			// Did not get a position to edit. Will create new claim.
			claim = new Claim();
		} else {
			// Request was to edit existing claim
			claim = ExpenseMasterApplication.getClaim(edit_position); 
		}

        claim_name = (EditText) findViewById(R.id.claim_name_text);
        claim_start_date = (TextView) findViewById(R.id.claim_start_date);
        claim_end_date = (TextView) findViewById(R.id.claim_end_date);
        
        claim_name.setText(claim.getName());
		SimpleDateFormat df = new SimpleDateFormat("yyyy/mm/dd", Locale.CANADA);
        claim_start_date.setText(df.format(claim.getStartDate()));   
        if (claim.getEndDate() == null) {
        	claim_end_date.setText("");
        } else {
        	claim_end_date.setText(df.format(claim.getEndDate()));
        }
        
	}
	
	@Override
	public void onBackPressed() {
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
	}

}

