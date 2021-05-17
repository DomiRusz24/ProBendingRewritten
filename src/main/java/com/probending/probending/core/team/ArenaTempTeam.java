package com.probending.probending.core.team;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.players.PBPlayerWrapper;
import org.bukkit.entity.Player;

public class ArenaTempTeam extends Team {

    private final Arena arena;
    private final TeamTag tag;

    public ArenaTempTeam(Arena arena, TeamTag tag) {
        super(tag.toString(), 3);
        this.arena = arena;
        this.tag = tag;
    }

    public TeamTag getTag() {
        return tag;
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public boolean onAddPlayer(PBPlayerWrapper player) {
        if (ProBending.teamM.getTempTeam(player.getPlayer()) != null) ProBending.teamM.getTempTeam(player.getPlayer()).removePlayer(player);
        ProBending.teamM.TEMPTEAM_BY_PLAYER.put(player.getPlayer(), this);
        return true;
    }

    @Override
    public boolean onRemovePlayer(PBPlayerWrapper player) {
        ProBending.teamM.TEMPTEAM_BY_PLAYER.remove(player.getPlayer());
        return true;
    }

    @Override
    protected String _onPlaceholderRequest(String message) {
        message = message.toLowerCase();
        if (message.equals("color")) {
            return tag.getColor();
        }
        return null;
    }
}
