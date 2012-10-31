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
package org.spoutcraft.api.block;

import org.spoutcraft.api.Instrument;
import org.spoutcraft.api.Note;

public interface NoteBlock extends BlockState {
	/**
	 * Gets the note.
	 *
	 * @return
	 */
	public Note getNote();

	/**
	 * Gets the note.
	 *
	 * @return
	 */
	public byte getRawNote();

	/**
	 * Set the note.
	 *
	 * @param note
	 */
	public void setNote(Note note);

	/**
	 * Set the note.
	 *
	 * @param note
	 */
	public void setRawNote(byte note);

	/**
	 * Attempts to play the note at block<br />
	 * <br />
	 * If the block is no longer a note block, this will return false
	 *
	 * @return true if successful, otherwise false
	 */
	public boolean play();

	/**
	 * Plays an arbitrary note with an arbitrary instrument
	 *
	 * @return true if successful, otherwise false
	 */
	public boolean play(byte instrument, byte note);

	/**
	 * Plays an arbitrary note with an arbitrary instrument
	 *
	 * @return true if successful, otherwise false
	 */
	public boolean play(Instrument instrument, Note note);
}
