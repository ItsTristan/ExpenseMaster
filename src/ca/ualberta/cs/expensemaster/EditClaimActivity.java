package ca.ualberta.cs.expensemaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class EditClaimActivity extends Activity {
	
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
		
	}
	
	@Override
	public void onBackPressed() {
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

}

