/*
 *   Copyright 2020 Moros <https://github.com/PrimordialMoros>
 *
 *    This file is part of Ares.
 *
 *   Ares is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Ares is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with Ares.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.moros.ares.command;

import me.moros.ares.Ares;
import me.moros.ares.locale.Message;
import me.moros.ares.model.Participant;
import me.moros.ares.model.ParticipantImpl;
import me.moros.ares.model.Tournament;
import me.moros.ares.model.user.CommandUser;
import me.moros.atlas.acf.BaseCommand;
import me.moros.atlas.acf.CommandHelp;
import me.moros.atlas.acf.annotation.CommandAlias;
import me.moros.atlas.acf.annotation.CommandCompletion;
import me.moros.atlas.acf.annotation.CommandPermission;
import me.moros.atlas.acf.annotation.Description;
import me.moros.atlas.acf.annotation.HelpCommand;
import me.moros.atlas.acf.annotation.Optional;
import me.moros.atlas.acf.annotation.Subcommand;
import me.moros.atlas.kyori.adventure.text.Component;
import me.moros.atlas.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Collection;

@CommandAlias("%arescommand")
public class AresCommand extends BaseCommand {
	@HelpCommand
	@CommandPermission("ares.command.help")
	public static void doHelp(CommandUser user, CommandHelp help) {
		Message.HELP_HEADER.send(user);
		help.showHelp();
	}

	@Subcommand("list|ls")
	@CommandPermission("ares.command.list")
	@CommandCompletion("@tournaments")
	@Description("List all currently active tournaments")
	public static void onList(CommandUser user) {
		Collection<Tournament> tournaments = Ares.getManager().getTournaments();
		if (tournaments.isEmpty()) {
			Message.TOURNAMENT_LIST_EMPTY.send(user);
			return;
		}
		Message.TOURNAMENT_LIST_HEADER.send(user);
		for (Tournament tournament : Ares.getManager().getTournaments()) {
			user.sendMessage(Component.text("> ", NamedTextColor.DARK_GRAY).append(tournament.getDisplayName()));
		}
	}

	// TODO allow specifying team members?
	@Subcommand("join|j")
	@CommandPermission("ares.command.join")
	@CommandCompletion("@tournaments")
	@Description("Join an open tournament")
	public static void onJoin(Player player, @Optional Tournament tournament) {
		if (tournament == null) {
			// If no tournament is specified then select the first open tournament
			return;
		}
		Participant participant = ParticipantImpl.of(player);
		if (tournament.hasParticipant(participant)) {
			participant.sendMessage(Message.TOURNAMENT_ALREADY_JOINED.build(tournament.getDisplayName()));
			return;
		}
		if (tournament.addParticipant(participant)) {
			participant.sendMessage(Message.TOURNAMENT_JOIN_SUCCESS.build(tournament.getDisplayName()));
		} else {
			participant.sendMessage(Message.TOURNAMENT_JOIN_FAIL.build(tournament.getDisplayName()));
		}
	}

	@Subcommand("create|c|new|n")
	@CommandPermission("ares.command.create")
	@CommandCompletion("@tournaments")
	@Description("Create an new tournament")
	public static void onCreate(CommandUser user, String name) {
		// Validate name and build tournament
		// Offer tournament presets
	}
}