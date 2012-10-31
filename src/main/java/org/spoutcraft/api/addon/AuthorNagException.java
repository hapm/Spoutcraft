/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.api.addon;

public class AuthorNagException extends RuntimeException {
	private static final long serialVersionUID = 7865800524327635948L;
	private final String message;

	/**
	 * Constructs a new AuthorNagException based on the given Exception
	 *
	 * @param message Brief message explaining the cause of the exception
	 */
	public AuthorNagException(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
