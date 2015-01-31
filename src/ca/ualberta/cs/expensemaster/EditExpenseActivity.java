package ca.ualberta.cs.expensemaster;

import java.util.Currency;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class EditExpenseActivity extends Activity {
	Claim claim;	// Claim to be edited

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_expense);

		// == Save ==
		Button save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(EditExpenseActivity.this,
                        "save; goto EditClaimActivity", Toast.LENGTH_SHORT)
                        .show();
                saveExpense();
                setResult(RESULT_OK);
                finish();
            }
        });
        
        Spinner currencies = (Spinner) findViewById(R.id.currency_type_spinner);
        
        
        // Currency
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	public void onBackPressed() {
		// Cancel without saving
		super.onBackPressed();
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
	}

	private void saveExpense() {
		String name;
		Date date;
	}
	
}
