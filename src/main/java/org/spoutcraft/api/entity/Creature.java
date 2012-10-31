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
 * Represents a Creature. Creatures are non-intelligent monsters or animals which
 * have very simple abilities.
 */
public interface Creature extends LivingEntity {
	/**
	 * Instructs this Creature to set the specified LivingEntity as its target.
	 * Hostile creatures may attack their target, and friendly creatures may
	 * follow their target.
	 *
	 * @param target New LivingEntity to target, or null to clear the target
	 */
	public void setTarget(LivingEntity target);

	/**
	 * Gets the current target of this Creature
	 *
	 * @return Current target of this creature, or null if none exists
	 */
	public LivingEntity getTarget();
}
