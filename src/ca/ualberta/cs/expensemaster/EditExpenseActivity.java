package ca.ualberta.cs.expensemaster;

import java.text.ParseException;
import java.util.Currency;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditExpenseActivity extends Activity {
	private static Claim claim;	// Claim to be edited
	private static int claim_position; // Its position in the expense list
	
	private static Expense expense;	// Expense to be edited
	private static int expense_position; // Its position in the expense list

	Spinner currencies;
	private boolean isFirstSelection;
	private ArrayAdapter<AvailableCurrencies> spinner_adapter;
	
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
                // Try to save. Do nothing if fails.
                if (saveExpense()) {
	                setResult(RESULT_OK);
	                finish();
                }
            }
        });
        
        // == Currency Spinner (OnChange) ==
        currencies = (Spinner) findViewById(R.id.currency_type_spinner);
        // Mostly coped from ClaimSummaryActivity
		currencies.setOnItemSelectedListener(new OnItemSelectedListener() { 
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			    // Change the hint for amount
				updateHint();
			    
				// Don't do work if we aren't done initializing the Activity yet.
				// (first selection is always the default selector)
				if (isFirstSelection) {
					isFirstSelection = false;
					return;
				}
				
				// Fetch status value from view
			    AvailableCurrencies currency_type = (AvailableCurrencies) parent.getItemAtPosition(pos);
			    
			    // Change the currency type
			    expense.getValue().setCurrencyType(currency_type);
			    
			    // Inform Application that the update occurred so it can be saved.
			    ExpenseMasterApplication.updateClaim(EditExpenseActivity.this,
			    		ExpenseMasterApplication.findClaim(claim), claim);
			    
			    // XXX
			    // Change views on this page to enable/disable as necessary.
			    // updateStatusViews();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Shouldn't happen
				throw new RuntimeException("Spinner selected nothing");
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// This should never be -1
		claim_position = getIntent().getIntExtra("claim_position", -1);
		if (claim_position == -1)
			throw new RuntimeException("Didn't receive a claim context to edit");
		claim = ExpenseMasterApplication.getClaim(claim_position);
		// This should be -1 only on NEW_EXPENSE_REQUEST
		expense_position = getIntent().getIntExtra("expense_position", -1);
		if (expense_position == -1) {
			expense = new Expense();
		} else {
			expense = claim.getExpense(expense_position);
		}
		
		// Associate spinner with available currencies enum
		spinner_adapter = new ArrayAdapter<AvailableCurrencies>(this,
				android.R.layout.simple_spinner_item,
				AvailableCurrencies.values());
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencies.setAdapter(spinner_adapter);

		// Load expense attributes into views
		updateViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateHint();
	}
	
	@Override
	public void onBackPressed() {
		// Cancel without saving
		super.onBackPressed();
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
	}
	/**
	 * Tries to create a new money instance. Assumes string is
	 * a decimal formatted string and checks for the right number
	 * of digits.
	 * If there are problems, it displays a Toast and returns null
	 * @param currency_type
	 * @param amount
	 */
	public Money tryParseMoney(Currency currency_type, String value) {
		if (value == null || value.isEmpty()) {
			Toast.makeText(this, "Amount cannot be blank", Toast.LENGTH_SHORT).show();
			return null;
		}
		String value_parts[];
		int whole, cents;
		
		// Split should return 1 or 2 things.
		value_parts = value.split("\\.");
		if (value_parts.length == 1) {
			// This shouldn't throw errors if it's a decimal format
			// since it's non-empty
			whole = Integer.parseInt(value_parts[0]);
			cents = 0;
		} else if (value_parts.length == 2) {
			// This might throw errors if either side of the decimal is blank
			// So, we handle these cases appropriately.
			
			// If whole portion is empty, assume it is supposed to be 0.
			whole = (value_parts[0].isEmpty()) ? 0 :Integer.parseInt(value_parts[0]);
			
			// "cents" portion must have the right number of digits for
			// the given currency. The hint for amount is changed when the 
			// currency type changes to make entry easier.
			if (value_parts[1].length() != currency_type.getDefaultFractionDigits()) {
				Toast.makeText(this, "Amount must have " + 
						currency_type.getDefaultFractionDigits() + " decimal places.",
						Toast.LENGTH_SHORT).show();
			}
			cents = Integer.parseInt(value_parts[1]);
		} else {
			// Should never happen if using android's decimal format.
			throw new RuntimeException("Malformatted string");
		}
		return new Money(currency_type, whole, cents);
	}

	/**
	 * Tries to save the expense.
	 * @return true on success.
	 */
	private boolean saveExpense() {
		String name = getExpenseName();
		Currency currency_type = getCurrencyType();
		String value = getCurrencyAmount();
		Date date = getExpenseDate();
		Money money;

		// We parse the value here so we can show appropriate toasts when the value
		// is invalid, even though Money can handle it itself.
		
		if (name == null || name.isEmpty()) {
			Toast.makeText(this, "Name cannot be left blank", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		// Try to get the money value, if possible.
		money = tryParseMoney(currency_type, value);
		if (money == null) {
			return false;
		}

		// Make new if a position hasn't been defined
		if (expense_position == -1) {
			expense_position = claim.addExpense(expense);
			getIntent().putExtra("expense_position", expense_position);
		}
		// Push to expense item object
		expense.setName(name);
		expense.setValue(money);
		expense.setDate(date);
		
		// Push update to application
		ExpenseMasterApplication.updateClaim(this, claim_position, claim);
		return true;
	}

	/**
	 * Set the expense name in the textbox
	 * @param name
	 * @return
	 */
	private void updateExpenseNameView(String name) {
		EditText text = (EditText) findViewById(R.id.expense_name_text);
		text.setText(name);
	}

	/**
	 * Get expense name from the textbox
	 * @return
	 */
	private String getExpenseName() {
		EditText text = (EditText) findViewById(R.id.expense_name_text);
		return text.getText().toString();
	}

	/**
	 * Get the currency amount from the text box
	 * @param value
	 * @return
	 */
	private String getCurrencyAmount() {
		EditText text = (EditText) findViewById(R.id.expense_amount_text);
		return text.getText().toString();
	}

	private Currency getCurrencyType() {
		// This work is also handled by the OnItemChanged handler
		return ((AvailableCurrencies) currencies.getSelectedItem()).getCurrency();
	}
	
	private Date getExpenseDate() {
		EditText text = (EditText) findViewById(R.id.expense_date_text);
		try {
			return ExpenseMasterApplication.global_date_format.parse(text.getText().toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Set the currency amount from the text box
	 * @param value
	 * @return
	 */
	private void updateCurrencyValueView(String value) {
		EditText text = (EditText) findViewById(R.id.expense_amount_text);
		text.setText(value);
	}

	/**
	 * Set the currency type to the spinner
	 * @param value
	 * @return 
	 * @return
	 */
	private void updateCurrencyTypeView(AvailableCurrencies c) {
		// This work is also handled by the OnItemChanged handler
		currencies.setSelection(spinner_adapter.getPosition(c));
	}

	private void updateDateView(Date date) {
		EditText text = (EditText) findViewById(R.id.expense_date_text);
		text.setText(ExpenseMasterApplication.global_date_format.format(date));
	}
	
	private void updateViews() {
		updateExpenseNameView(expense.getName());
		updateDateView(expense.getDate());
		updateCurrencyValueView(expense.getValue().toValueString());
		// CurrencyType view is slightly harder to update because it's an enumeration
		updateCurrencyTypeView(expense.getValue().toAvailableCurrency());
	}
	
	/**
	 * Updates the hint for the amount text.
	 */
	private void updateHint() {
	    EditText expense_amount = (EditText) findViewById(R.id.expense_amount_text);
	    expense_amount.setHint("e.g. 10" + expense.getValue().getZeroDenominationString());
	}
}
