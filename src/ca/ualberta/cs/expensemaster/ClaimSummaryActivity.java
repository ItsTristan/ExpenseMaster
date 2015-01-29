package ca.ualberta.cs.expensemaster;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ClaimSummaryActivity extends Activity implements EMView {
	private int position = -2;
	private Claim claim;
	
	private TextView claim_name;
	private TextView claim_date;
	private Spinner claim_status;
	private ListView expenses_summary;
	
	private ArrayAdapter<Money> adapter;
	private ArrayAdapter<ClaimStatus> spinner_adapter;
		
	private static ArrayList<Money> expense_summary;
	
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

				// On tap, open claim for editing
        		Intent intent = new Intent(ClaimSummaryActivity.this, EditClaimActivity.class);
        		
                // Pass list index through intent
        		intent.putExtra("position", getIntent().getIntExtra("position", -2));
        		
        		// Activity is responsible for the update
        		startActivityForResult(intent, RequestCode.REQUEST_EDIT_CLAIM);
            }
        });
        

		claim_name = (TextView) findViewById(R.id.claim_name_text);
		claim_date = (TextView) findViewById(R.id.claim_date_text);
		expenses_summary = (ListView) findViewById(R.id.expense_summary_list);

		claim_status = (Spinner) findViewById(R.id.status_spinner);
		claim_status.setOnItemSelectedListener(new OnItemSelectedListener() { 
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			    ClaimStatus status = (ClaimStatus) parent.getItemAtPosition(pos);
			
                Toast.makeText(ClaimSummaryActivity.this,
                        status.toString(), Toast.LENGTH_SHORT)
                        .show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		position = getIntent().getIntExtra("position", -2);
		claim = ExpenseMasterApplication.getClaim(position);
        claim.addView(this);
        expense_summary = claim.getExpenseSummary();;
		
		// Associate spinner with claim status enum
		spinner_adapter = new ArrayAdapter<ClaimStatus>(this, android.R.layout.simple_spinner_item, ClaimStatus.values());
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		claim_status.setAdapter(adapter);
		
		// Adapter updates to match money summary.
		adapter = new ArrayAdapter<Money>(this, R.layout.list_item, 
				expense_summary);
		expenses_summary.setAdapter(adapter);
		
		
		updateTextViews();
		updateSummaryList();
	}

	@Override
	public void update(EMModel model) {
		if (model instanceof Claim) {
			// Safe downcast
			update((Claim) model);
			return;
		}
	}

	public void update(Claim model) {
		updateTextViews();
		updateSummaryList();		
		
		// Update ListView
		adapter.notifyDataSetChanged();
	}
	
	private void updateTextViews() {
		// Update TextViews
		claim_name.setText(claim.getName());
//		claim_status.setText(claim.getStatus().toString());
		claim_date.setText(claim.getDateString());
	}
	
	private void updateSummaryList() {
		// Update summary array first
		expense_summary = claim.getExpenseSummary();
		adapter.notifyDataSetChanged();
	}

}
