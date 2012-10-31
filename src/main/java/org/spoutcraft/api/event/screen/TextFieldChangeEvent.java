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
import org.spoutcraft.api.gui.TextField;

public class TextFieldChangeEvent extends ScreenEvent<TextFieldChangeEvent>{
	protected TextField field;
	protected String oldVal;
	protected String newVal;
	public TextFieldChangeEvent(Player player, Screen screen, TextField field, String newVal) {
		super(player, screen, ScreenType.CUSTOM_SCREEN);
		this.field = field;
		if (field != null) {
			this.oldVal = field.getText();
		} else {
			this.oldVal = "";
		}
		this.newVal = newVal;
	}

	private static final TextFieldChangeEvent instance = new TextFieldChangeEvent(null, null, null, null);

	/**
	 * Gets the singleton, updates its state and returns it
	 * @param Player to update the singleton with
	 * @param Screen to update the singleton with
	 * @param TextField to update the singleton with
	 * @param String to update the singleton with
	 * @return TextFieldChangeEvent singleton
	 */
	public static TextFieldChangeEvent getInstance(Player player, Screen screen, TextField field, String newVal) {
		instance.player = player;
		instance.screen = screen;
		instance.type = ScreenType.CUSTOM_SCREEN;
		instance.field = field;
		instance.newVal = newVal;
		return instance;
	}

	public TextField getTextField() {
		return field;
	}

	public String getOldText() {
		return oldVal;
	}

	public String getNewText() {
		return newVal;
	}

	public void setNewText(String newVal) {
		if (newVal == null) {
			newVal = "";
		}
		this.newVal = newVal;
	}

	public static final HandlerList<TextFieldChangeEvent> handlers = new HandlerList<TextFieldChangeEvent>();

	@Override
	public HandlerList<TextFieldChangeEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Text Field Change Event";
	}
}
