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

import android.content.Context;
import android.content.Intent;

/* This class was built so that emails could be sent in HTML,
 * ...but it turns out Android doesn't support HTML and MIME very well,
 * as well as too many clients (c'mon, what year is it anyway?!).
 */
public abstract class EmailMessage { 
	protected String address;
	protected String subject;
	protected StringBuilder body;
	
	// implementation details
	private boolean open;

	public EmailMessage(String address, String subject) {
		super();
		this.address = address;
		this.subject = subject;
		
		// Use a StringBuilder because appending strings is slow.
		this.body = new StringBuilder();
		
		open = true;
	}
	
	public void put(String text) {
		body.append(text + "\n");
	}
	public void putParagraph(String text) {
		body.append("\t" + text + "\n");
	}
	
	public void close() {
		if (!open) {
			throw new IllegalStateException("Email is already in a closed state.");
		}
		open = false;
	}
	
	public boolean isOpen(){
		return open;
	}

	/**
	 * Sends an email if the email has been closed.
	 * @param ctx
	 */
	public void send(Context ctx) {
		if (isOpen()) {
			throw new IllegalStateException("Email hasn't been closed.");
		}
		ctx.startActivity(Intent.createChooser(getMessageIntent(), "Send Email"));
	}
	
	/**
	 * This is hidden from the user but is called after the state
	 * is validated. It should open the email client in the end.
	 * The purpose of this is to give a last chance to change
	 * subject, address, or body/body type before the message is sent.
	 * @param ctx
	 */
	protected abstract Intent getMessageIntent();
}
