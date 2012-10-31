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
package org.spoutcraft.api.addon.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.error.YAMLException;

import org.spoutcraft.api.Client;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.UnsafeMethod;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.addon.AddonDescriptionFile;
import org.spoutcraft.api.addon.AddonLoader;
import org.spoutcraft.api.addon.InvalidAddonException;
import org.spoutcraft.api.addon.InvalidDescriptionException;
import org.spoutcraft.api.addon.SimpleSecurityManager;
import org.spoutcraft.api.addon.UnknownDependencyException;
import org.spoutcraft.api.addon.UnknownSoftDependencyException;
import org.spoutcraft.api.event.addon.AddonDisableEvent;
import org.spoutcraft.api.event.addon.AddonEnableEvent;

public final class JavaAddonLoader implements AddonLoader {
	private final Client client;
	private final Pattern[] fileFilters = new Pattern[] { Pattern.compile("\\.jar$"), };
	private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
	private final Map<String, AddonClassLoader> loaders = new HashMap<String, AddonClassLoader>();
	private final SimpleSecurityManager manager;
	private final double key;

	public JavaAddonLoader(Client instance, SimpleSecurityManager manager, double key) {
		client = instance;
		this.manager = manager;
		this.key = key;
	}

	public Addon loadAddon(File file) throws InvalidAddonException, InvalidDescriptionException, UnknownDependencyException {
		return loadAddon(file, false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Addon loadAddon(File file, boolean ignoreSoftDependencies) throws InvalidAddonException, InvalidDescriptionException, UnknownDependencyException {
		JavaAddon result = null;
		AddonDescriptionFile description = null;

		if (!file.exists()) {
			throw new InvalidAddonException(new FileNotFoundException(String.format("%s does not exist", file.getPath())));
		}
		try {
			JarFile jar = new JarFile(file);
			JarEntry entry = jar.getJarEntry("addon.yml");

			if (entry == null) {
				jar.close();
				throw new InvalidAddonException(new FileNotFoundException("Jar does not contain addon.yml"));
			}

			InputStream stream = jar.getInputStream(entry);

			description = new AddonDescriptionFile(stream);

			stream.close();
			jar.close();
		} catch (IOException ex) {
			throw new InvalidAddonException(ex);
		} catch (YAMLException ex) {
			throw new InvalidAddonException(ex);
		}

		File dataFolder = new File(file.getParentFile(), description.getName());
		File oldDataFolder = getDataFolder(file);

		// Found old data folder
		if (dataFolder.equals(oldDataFolder)) {
			// They are equal -- nothing needs to be done!
		} else if (dataFolder.isDirectory() && oldDataFolder.isDirectory()) {
			client.getLogger().log(Level.INFO, String.format("While loading %s (%s) found old-data folder: %s next to the new one: %s", description.getName(), file, oldDataFolder, dataFolder));
		} else if (oldDataFolder.isDirectory() && !dataFolder.exists()) {
			if (!oldDataFolder.renameTo(dataFolder)) {
				throw new InvalidAddonException(new Exception("Unable to rename old data folder: '" + oldDataFolder + "' to: '" + dataFolder + "'"));
			}
			client.getLogger().log(Level.INFO, String.format("While loading %s (%s) renamed data folder: '%s' to '%s'", description.getName(), file, oldDataFolder, dataFolder));
		}

		if (dataFolder.exists() && !dataFolder.isDirectory()) {
			throw new InvalidAddonException(new Exception(String.format("Projected datafolder: '%s' for %s (%s) exists and is not a directory", dataFolder, description.getName(), file)));
		}

		ArrayList<String> depend;

		try {
			depend = (ArrayList) description.getDepend();
			if (depend == null) {
				depend = new ArrayList<String>();
			}
		} catch (ClassCastException ex) {
			throw new InvalidAddonException(ex);
		}

		for (String addonName : depend) {
			if (loaders == null) {
				throw new UnknownDependencyException(addonName);
			}
			AddonClassLoader current = loaders.get(addonName);

			if (current == null) {
				throw new UnknownDependencyException(addonName);
			}
		}

		if (!ignoreSoftDependencies) {
			ArrayList<String> softDepend;

			try {
				softDepend = (ArrayList) description.getSoftDepend();
				if (softDepend == null) {
					softDepend = new ArrayList<String>();
				}
			} catch (ClassCastException ex) {
				throw new InvalidAddonException(ex);
			}

			for (String addonName : softDepend) {
				if (loaders == null) {
					throw new UnknownSoftDependencyException(addonName);
				}
				AddonClassLoader current = loaders.get(addonName);

				if (current == null) {
					throw new UnknownSoftDependencyException(addonName);
				}
			}
		}

		AddonClassLoader loader = null;

		try {
			URL[] urls = new URL[1];

			urls[0] = file.toURI().toURL();
			loader = new AddonClassLoader(this, urls, getClass().getClassLoader());
			Class<?> jarClass = Class.forName(description.getMain(), true, loader);
			Class<? extends JavaAddon> addon = jarClass.asSubclass(JavaAddon.class);

			manager.lock(key);
			Constructor<? extends JavaAddon> constructor = addon.getConstructor();

			result = constructor.newInstance();

			result.initialize(this, client, description, dataFolder, file, loader);
			manager.unlock(key);
		} catch (Throwable ex) {
			throw new InvalidAddonException(ex);
		}

		loaders.put(description.getName(), (AddonClassLoader) loader);

		return (Addon) result;
	}

	private File getDataFolder(File file) {
		File dataFolder = null;

		String filename = file.getName();
		int index = file.getName().lastIndexOf(".");

		if (index != -1) {
			String name = filename.substring(0, index);

			dataFolder = new File(file.getParentFile(), name);
		} else {
			// This is if there is no extension, which should not happen
			// Using _ to prevent name collision

			dataFolder = new File(file.getParentFile(), filename + "_");
		}

		return dataFolder;
	}

	public Pattern[] getAddonFileFilters() {
		return fileFilters;
	}

	public Class<?> getClassByName(final String name) {
		Class<?> cachedClass = classes.get(name);

		if (cachedClass != null) {
			return cachedClass;
		} else {
			for (String current : loaders.keySet()) {
				AddonClassLoader loader = loaders.get(current);

				try {
					cachedClass = loader.findClass(name, false);
				} catch (ClassNotFoundException cnfe) {
				}
				if (cachedClass != null) {
					return cachedClass;
				}
			}
		}
		return null;
	}

	public void setClass(final String name, final Class<?> clazz) {
		if (!classes.containsKey(name)) {
			classes.put(name, clazz);
		}
	}

	@UnsafeMethod
	public void enableAddon(final Addon addon) {
		if (!(addon instanceof JavaAddon)) {
			throw new IllegalArgumentException("Addon is not associated with this AddonLoader");
		}

		if (!addon.isEnabled()) {
			JavaAddon jAddon = (JavaAddon) addon;

			String addonName = jAddon.getDescription().getName();

			if (!loaders.containsKey(addonName)) {
				loaders.put(addonName, (AddonClassLoader) jAddon.getClassLoader());
			}

			try {
				jAddon.setEnabled(true);
			} catch (Throwable ex) {
				client.getLogger().log(Level.SEVERE, "Error occurred while enabling " + addon.getDescription().getFullName() + " (Is it up to date?): " + ex.getMessage(), ex);
			}

			// Perhaps abort here, rather than continue going, but as it stands,
			// an abort is not possible the way it's currently written
			Spoutcraft.getAddonManager().callEvent(AddonEnableEvent.getInstance(jAddon));
		}
	}

	@UnsafeMethod
	public void disableAddon(Addon addon) {
		if (!(addon instanceof JavaAddon)) {
			throw new IllegalArgumentException("Addon is not associated with this AddonLoader");
		}

		if (addon.isEnabled()) {
			JavaAddon jAddon = (JavaAddon) addon;
			ClassLoader cloader = jAddon.getClassLoader();

			try {
				jAddon.setEnabled(false);
			} catch (Throwable ex) {
				client.getLogger().log(Level.SEVERE, "Error occurred while disabling " + addon.getDescription().getFullName() + " (Is it up to date?): " + ex.getMessage(), ex);
			}

			Spoutcraft.getAddonManager().callEvent(AddonDisableEvent.getInstance(jAddon));

			loaders.remove(jAddon.getDescription().getName());

			if (cloader instanceof AddonClassLoader) {
				AddonClassLoader loader = (AddonClassLoader) cloader;
				Set<String> names = loader.getClasses();

				for (String name : names) {
					classes.remove(name);
				}
			}
		}
	}
}
