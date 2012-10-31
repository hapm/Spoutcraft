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
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Screen;
import org.spoutcraft.api.gui.ScreenType;

public class ButtonClickEvent extends ScreenEvent<ButtonClickEvent>{
	protected Button control;
	private ButtonClickEvent(Player player, Screen screen, Button control) {
		super(player, screen, ScreenType.CUSTOM_SCREEN);
		this.control = control;
	}

	private static final ButtonClickEvent instance = new ButtonClickEvent(null, null, null);

	/**
	 * Gets the singleton, updates its state and returns it
	 * @param Player to update the singleton with
	 * @param Screen to update the singleton with
	 * @param Button to update the singleton with
	 * @return ButtonClickEvent singleton
	 */
	public static ButtonClickEvent getInstance(Player player, Screen screen, Button control) {
		instance.player = player;
		instance.screen = screen;
		instance.type = ScreenType.CUSTOM_SCREEN;
		instance.control = control;
		return instance;
	}

	public Button getButton() {
		return control;
	}

	public static final HandlerList<ButtonClickEvent> handlers = new HandlerList<ButtonClickEvent>();

	@Override
	public HandlerList<ButtonClickEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Button Click Event";
	}
}
