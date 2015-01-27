package ca.ualberta.cs.expensemaster;

public interface EMView<M extends EMModel> {
	
	void update(M model);
	
}
