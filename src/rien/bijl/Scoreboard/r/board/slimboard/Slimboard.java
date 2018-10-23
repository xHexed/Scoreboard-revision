package rien.bijl.Scoreboard.r.board.slimboard;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;
import rien.bijl.Scoreboard.r.Main;

import java.util.HashMap;

/**
 * Created by Rien on 23-10-2018.
 */
public class Slimboard {

    private Player player;
    private Plugin plugin;
    private Scoreboard board;
    private Objective objective;
    private int linecount;

    private HashMap<Integer, String> cache = new HashMap<Integer, String>();

    public Slimboard(Plugin plugin, Player player, int linecount)
    {
        this.player = player;
        this.plugin = plugin;
        this.linecount = linecount;
        this.board = this.plugin.getServer().getScoreboardManager().getNewScoreboard();
        this.objective = this.board.registerNewObjective("Slimboard1", "Slimboard2", "Slimboard3");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName("...");

        int score = linecount;
        for(int i = 0; i<linecount;i++)
        {
            Team t = this.board.registerNewTeam(i + "");
            t.addEntry(ChatColor.values()[i] + "");

            this.objective.getScore(ChatColor.values()[i] + "").setScore(score);

            score--;
        }

        this.player.setScoreboard(this.board);
    }

    public void setTitle(String string)
    {
        if(string == null) string = "";
        if(Main.papi) string = PlaceholderAPI.setPlaceholders(this.player, string);

        if(cache.containsKey(-1) && cache.get(-1) == string) return;
        if(cache.containsKey(-1)) cache.remove(-1);
        cache.put(-1, string);
        this.objective.setDisplayName(string);
    }

    public void setLine(int line, String string)
    {
        Team t = this.board.getTeam((line) + "");
        if(string == null) string = "";

        if(cache.containsKey(line) && cache.get(line) == string) return;
        if(cache.containsKey(line)) cache.remove(line);
        cache.put(line, string);

        t.setPrefix(string);
    }

}
