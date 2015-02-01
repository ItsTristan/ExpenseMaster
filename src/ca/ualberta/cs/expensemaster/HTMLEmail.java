package ca.ualberta.cs.expensemaster;

import java.util.Stack;

import android.content.Intent;
import android.text.Html;

/* This class was built so that emails could be sent in HTML,
 * ...but it turns out Android doesn't support HTML and MIME very well,
 * as well as too many clients (c'mon, what year is it anyway?!).
 */
public class HTMLEmail extends EmailMessage { 
	private Stack<String> tagStack;

	public HTMLEmail(String address, String subject) {
		super(address, subject);

		tagStack = new Stack<String>();
		startTag("html");
	}
	
	// Tag starters
	public void startTag(String tag, String properties) {
		put("<" + tag + " " + properties + " >");
		tagStack.add("</" + tag + ">");
	}
	public void startTag(String tag) {
		put("<" + tag + ">");
		tagStack.add("</" + tag + ">");
	}
	// Tag ender
	public void endTag() {
		put(tagStack.pop());
	}
	
	/**
	 * Put some things into the email
	 * @param p
	 */
	@Override
	public void putParagraph(String data) {
		putTag("p", data);
	}
	public void putTag(String tag, String data) {
		startTag(tag);
		put(data);
		endTag();
	}
	public void putSmallTag(String tag, String data) {
		put("<" + tag + " " + data + " />");
	}
	public void putSmallTag(String tag) {
		put("<" + tag + " />");
	}
	/**
	 * Puts string data in directly. This should basically
	 * never be used.
	 * @param data
	 */
	public void putDirect(String data) {
		put(data);
	}
	
	@Override
	public Intent getMessageIntent() {
		if (isOpen()) {
			throw new IllegalStateException("Email hasn't been closed.");
		}
		// Create the intent and start the activity using the given context.
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_EMAIL, address);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		// Convert the body to an HTML body with MIME
		intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body.toString()));
		return intent;
	}
}
