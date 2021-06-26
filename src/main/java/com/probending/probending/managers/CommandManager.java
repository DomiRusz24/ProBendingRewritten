package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.AbstractCommand;
import com.probending.probending.command.abstractclasses.AbstractSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.command.arena.ArenaConfigCommand;
import com.probending.probending.command.arena.ArenaControlCommand;
import com.probending.probending.command.arena.arenaconfig.ArenaGetCommand;
import com.probending.probending.command.arena.arenaconfig.ArenaSetCommand;
import com.probending.probending.command.arena.arenacontrol.*;
import com.probending.probending.command.base.ProBendingCommand;
import com.probending.probending.command.base.probending.CreateArenaCommand;
import com.probending.probending.command.base.probending.ReloadCommand;
import com.probending.probending.command.base.probending.RulesCommand;
import com.probending.probending.command.base.probending.SetSpawnCommand;
import com.probending.probending.command.forceskip.ForceStartCommand;
import com.probending.probending.command.leave.LeaveCommand;
import com.probending.probending.command.pblevel.PBLevelCommand;
import com.probending.probending.command.pbteam.PBTeamCommand;
import com.probending.probending.command.pbteam.pbteam.*;
import com.probending.probending.config.SpigotCommandsConfig;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CommandManager extends PBManager implements CommandExecutor, TabExecutor {

    private final HashSet<AbstractCommand<? extends AbstractSubCommand>> COMMANDS = new HashSet<>();

    public CommandManager(ProBending plugin) {
        super(plugin);
        plugin.getDataFolder().mkdirs();
        registerCommands();
    }

    private void registerCommands() {
        registerCommand(new ArenaControlCommand()
                .addSubCommand(new ArenaJoinCommand())
                .addSubCommand(new ArenaAddCommand())
                .addSubCommand(new ArenaRemoveCommand())
                .addSubCommand(new ArenaStartCommand())
                .addSubCommand(new ArenaStopCommand())
                .addSubCommand(new ArenaSpectateCommand())
        );

        registerCommand(new ArenaConfigCommand()
                .addSubCommand(new ArenaGetCommand())
                .addSubCommand(new ArenaSetCommand())
                .addSubCommand(new ArenaRefreshCommand())
        );

        registerCommand(new ProBendingCommand()
                .addSubCommand(new ReloadCommand())
                .addSubCommand(new RulesCommand())
                .addSubCommand(new CreateArenaCommand())
                .addSubCommand(new SetSpawnCommand())
        );

        registerCommand(new LeaveCommand());

        registerCommand(new PBLevelCommand());

        registerCommand(new ForceStartCommand());

        registerCommand(new PBTeamCommand()
                .addSubCommand(new CreateTeamCommand())
                .addSubCommand(new InviteTeamCommand())
                .addSubCommand(new TeamAcceptCommand())
                .addSubCommand(new TeamLeaveCommand())
                .addSubCommand(new TeamPlayCommand())
        );

        getConfig().save();
    }

    private SpigotCommandsConfig getConfig() {
        return ProBending.configM.getSpigotCommandsConfig();
    }

    public void registerCommand(AbstractCommand<? extends AbstractSubCommand> command) {
        COMMANDS.add(command);
        plugin.getCommand(command.getName()).setExecutor(this);
        plugin.getCommand(command.getName()).setTabCompleter(this);
        getConfig().set("commands." + command.getName() + ".usage", command.getUsage());
        String permission = "probending.command." + command.getName();
        getConfig().set("commands." + command.getName() + ".permission", permission);
        ProBending.configM.getSpigotCommandsConfig().set("permissions." + permission + ".default", "op");
        if (!command.getAliases().isEmpty()) {
            StringBuilder aliases = new StringBuilder("[");
            for (String alias : command.getAliases()) {
                aliases.append(alias).append(",");
            }
            aliases.deleteCharAt(aliases.length() - 1);
            aliases.append("]");
            getConfig().set("commands." + command.getName() + ".aliases", aliases.toString());
        }
    }

    /*
    public boolean preformCommand(CommandSender sender, String command) {
        if (command.startsWith("/")) command = command.substring(1);
        if (command.length() == 0) return false;
        List<String> args = Arrays.asList(command.split(" "));
        String name = args.get(0);
        if (args.size() > 1) {
            args = args.subList(1, args.size());
        } else {
            args = new ArrayList<>();
        }
        for (AbstractCommand<?> com : COMMANDS) {
            if (com.getName().equalsIgnoreCase(name) || (com.getAliases() != null && com.getAliases().contains(name.toLowerCase()))) {
                try {
                    if (args.size() == 1 && args.get(0).equalsIgnoreCase("help")) {
                        com.help(sender, true);
                    } else if (com.canSelfExecute(sender, args)) {
                        com.selfExecute(sender, args);
                    } else if (com.canExecuteSubCommands(sender, args)) {
                        for (AbstractSubCommand subCommand : com.getSubCommands()) {
                            if (subCommand.matching(args)) {
                                subCommand.executeCommand(sender, args);
                                return true;
                            }
                        }
                        com.help(sender, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sender.sendMessage(Command.LANG_FAIL_ERROR);
                }
                return true;
            }
        }
        return false;
    }
     */

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] strings) {
        String name = command.getName();
        List<String> args = Arrays.asList(strings);
        for (AbstractCommand<?> com : COMMANDS) {
            if (com.getName().equalsIgnoreCase(name) || (com.getAliases() != null && com.getAliases().contains(name.toLowerCase()))) {
                try {
                    if (args.size() == 1 && args.get(0).equalsIgnoreCase("help")) {
                        com.help(sender, true);
                    } else if (com.canSelfExecute(sender, args)) {
                        com.selfExecute(sender, args);
                    } else if (com.canExecuteSubCommands(sender, args)) {
                        for (AbstractSubCommand subCommand : com.getSubCommands()) {
                            if (subCommand.matching(args)) {
                                subCommand.executeCommand(sender, args);
                                return true;
                            }
                        }
                        com.help(sender, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sender.sendMessage(Command.LANG_FAIL_ERROR);
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        String name = command.getName();
        for (AbstractCommand<? extends AbstractSubCommand> com : COMMANDS) {
            if (com.getName().equalsIgnoreCase(name) || (com.getAliases() != null && com.getAliases().contains(name.toLowerCase()))) {
                return com.autoComplete(commandSender, Arrays.asList(strings));
            }
        }
        return null;
    }
}
