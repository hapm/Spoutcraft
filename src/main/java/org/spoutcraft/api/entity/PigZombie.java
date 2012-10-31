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
 * Represents a Pig Zombie.
 */
public interface PigZombie extends Zombie {
	/**
	 * Get the pig zombie's current anger level.
	 *
	 * @return The anger level.
	 */
	int getAnger();

	/**
	 * Set the pig zombie's current anger level.
	 *
	 * @param level The anger level. Higher levels of anger take longer to wear off.
	 */
	void setAnger(int level);

	/**
	 * Shorthand; sets to either 0 or the default level.
	 *
	 * @param angry Whether the zombie should be angry.
	 */
	void setAngry(boolean angry);

	/**
	 * Shorthand; gets whether the zombie is angry.
	 *
	 * @return True if the zombie is angry, otherwise false.
	 */
	boolean isAngry();
}
