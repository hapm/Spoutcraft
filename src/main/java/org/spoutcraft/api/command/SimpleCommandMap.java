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
package org.spoutcraft.api.command;

import static org.bukkit.util.Java15Compat.Arrays_copyOfRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spoutcraft.api.ChatColor;
import org.spoutcraft.api.Client;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.addon.AddonDescriptionFile;

public final class SimpleCommandMap implements CommandMap {
	private final Map<String, Command> knownCommands = new HashMap<String, Command>();
	private final Set<String> aliases = new HashSet<String>();
	private final Client client;

	public SimpleCommandMap(final Client client) {
		this.client = client;
		setDefaultCommands(client);
	}

	private void setDefaultCommands(final Client client) {
		register("bukkit", new VersionCommand("version", client));
		register("bukkit", new ReloadCommand("reload", client));
		register("bukkit", new AddonsCommand("addons", client));
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerAll(String fallbackPrefix, List<Command> commands) {
		if (commands != null) {
			for (Command c : commands) {
				register(fallbackPrefix, c);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean register(String fallbackPrefix, Command command) {
		return register(command.getName(), fallbackPrefix, command);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public boolean register(String label, String fallbackPrefix, Command command) {
		boolean registeredPassedLabel = register(label, fallbackPrefix, command, false);

		Iterator iterator = command.getAliases().iterator();
		while (iterator.hasNext()) {
			if (!register((String) iterator.next(), fallbackPrefix, command, true)) {
				iterator.remove();
			}
		}

		// Register to us so further updates of the commands label and aliases are postponed until its reregistered
		command.register(this);

		return registeredPassedLabel;
	}

	/**
	 * Registers a command with the given name is possible, otherwise uses fallbackPrefix to create a unique name if its not an alias
	 *
	 * @param name the name of the command, without the '/'-prefix.
	 * @param fallbackPrefix a prefix which is prepended to the command with a ':' one or more times to make the command unique
	 * @param command the command to register
	 * @return true if command was registered with the passed in label, false otherwise. If isAlias was true a return of false indicates no command was registerd If isAlias was false a return of false indicates the fallbackPrefix was used one or more times to create a unique name for the command
	 */
	private synchronized boolean register(String label, String fallbackPrefix, Command command, boolean isAlias) {
		String lowerLabel = label.trim().toLowerCase();

		if (isAlias && knownCommands.containsKey(lowerLabel)) {
			// Request is for an alias and it conflicts with a existing command or previous alias ignore it
			// Note: This will mean it gets removed from the commands list of active aliases
			return false;
		}

		String lowerPrefix = fallbackPrefix.trim().toLowerCase();
		boolean registerdPassedLabel = true;

		// If the command exists but is an alias we overwrite it, otherwise we rename it based on the fallbackPrefix
		while (knownCommands.containsKey(lowerLabel) && !aliases.contains(lowerLabel)) {
			lowerLabel = lowerPrefix + ":" + lowerLabel;
			registerdPassedLabel = false;
		}

		if (isAlias) {
			aliases.add(lowerLabel);
		} else {
			// Ensure lowerLabel isn't listed as a alias anymore and update the commands registered name
			aliases.remove(lowerLabel);
			command.setLabel(lowerLabel);
		}
		knownCommands.put(lowerLabel, command);

		return registerdPassedLabel;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean dispatch(CommandSender sender, String commandLine) throws CommandException {
		String[] args = commandLine.split(" ");

		if (args.length == 0) {
			return false;
		}

		String sentCommandLabel = args[0].toLowerCase();
		Command target = getCommand(sentCommandLabel);
		if (target == null) {
			return false;
		}

		try {
			// Note: we don't return the result of target.execute as thats success / failure, we return handled (true) or not handled (false)
			target.execute(sender, sentCommandLabel, Arrays_copyOfRange(args, 1, args.length));
		} catch (CommandException ex) {
			throw ex;
		} catch (Throwable ex) {
			throw new CommandException("Unhandled exception executing '" + commandLine + "' in " + target, ex);
		}

		// Return true as command was handled
		return true;
	}

	public synchronized void clearCommands() {
		for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
			entry.getValue().unregister(this);
		}
		knownCommands.clear();
		aliases.clear();
		setDefaultCommands(client);
	}

	public Command getCommand(String name) {
		return knownCommands.get(name.toLowerCase());
	}

	private static class VersionCommand extends Command {
		private final Client client;

		public VersionCommand(String name, Client client) {
			super(name);
			this.client = client;
			this.description = "Gets the version of Spoutcraft including any plugins in use";
			this.usageMessage = "/version [addon name]";
			this.setAliases(Arrays.asList("ver", "about"));
		}

		@Override
		public boolean execute(CommandSender sender, String currentAlias, String[] args) {
			if (args.length == 0) {
				sender.sendMessage("Spoutcraft is running " + ChatColor.GREEN + client.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + client.getVersion());
				sender.sendMessage("Spoutcraft is also sporting some funky dev build of Bukkit!");
			} else {
				StringBuilder name = new StringBuilder();

				for (String arg : args) {
					if (name.length() > 0) {
						name.append(' ');
					}
					name.append(arg);
				}

				Addon plugin = client.getAddonManager().getAddon(name.toString());

				if (plugin != null) {
					AddonDescriptionFile desc = plugin.getDescription();

					sender.sendMessage(ChatColor.GREEN + desc.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + desc.getVersion());

					if (desc.getDescription() != null) {
						sender.sendMessage(desc.getDescription());
					}

					if (desc.getWebsite() != null) {
						sender.sendMessage("Website: " + ChatColor.GREEN + desc.getWebsite());
					}

					if (!desc.getAuthors().isEmpty()) {
						if (desc.getAuthors().size() == 1) {
							sender.sendMessage("Author: " + getAuthors(desc));
						} else {
							sender.sendMessage("Authors: " + getAuthors(desc));
						}
					}
				} else {
					sender.sendMessage("Spoutcraft is not running any addon by that name.");
					sender.sendMessage("Use /addons to get a list of addons.");
				}
			}

			return true;
		}

		private String getAuthors(final AddonDescriptionFile desc) {
			StringBuilder result = new StringBuilder();
			ArrayList<String> authors = desc.getAuthors();

			for (int i = 0; i < authors.size(); i++) {
				if (result.length() > 0) {
					result.append(ChatColor.WHITE);

					if (i < authors.size() - 1) {
						result.append(", ");
					} else {
						result.append(" and ");
					}
				}

				result.append(ChatColor.GREEN);
				result.append(authors.get(i));
			}

			return result.toString();
		}
	}

	private static class ReloadCommand extends Command {
		@SuppressWarnings("unused")
		private final Client client;

		public ReloadCommand(String name, Client client) {
			super(name);
			this.client = client;
			this.description = "Reloads Spoutcraft configuration and addons";
			this.usageMessage = "/reload";
			this.setAliases(Arrays.asList("rl"));
		}

		@Override
		public boolean execute(CommandSender sender, String currentAlias, String[] args) {
			/*if (sender.isOp()) {
					spoutcraft.reload(); sender.sendMessage(ChatColor.GREEN + "Reload complete.");
				} else {
					sender.sendMessage(ChatColor.RED + "You do not have sufficient access to reload Spoutcraft.");
			}*/
			return true;
		}
	}

	private static class AddonsCommand extends Command {
		private final Client client;

		public AddonsCommand(String name, Client client) {
			super(name);
			this.client = client;
			this.description = "Gets a list of addons running on Spoutcraft";
			this.usageMessage = "/addons";
			this.setAliases(Arrays.asList("pl"));
		}

		@Override
		public boolean execute(CommandSender sender, String currentAlias, String[] args) {
			sender.sendMessage("Addons: " + getAddonList());
			return true;
		}

		private String getAddonList() {
			StringBuilder pluginList = new StringBuilder();
			Addon[] plugins = client.getAddonManager().getAddons();

			for (Addon plugin : plugins) {
				if (pluginList.length() > 0) {
					pluginList.append(ChatColor.WHITE);
					pluginList.append(", ");
				}

				pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
				pluginList.append(plugin.getDescription().getName());
			}

			return pluginList.toString();
		}
	}
}
