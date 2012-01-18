/*
 * This file is part of Bukkit (http://bukkit.org/).
 *
 * Bukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.block;

import net.minecraft.src.InventoryLargeChest;
import net.minecraft.src.TileEntityChest;

import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.client.inventory.CraftDoubleInventory;
import org.spoutcraft.client.inventory.CraftInventory;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.BlockState;
import org.spoutcraft.spoutcraftapi.block.Chest;
import org.spoutcraft.spoutcraftapi.inventory.DoubleChestInventory;
import org.spoutcraft.spoutcraftapi.inventory.Inventory;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public class CraftChest extends CraftBlockState implements Chest {
	private final SpoutcraftWorld world;
	protected final TileEntityChest chest;

	public CraftChest(final Block block) {
		super(block);

		world = (SpoutcraftWorld) block.getWorld();
		chest = (TileEntityChest) world.getHandle().getBlockTileEntity(getX(), getY(), getZ());
	}

	public Inventory getInventory() {
		return new CraftInventory(chest);
	}

	public boolean update(boolean force) {
		boolean result = super.update(force);

		if (result) {
			chest.updateEntity();
		}

		return result;
	}

	public boolean isDoubleChest() {
		return getOtherSide() != null;
	}

	public Chest getOtherSide() {
		if (getBlock().getRelative(1, 0, 0).getType() == MaterialData.chest) {
			BlockState bs = getBlock().getRelative(1, 0, 0).getState();
			if (bs instanceof Chest) {
				return ((Chest)bs);
			}
		}
		if (getBlock().getRelative(-1, 0, 0).getType() == MaterialData.chest) {
			BlockState bs = getBlock().getRelative(-1, 0, 0).getState();
			if (bs instanceof Chest) {
				return ((Chest)bs);
			}
		}
		if (getBlock().getRelative(0, 0, 1).getType() == MaterialData.chest) {
			BlockState bs = getBlock().getRelative(0, 0, 1).getState();
			if (bs instanceof Chest) {
				return ((Chest)bs);
			}
		}
		if (getBlock().getRelative(0, 0, -1).getType() == MaterialData.chest) {
			BlockState bs = getBlock().getRelative(0, 0, -1).getState();
			if (bs instanceof Chest) {
				return ((Chest)bs);
			}
		}
		return null;
	}

	public DoubleChestInventory getFullInventory() {
		if (isDoubleChest()) {
			CraftChest other = (CraftChest)getOtherSide();
			CraftChest top, bottom;

			if ((this.getLocation().getBlockX() < other.getLocation().getBlockX()) ||
				(this.getLocation().getBlockZ() < other.getLocation().getBlockZ())) {
				top = this;
				bottom = other;
			} else {
				top = other;
				bottom = this;
			}

			return new CraftDoubleInventory(new InventoryLargeChest("Double Chest", top.chest, bottom.chest), top.getBlock(), bottom.getBlock());
		}
		return null;
	}

	public Inventory getLargestInventory() {
		if (isDoubleChest()) {
			return getFullInventory();
		}
		return getInventory();
	}
}
