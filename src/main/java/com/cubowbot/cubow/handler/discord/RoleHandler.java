package com.cubowbot.cubow.handler.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.util.Arrays;
import java.util.List;

public class RoleHandler {
    public boolean checkIfModerator(List<Role> roles, Guild server) {
        String moderationRoles = ConfigHandler.getServerConfig(server.getId(), "Moderation_Roles");

        String[] moderationRoleIds = moderationRoles.split(", ");

        for (Role role : roles) {
            String roleIdStr = role.getId();
            if (Arrays.asList(moderationRoleIds).contains(roleIdStr)) {
                return true;
            }
        }

        // None of the roles match moderator roles
        return false;
    }
}