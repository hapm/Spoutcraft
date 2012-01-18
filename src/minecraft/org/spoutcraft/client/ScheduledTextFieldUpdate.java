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
package org.spoutcraft.client;

import org.spoutcraft.client.packet.PacketControlAction;
import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.gui.TextField;

public class ScheduledTextFieldUpdate implements Runnable {
	private static final long DELAY_TIME = 500;
	private static final long SLEEP_TIME = 125;
	private TextField textField;
	private Screen screen;
	private long sendTime;
	private Thread thread;

	public ScheduledTextFieldUpdate(Screen screen, TextField textField) {
		this.textField = textField;
		this.screen = screen;
	}

	public void run() {
		delay();
		while (!expired()) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				break;
			}
		}
		textField.onTypingFinished();
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketControlAction(screen, textField, textField.getText(), textField.getCursorPosition()));
	}

	public synchronized void delay() {
		sendTime = System.currentTimeMillis() + DELAY_TIME;
	}

	public synchronized boolean expired() {
		return sendTime <= System.currentTimeMillis();
	}

	public synchronized void start() {
		(thread = new Thread(this)).start();
	}

	public synchronized boolean isAlive() {
		return thread.isAlive();
	}
}
