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

public class UnknownDependencyException extends Exception {
	private static final long serialVersionUID = 989022178855271278L;
	private final Throwable cause;
	private final String message;

	public UnknownDependencyException(Throwable throwable) {
		this(throwable, "Unknown dependency");
	}

	public UnknownDependencyException(String message) {
		this(null, message);
	}

	public UnknownDependencyException(Throwable throwable, String message) {
		this.cause = null;
		this.message = message;
	}

	public UnknownDependencyException() {
		this(null, "Unknown dependency");
	}

	public Throwable getCause() {
		return this.cause;
	}

	public String getMessage() {
		return this.message;
	}
}
