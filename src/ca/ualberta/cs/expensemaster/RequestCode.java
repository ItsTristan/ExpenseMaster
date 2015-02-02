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

/**
 * This class is for global constants that don't
 * nicely fit into the scope of Enums.
 * 
 * @author ItsTristan (Tristan Meleshko)
 * 
 */
public abstract class RequestCode {
	public static final int REQUEST_NEW_CLAIM = 1;
	public static final int REQUEST_EDIT_CLAIM = 2;
	
	public static final int REQUEST_CLAIM_SUMMARY = 3;

	public static final int REQUEST_NEW_EXPENSE = 4;
	public static final int REQUEST_EDIT_EXPENSE = 5;
}
