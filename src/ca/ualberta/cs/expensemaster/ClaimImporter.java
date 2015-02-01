package ca.ualberta.cs.expensemaster;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ClaimImporter {
	public Claim readClaim() throws IOException, ParseException;
	public Expense readExpense() throws IOException, ParseException;
	
	public List<Claim> readAll() throws IOException, ParseException;
	
	public void close() throws IOException;
}
