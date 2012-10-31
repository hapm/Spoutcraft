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

import org.spoutcraft.api.NotImplemented;
import org.spoutcraft.api.SPOnly;
import org.spoutcraft.api.World;
import org.spoutcraft.api.util.FixedLocation;

public interface Block {
	/**
	 * Gets the hardness of this block
	 * @return hardnes
	 */
	@SPOnly
	public float getHardness();

	/**
	 * Sets the hardness of this block
	 * @param hardness to set to
	 */
	@SPOnly
	public void setHardness(float hardness);

	/**
	 * Resets the hardness of this block to the notchian defaults
	 */
	@SPOnly
	public void resetHardness();

	/**
	 * Gets the light level between 0-15
	 *
	 * @return light level
	 */
	public byte getLightLevel();

	/**
	 * Gets the type-id of this block
	 *
	 * @return block type-id
	 */
	public int getTypeId();

	/**
	 * Gets the metadata for this block
	 *
	 * @return block metadata
	 */
	public byte getData();

	/**
	 * Gets the block at the given offsets
	 *
	 * @param modX X-coordinate offset
	 * @param modY Y-coordinate offset
	 * @param modZ Z-coordinate offset
	 * @return Block at the given offsets
	 */
	public Block getRelative(int modX, int modY, int modZ);

	/**
	 * Gets the block at the given face<br />
	 * <br />
	 * This method is equal to getRelative(face, 1)
	 *
	 * @param face Face of this block to return
	 * @return Block at the given face
	 * @see Block.getRelative(BlockFace face, int distance);
	 */
	public Block getRelative(BlockFace face);

	/**
	 * Gets the block at the given distance of the given face<br />
	 * <br />
	 * For example, the following method places water at 100,102,100; two blocks
	 * above 100,100,100.
	 * <pre>
	 * Block block = world.getBlockAt(100,100,100);
	 * Block shower = block.getFace(BlockFace.UP, 2);
	 * shower.setType(Material.WATER);
	 * </pre>
	 *
	 * @param face Face of this block to return
	 * @param distance Distance to get the block at
	 * @return Block at the given face
	 */
	public Block getRelative(BlockFace face, int distance);

	/**
	 * Gets the world which contains this Block
	 *
	 * @return World containing this block
	 */
	public World getWorld();

	/**
	 * Gets the x-coordinate of this block
	 *
	 * @return x-coordinate
	 */
	public int getX();

	/**
	 * Gets the y-coordinate of this block
	 *
	 * @return y-coordinate
	 */
	public int getY();

	/**
	 * Gets the z-coordinate of this block
	 *
	 * @return z-coordinate
	 */
	public int getZ();

	/**
	 * Gets the material that this block represents
	 *
	 * @return material
	 */
	public org.spoutcraft.api.material.Block getType();

	/**
	 * Gets the Location of the block
	 *
	 * @return Location of block
	 */

	public FixedLocation getLocation();

	/**
	 * Gets the chunk which contains this block
	 *
	 * @return Containing Chunk
	 */
	public Chunk getChunk();

	/**
	 * Sets the metadata for this block
	 *
	 * @param data New block specific metadata
	 */
	public void setData(byte data);

	/**
	 * Sets the metadata for this block
	 *
	 * @param data New block specific metadata
	 * @param whether or not to update neighbor blocks after setting the metadata
	 */
	public void setData(byte data, boolean applyPhyiscs);

	/**
	 * Sets the type-id of this block
	 *
	 * @param type Type-Id to change this block to
	 * @return whether the block was changed
	 */
	public boolean setTypeId(int type);

	/**
	 * Sets the type-id of this block
	 *
	 * @param type Type-Id to change this block to
	 * @param applyPhysics whether neighbor blocks should be updated
	 * @return whether the block was changed
	 */
	boolean setTypeId(int type, boolean applyPhysics);

	/**
	 * Sets the type-id of this block
	 *
	 * @param type Type-Id to change this block to
	 * @param data to change this block to
	 * @param applyPhysics whether neighbor blocks should be updated
	 * @return whether the block was changed
	 */
	public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics);

	/**
	 * Gets the face relation of this block compared to the given block<br />
	 * <br />
	 * For example:
	 * <pre>
	 * Block current = world.getBlockAt(100, 100, 100);
	 * Block target = world.getBlockAt(100, 101, 100);
	 *
	 * current.getFace(target) == BlockFace.Up;
	 * </pre>
	 * <br />
	 * If the given block is not connected to this block, null may be returned
	 *
	 * @param block Block to compare against this block
	 * @return BlockFace of this block which has the requested block, or null
	 */
	public BlockFace getFace(Block block);

	/**
	 * Captures the current state of this block. You may then cast that state
	 * into any accepted type, such as Furnace or Sign.
	 *
	 * The returned object will never be updated, and you are not guaranteed that
	 * (for example) a sign is still a sign after you capture its state.
	 *
	 * @return BlockState with the current state of this block.
	 */
	@NotImplemented
	public BlockState getState();

	/**
	 * Returns the biome that this block resides in
	 *
	 * @return Biome type containing this block
	 */
	public Biome getBiome();

	/**
	 * Returns true if the block is being powered by Redstone.
	 *
	 * @return true if the block is powered
	 */
	boolean isBlockPowered();

	/**
	 * Returns true if the block is being indirectly powered by Redstone.
	 *
	 * @return true if the block is indirectly powered
	 */
	boolean isBlockIndirectlyPowered();

	/**
	 * Returns true if the block face is being powered by Redstone.
	 *
	 * @return true if the block face is powered
	 */
	boolean isBlockFacePowered(BlockFace face);

	/**
	 * Returns true if the block face is being indirectly powered by Redstone.
	 *
	 * @return true if the block face is indirectly powerd
	 */
	boolean isBlockFaceIndirectlyPowered(BlockFace face);

	/**
	 * Returns the redstone power being provided to this block face
	 *
	 * @param face the face of the block to query or BlockFace.SELF for the block itself
	 * @return power level
	 */
	public int getBlockPower(BlockFace face);

	/**
	 * Returns the redstone power being provided to this block
	 *
	 * @return power level
	 */
	public int getBlockPower();

	/**
	 * Checks if this block is empty.
	 *
	 * A block is considered empty when {@link #getType()} returns {@link Material#AIR}.
	 *
	 * @return true if this block is empty
	 */
	public boolean isEmpty();

	/**
	 * Checks if this block is liquid.
	 *
	 * A block is considered liquid when {@link #getType()} returns {@link Material#WATER}, {@link Material#STATIONARY_WATER}, {@link Material#LAVA} or {@link Material#STATIONARY_LAVA}.
	 *
	 * @return true if this block is liquid
	 */
	public boolean isLiquid();

	/**
	 * Gets the temperature of the biome of this block
	 *
	 * @return Temperature of this block
	 */
	public double getTemperature();

	/**
	 * Gets the humidity of the biome of this block
	 *
	 * @return Humidity of this block
	 */
	public double getHumidity();
}
