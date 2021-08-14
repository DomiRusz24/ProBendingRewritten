package com.probending.probending.core.team;

import com.probending.probending.command.Languages;
import com.probending.probending.core.players.PBPlayerWrapper;
import me.domirusz24.plugincore.config.annotations.Language;
import me.domirusz24.plugincore.config.language.dynamics.ClickableMessage;
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

    public static ClickableMessage LANG_INVITE = ClickableMessage.of("Player %player_name% has invited you to his team %team_name%!||Click here to accept!", "Team.Invite", "Click here to accept!", "/team accept");

    public void send() {
        reciever.getOnlinePlayer().getPlayer().spigot().sendMessage(LANG_INVITE.getText(team, PBPlayerWrapper.of(sender)));
    }

    @Language("Team.InviteAccept")
    public static String LANG_INVITE_ACCEPT = "Player %player_name% has joined your team!";

    @Language("Team.InviteExpire")
    public static String LANG_EXPIRED = "This invite has expired!";

    public void accept() {
        if (time >= System.currentTimeMillis()) {
            team.addPlayer(new PBMember(reciever.getName(), reciever.getUuid().toString()));
            if (sender.isOnline()) {
                sender.sendMessage(PlaceholderAPI.setPlaceholders(reciever.getOnlinePlayer().getPlayer(), LANG_INVITE_ACCEPT));
                reciever.getOnlinePlayer().getPlayer().sendMessage(Languages.SUCCESS);
            }
        } else {
            reciever.getOnlinePlayer().getPlayer().sendMessage(LANG_EXPIRED);
        }
    }


}
