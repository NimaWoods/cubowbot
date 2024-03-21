package com.cubowbot.cubow.Handler;

import net.dv8tion.jda.api.entities.Guild;

public class RoleHandler {
    public String getModRole(Guild server) {
        ConfigHandler configHandler = new ConfigHandler();
        String modRole = configHandler.getServerConfig(server.getId(), "Moderation_Role");

        if (modRole != "1234567890123") {
            return modRole;
        } else {
            return null;
        }
    }
}
