package com.probending.probending.core.team;

import com.probending.probending.command.abstractclasses.Command;
import me.domirusz24.plugincore.config.annotations.Language;
import me.domirusz24.plugincore.core.placeholders.PlaceholderObject;
import com.probending.probending.core.players.PBMember;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.managers.PAPIManager;
import me.clip.placeholderapi.PlaceholderAPI;
import me.domirusz24.plugincore.core.team.AbstractTeam;
import org.bukkit.entity.Player;

public class TeamInvite {

    private final Player sender;
    private final PBPlayer reciever;
    private final AbstractTeam<PBMember> team;
    private final long time;

    public TeamInvite(Player sender, PBPlayer receiver, AbstractTeam<PBMember> team, long duration) {
        this.sender = sender;
        this.reciever = receiver;
        this.team = team;
        this.time = System.currentTimeMillis() + duration;
    }

    @Language("Team.Invite")
    public static String LANG_INVITE = "Player %player_name% has invited you to his team %team_name%!||Type in /team accept to accept the invite!";

    public void send() {
        reciever.getOnlinePlayer().getPlayer().sendMessage(PlaceholderAPI.setPlaceholders(sender, PAPIManager.setPlaceholders(team, LANG_INVITE)));
    }

    @Language("Team.InviteAccept")
    public static String LANG_INVITE_ACCEPT = "Player %player_name% has joined your team!";

    @Language("Team.InviteExpire")
    public static String LANG_EXPIRED = "This invite has expired!";

    public void accept() {
        if (time >= System.currentTimeMillis()) {
            team.addPlayer(new PBMember(reciever.getName(), reciever.getUuid()));
            if (sender.isOnline()) {
                sender.sendMessage(PlaceholderAPI.setPlaceholders(reciever.getOnlinePlayer().getPlayer(), LANG_INVITE_ACCEPT));
                reciever.getOnlinePlayer().getPlayer().sendMessage(Command.LANG_SUCCESS);
            }
        } else {
            reciever.getOnlinePlayer().getPlayer().sendMessage(LANG_EXPIRED);
        }
    }


}
