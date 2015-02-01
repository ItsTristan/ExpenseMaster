/* This file is part of the ExpenseMaster Android Application
 * See github.com/ItsTristan/ExpenseMaster for more information
 * 
 * Copyright (C) 2015 Tristan Meleshko
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA. */

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
