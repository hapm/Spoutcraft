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

import org.spoutcraft.api.util.Vector;

/**
 * Represents a minecart entity.
 */
public interface Minecart extends Vehicle {
	/**
	 * Sets a minecart's damage.
	 *
	 * @param damage over 40 to "kill" a minecart
	 */
	public void setDamage(int damage);

	/**
	 * Gets a minecart's damage.
	 *
	 * @return The damage
	 */
	public int getDamage();

	/**
	 * Gets the maximum speed of a minecart. The speed is unrelated to the velocity.
	 *
	 * @return The max speed
	 */
	public double getMaxSpeed();

	/**
	 * Sets the maximum speed of a minecart. Must be nonnegative. Default is 0.4D.
	 *
	 * @param speed The max speed
	 */
	public void setMaxSpeed(double speed);

	/**
	 * Returns whether this minecart will slow down faster without a passenger occupying it
	 *
	 * @return Whether it decelerates faster
	 */
	public boolean isSlowWhenEmpty();

	/**
	 * Sets whether this minecart will slow down faster without a passenger occupying it
	 *
	 * @param slow Whether it will decelerate faster
	 */
	public void setSlowWhenEmpty(boolean slow);

	/**
	 * Gets the flying velocity modifier. Used for minecarts that are in mid-air.
	 * A flying minecart's velocity is multiplied by this factor each tick.
	 *
	 * @return The vector factor
	 */
	public Vector getFlyingVelocityMod();

	/**
	 * Sets the flying velocity modifier. Used for minecarts that are in mid-air.
	 * A flying minecart's velocity is multiplied by this factor each tick.
	 *
	 * @param flying velocity modifier vector
	 */
	public void setFlyingVelocityMod(Vector flying);

	/**
	 * Gets the derailed velocity modifier. Used for minecarts that are on the ground, but not on rails.
	 *
	 * A derailed minecart's velocity is multiplied by this factor each tick.
	 * @return derailed visible speed
	 */
	public Vector getDerailedVelocityMod();

	/**
	 * Sets the derailed velocity modifier. Used for minecarts that are on the ground, but not on rails.
	 * A derailed minecart's velocity is multiplied by this factor each tick.
	 *
	 * @param derailed visible speed
	 */
	public void setDerailedVelocityMod(Vector derailed);
}
