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

/**
 * A representation of an explosive entity
 */
public interface Explosive extends Entity {
	/**
	 * Set the radius affected by this explosive's explosion
	 * @param yield The explosive yield
	 */
	public void setYield(float yield);

	/**
	 * Return the radius or yield of this explosive's explosion
	 * @return the radius of blocks affected
	 */
	public float getYield();

	/**
	 * Set whether or not this explosive's explosion causes fire
	 * @param isIncendiary Whether it should cause fire
	 */
	public void setIsIncendiary(boolean isIncendiary);

	/**
	 * Return whether or not this explosive creates a fire when exploding
	 * @return true if the explosive creates fire, false otherwise
	 */
	public boolean isIncendiary();
}
