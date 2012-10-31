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
package org.spoutcraft.api.material.item;

import java.io.IOException;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.block.Block;
import org.spoutcraft.api.block.BlockFace;
import org.spoutcraft.api.entity.Player;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.api.material.CustomItem;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.api.util.UniqueItemStringMap;

public class GenericCustomItem implements CustomItem {
	private String name;
	private String fullName;
	private Addon addon;
	private int customId;
	public String texture;

	/**
	 * Creates a GenericCustomItem with no values, used for serialization purposes only.
	 */
	public GenericCustomItem() {
	}

	public GenericCustomItem(Addon addon, String name, int customId) {
		this.name = name;
		this.fullName = addon.getDescription().getName() + name;
		this.customId = customId;
		this.addon = addon;
		this.setName(name);
		MaterialData.addCustomItem(this);
	}

	public GenericCustomItem(Addon addon, String name) {
		this(addon, name, UniqueItemStringMap.getId(addon.getDescription().getName() + name));
	}

	public GenericCustomItem(Addon addon, String name, String texture) {
		this(addon, name);
		this.setTexture(texture);
	}

	public int getRawId() {
		return 318; // Flint
	}

	public int getRawData() {
		return customId;
	}

	public boolean hasSubtypes() {
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.fullName = addon.getDescription().getName() + name;
	}

	public int getCustomId() {
		return customId;
	}

	public String getFullName() {
		return fullName;
	}

	public String getNotchianName() {
		return getName();
	}

	public Addon getAddon() {
		return addon;
	}

	public CustomItem setTexture(String texture) {
		this.texture = texture;
		return this;
	}

	public String getTexture() {
		return texture;
	}

	public boolean onItemInteract(Player player, Block block, BlockFace face) {
		return true;
	}

	public void readData(SpoutInputStream input) throws IOException {
		customId = input.readInt();
		name = input.readString();
		addon = Spoutcraft.getAddonManager().getOrCreateAddon(input.readString());
		texture = input.readString();
		setName(name);
		MaterialData.addCustomItem(this);
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.write(customId);
		output.writeString(getName());
		output.writeString(getAddon().getDescription().getName());
		output.writeString(getTexture());
	}

	public int getVersion() {
		return 0;
	}
}
