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
	private Claim claim;
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
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		claim = getIntent().getParcelableExtra("claim");
		if (claim == null) {
			throw new RuntimeException("Did not receive a claim.");
		}

        EditText claim_name = (EditText) findViewById(R.id.claim_name_text);
        TextView claim_start_date = (TextView) findViewById(R.id.claim_start_date);
        TextView claim_end_date = (TextView) findViewById(R.id.claim_end_date);
        
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
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

}

