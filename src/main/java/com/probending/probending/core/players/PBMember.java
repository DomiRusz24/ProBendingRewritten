package com.probending.probending.core.players;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.core.placeholders.PlaceholderObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PBMember implements PlaceholderObject {

    private final String name;

    private final String uuid;

    private ArrayList<Role> roles = new ArrayList<>();

    private PBPlayer player = null;

    public PBMember(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
        if (ProBending.playerDataM.exists(UUID.fromString(uuid))) {
            player = (PBPlayer) ProBending.playerDataM.getPlayer(name, UUID.fromString(uuid));
        }
    }

    public PBMember(String name, String uuid, List<Role> roles) {
        this.name = name;
        this.uuid = uuid;
        this.roles.addAll(roles);
        if (ProBending.playerDataM.exists(UUID.fromString(uuid))) {
            player = (PBPlayer) ProBending.playerDataM.getPlayer(name, UUID.fromString(uuid));
        }
    }

    public void setPBPlayer(PBPlayer player) {
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public static PBMember getFromString(String string) {
        String[] split = string.split("%");
        String uuid = split[0];
        String name = split[1];
        ArrayList<Role> roles = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (i == 0 || i == 1) continue;
            roles.add(ProBending.playerM.getRole(split[i]));
        }
        return new PBMember(name, uuid, roles);
    }

    public void addRoles(Role... role) {
        roles.addAll(Arrays.asList(role));
    }

    public void addRoles(String... role) {
        for (String s : role) {
            roles.add(ProBending.playerM.getRole(s));
        }
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public String toDBString() {
        StringBuilder s = new StringBuilder(uuid).append("%").append(name);
        for (Role role : roles) {
            s.append("%").append(role.getId());
        }
        return s.toString();
    }

    public boolean hasRole(String role) {
        for (Role r : roles) {
            if (r.getId().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRole(Role role) {
        for (Role r : roles) {
            if (r.getId().equals(role.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnline() {
        if (player == null) return false;
        player = (PBPlayer) ProBending.playerDataM.getPlayer(name, UUID.fromString(uuid));
        return player != null;
    }

    public PBPlayerWrapper getPlayer() {
        if (isOnline()) {
            return player.getOnlinePlayer();
        } else {
            return null;
        }
    }

    @Override
    public String onPlaceholderRequest(String param) {
        switch (param.toLowerCase()) {
            case "name":
                return name;
        }
        for (Role role : ProBending.playerM.getAllRoles()) {
            if (param.equals(role.getId())) {
                if (hasRole(role)) {
                    return role.getName();
                } else {
                    return "";
                }
            } else if (param.equals(role.getId() + "_prefix")) {
                if (hasRole(role)) {
                    return role.getPrefix();
                } else {
                    return "";
                }
            }
        }
        return null;
    }

    @Override
    public String placeHolderPrefix() {
        return "member";
    }

    @Override
    public PluginCore getCorePlugin() {
        return ProBending.plugin;
    }
}
