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
package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.CRCManager;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.io.Download;
import org.spoutcraft.client.io.FileDownloadThread;
import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketPreCacheFile implements SpoutPacket{
	private static byte[] downloadBuffer = new byte[16384];
	private boolean cached = false;
	private boolean url = false;
	private long expectedCRC;
	private String file;
	private String plugin;

	public PacketPreCacheFile() {

	}

	public PacketPreCacheFile(String plugin, String file, long expectedCRC, boolean url) {
		this.file = file;
		this.plugin = plugin;
		this.expectedCRC = expectedCRC;
		this.url = url;
	}

	public int getNumBytes() {
		return 10 + PacketUtil.getNumBytes(file) + PacketUtil.getNumBytes(plugin);
	}

	public void readData(DataInputStream input) throws IOException {
		this.cached = input.readBoolean();
		this.url = input.readBoolean();
		this.expectedCRC = input.readLong();
		this.file = PacketUtil.readString(input);
		this.plugin = PacketUtil.readString(input);
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeBoolean(this.cached);
		output.writeBoolean(this.url);
		output.writeLong(this.expectedCRC);
		PacketUtil.writeString(output, this.file);
		PacketUtil.writeString(output, this.plugin);
	}

	public void run(int playerId) {
		if (!FileUtil.canCache(file)) {
			System.out.println("WARNING, " + plugin + " tried to cache an invalid file type: " + file);
			return;
		}
		final File directory = new File(FileUtil.getCacheDirectory(), plugin);
		if (!directory.exists()) {
			directory.mkdir();
		}
		final String fileName = FileUtil.getFileName(file);
		final File expected = new File(directory, fileName);
		if (expected.exists()) {
			long crc = FileUtil.getCRC(expected, downloadBuffer);
			this.cached = expectedCRC != 0 && crc == expectedCRC;
		}
		if (!cached) {
			final long finalCRC = expectedCRC;
			CRCManager.setCRC(fileName, finalCRC);
			if (expected.exists()) {
				expected.delete();
			}
			// Request copy of file
			if (!url) {
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(this);
			} else { // Begin download
				Runnable queued = null;
				if (FileUtil.isImageFile(fileName)) {
					queued = new Runnable() {
						public void run() {
							long crc = FileUtil.getCRC(expected, new byte[16384]);
							System.out.println("Downloaded File " + fileName + "'s CRC " + crc + ", expected CRC: " + expectedCRC);
							if (crc == finalCRC) {
								CustomTextureManager.getTextureFromUrl(plugin, fileName);
							} else {
								System.out.println("WARNING, Downloaded File " + fileName + "'s CRC " + crc + " did not match the expected CRC: " + finalCRC);
							}
						}
					};
				}
				Download data = new Download(fileName, directory, file, queued);
				FileDownloadThread.getInstance().addToDownloadQueue(data);
			}
		} else {
			if (FileUtil.isImageFile(fileName)) {
				CustomTextureManager.getTextureFromUrl(plugin, fileName);
			}
		}
	}

	public void failure(int playerId) {

	}

	public PacketType getPacketType() {
		return PacketType.PacketPreCacheFile;
	}

	public int getVersion() {
		return 0;
	}
}
