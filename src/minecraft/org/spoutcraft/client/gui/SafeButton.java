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
package org.spoutcraft.client.gui;

import org.spoutcraft.spoutcraftapi.ChatColor;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public abstract class SafeButton extends GenericButton {
	private String warningText = ChatColor.RED + "Really?";
	private boolean reallyShown = false;
	private long timeout = 3000;
	protected Thread currentThread = null;

	@Override
	public String getText() {
		return reallyShown?getWarningText():super.getText();
	}

	public String getOriginalText() {
		return super.getText();
	}

	public void setWarningText(String warningText) {
		this.warningText = warningText;
	}

	public String getWarningText() {
		return warningText;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getTimeout() {
		return timeout;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		if (currentThread != null) {
			currentThread.interrupt();
			currentThread = null;
		}
		if (reallyShown) {
			executeAction();
			reallyShown = false;
		} else {
			reallyShown = true;
			currentThread = new Thread() {
				public void run() {
					try {
						Thread.sleep(getTimeout());
						reallyShown = false;
						currentThread = null;
					} catch(InterruptedException e) {}
				}
			};
			currentThread.start();
		}
	}

	/**
	 * Execute the unsafe action. Will be called if clicked the second time within the timeout.
	 */
	protected abstract void executeAction();
}
