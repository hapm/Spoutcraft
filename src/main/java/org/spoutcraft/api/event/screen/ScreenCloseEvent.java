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
package org.spoutcraft.api.event.screen;

import org.spoutcraft.api.entity.Player;
import org.spoutcraft.api.event.HandlerList;
import org.spoutcraft.api.gui.Screen;
import org.spoutcraft.api.gui.ScreenType;

public class ScreenCloseEvent extends ScreenEvent<ScreenCloseEvent>{
	private ScreenCloseEvent(Player player, Screen screen, ScreenType type) {
		super(player, screen, type);
	}

	private static final ScreenCloseEvent instance = new ScreenCloseEvent(null, null, null);

	/**
	 * Gets the singleton, updates its state and returns it
	 * @param Player to update the singleton with
	 * @param Screen to update the singleton with
	 * @param ScreenType to update the singleton with
	 * @return ScreenCloseEvent singleton
	 */
	public static ScreenCloseEvent getInstance(Player player, Screen screen, ScreenType type) {
		instance.player = player;
		instance.screen = screen;
		instance.type = type;
		return instance;
	}

	public static final HandlerList<ScreenCloseEvent> handlers = new HandlerList<ScreenCloseEvent>();

	@Override
	public HandlerList<ScreenCloseEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Screen Close Event";
	}
}
