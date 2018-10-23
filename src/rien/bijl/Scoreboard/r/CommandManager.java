package rien.bijl.Scoreboard.r;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rien.bijl.Scoreboard.r.board.Row;
import rien.bijl.Scoreboard.r.board.ScoreboardHolder;
import rien.bijl.Scoreboard.r.util.ConfigControl;
import rien.bijl.Scoreboard.r.util.Func;

/**
 * Created by Rien on 23-10-2018.
 */
public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(!(commandSender instanceof Player))
        {
            commandSender.sendMessage("This is a player-only command!");
        } else {

            Player player = (Player) commandSender;

            if(args.length < 1)
            {
                Func.msg(player, "Too few arguments!");
                help(player);
            } else {
                if(args[0].equalsIgnoreCase("rowhandlers") && Func.perm(player, "rowhandlers"))
                {
                    Func.msg(player, "Rows are handled in the following way:");
                    int count = 1;
                    for(Row r : Main.app.getRows())
                    {
                        String msg = "Row " + count + ": ";
                        if(r.active) msg = msg + "&7[&aactive&7] "; else msg = msg + "&7[&cactive&7] ";
                        if(r.static_line) msg = msg + "&7[&astatic&7] ";
                        if(r.placeholders && Main.papi) msg = msg + "&7[&aplaceholders&7] ";
                        if(r.placeholders && !Main.papi) msg = msg + "&7[&cplaceholders&7] ";
                        Func.smsg(player, msg);
                        count++;
                    }

                } else if(args[0].equalsIgnoreCase("reload") && Func.perm(player, "reload")) {

                    Main.app.cancel();
                    Main.app = null;

                    ConfigControl.get().reloadConfigs();

                    Main.newApp();
                    Func.smsg(player, "Scoreboard reloaded");

                } else if(args[0].equalsIgnoreCase("toggle") && Func.perm(player, "toggle")) {
                    ScoreboardHolder holder = null;
                    for(ScoreboardHolder h : Main.app.holders)
                        if(h.player == player) holder = h;

                    if(holder != null)
                    {
                        player.setScoreboard(Main.empty);
                        Main.app.unregisterHolder(holder);
                    } else {
                        Main.app.registerHolder(new ScoreboardHolder(Main.app, player));
                    }

                    Func.smsg(player, "Scoreboard toggled");

                } else {
                    Func.msg(player,"Unknown command!");
                    help(player);
                }
            }
        }

        return false;
    }

    private void help(Player player)
    {
        Func.smsg(player, "/sb rowhandlers (How the plugin classified the rows)");
        Func.smsg(player, "/sb toggle (Toggle scoereboard for you)");
        Func.smsg(player, "/sb reload (Reload config and application)");
    }
}
