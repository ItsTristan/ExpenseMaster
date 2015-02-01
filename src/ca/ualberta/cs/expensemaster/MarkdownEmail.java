package ca.ualberta.cs.expensemaster;

import android.content.Intent;

public class MarkdownEmail extends EmailMessage {
	private int listNumber = 1;
	private int listDepth = 1;
	
	public MarkdownEmail(String address, String subject) {
		super(address, subject);
		listNumber = 1;
		listDepth = 1;
	}
	
	public void putH1(String text) {
		put(text);
		repeat("=", text.length());
		body.append("\n");
	}
	public void putH2(String text) {
		body.append(text + "\n");
		repeat("-", text.length());
		body.append("\n");
	}
	public void putHeader(String text, int depth) {
		repeat("#", depth);
		body.append(" " + text + "\n");
	}
	public void putQuote(String text, int depth) {
		repeat("> ", depth);
		put(text + "\n");
	}

	public void putListItem(String text) {
		putListItem(text, 0);
	}

	public void putListItem(String text, int depth) {
		repeat("\t", depth);
		put("*\t" + text);
	}
	
	public void setNumberedList(int start, int depth) {
		listDepth = depth;
		listNumber = start;
	}
	public void putNumberedListItem(String text) {
		repeat("\t", listDepth);
		put((listNumber++) + "\t" + text);
	}
	
	public void putHorizontalRule() {
		puthorizontalRule("- ");
	}
	public void puthorizontalRule(String style) {
		if (!style.isEmpty())
			repeat(style, 60 / style.length());
		body.append("\n");
	}
	
	private void repeat(String text, int numTimes) {
		for (int i = 0; i < numTimes; i++)
			body.append(text);
	}
	
	
	@Override
	protected Intent getMessageIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_EMAIL, address);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		// Convert the body to an HTML body with MIME
		intent.putExtra(Intent.EXTRA_TEXT, body.toString());
		return intent;
	}

}
