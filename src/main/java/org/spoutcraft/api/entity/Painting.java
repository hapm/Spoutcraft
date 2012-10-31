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
package org.spoutcraft.api.entity;

import org.spoutcraft.api.Art;
import org.spoutcraft.api.block.BlockFace;

/**
 * Represents a Painting.
 */
public interface Painting extends Entity {
	/**
	 * Get the art on this painting
	 * @return The art
	 */
	public Art getArt();

	/**
	 * Set the art on this painting
	 * @param art The new art
	 * @return False if the new art won't fit at the painting's current location
	 */
	public boolean setArt(Art art);

	/**
	 * Set the art on this painting
	 * @param art The new art
	 * @param force If true, force the new art regardless of whether it fits at the current location
	 * Note that forcing it where it can't fit normally causes it to drop as an item unless you override
	 * this by catching the PAINTING_BREAK event.
	 * @return False if force was false and the new art won't fit at the painting's current location
	 */
	public boolean setArt(Art art, boolean force);

	/**
	 * Sets the direction of the painting, potentially overriding rules of placement. Note that if the result
	 * is not valid the painting would normally drop as an item.
	 * @param face The new direction.
	 * @param force Whether to force it.
	 * @return False if force was false and there was no block for it to attach to in order to face the given direction.
	 */
	public boolean setFacingDirection(BlockFace face, boolean force);
}
