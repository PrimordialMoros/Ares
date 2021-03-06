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

package me.moros.ares.model;

import me.moros.atlas.cf.checker.nullness.qual.NonNull;
import me.moros.atlas.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface Participant extends ForwardingAudience {
	default boolean hasMember(@NonNull LivingEntity entity) {
		return getMembers().contains(entity);
	}

	default boolean isValid() {
		return getMembers().stream().allMatch(Participant::isValidEntity);
	}

	@NonNull Collection<LivingEntity> getMembers();

	static @NonNull Participant dummy() {
		return DummyParticipant.INSTANCE;
	}

	static boolean isValidEntity(@NonNull LivingEntity entity) {
		return (entity instanceof Player && ((Player) entity).isOnline()) || entity.isValid();
	}
}
