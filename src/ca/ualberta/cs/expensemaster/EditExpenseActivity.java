package ca.ualberta.cs.expensemaster;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class EditExpenseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button save = (Button) findViewById(R.id.save_expense_button);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(EditExpenseActivity.this,
                        "save; goto EditClaimActivity", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        
		Button cancel = (Button) findViewById(R.id.cancel_expense_button);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(EditExpenseActivity.this,
                        "cancel; goto EditClaimActivity", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        
        
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}


}
