package ca.ualberta.cs.expensemaster;

import java.io.IOException;
import java.util.List;

public interface ClaimExporter {
	public void writeClaim(Claim c) throws IOException;
	public void writeExpense(Expense e) throws IOException;
	
	public void writeAll(List<Claim> c) throws IOException;
	public void close() throws IOException;
}
