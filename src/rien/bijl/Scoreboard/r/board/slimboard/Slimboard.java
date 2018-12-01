package rien.bijl.Scoreboard.r.board.slimboard;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;
import rien.bijl.Scoreboard.r.Main;
import rien.bijl.Scoreboard.r.board.App;

import java.util.ArrayList;
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
        this.objective = this.board.registerNewObjective("sb1", "sb2");
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

        string = prep(string);
        ArrayList<String> parts = null;
        if(App.longline) parts = getPartsForLongline(string); else parts = getPartsForShortline(string);

        t.setPrefix(parts.get(0));
        t.setSuffix(parts.get(1));
    }


    /*
    Parter
     */
    private String prep(String color)
    {
        ArrayList<String> parts = null;
        if(App.longline) parts = getPartsForLongline(color); else parts = getPartsForShortline(color);
        return parts.get(0) + "§f" + getLastColor(parts.get(0)) + parts.get(1);

    }

    private String getLastColor(String s)
    {
        String last = ChatColor.getLastColors(s);
        if(last == null)
            return "";
        return last;
    }

    private ArrayList<String> getPartsForShortline(String s)
    {

        ArrayList<String> parts = new ArrayList<String>();

        if(ChatColor.stripColor(s).length() > 16)
        {
            parts.add(s.substring(0, 16));

            String s2 = s.substring(16, s.length());
            if(s2.length() > 16)
                s2 = s2.substring(0, 16);
            parts.add(s2);
        } else {
            parts.add(s);
            parts.add("");
        }

        return parts;
    }
    private ArrayList<String> getPartsForLongline(String s)
    {

        ArrayList<String> parts = new ArrayList<String>();

        if(ChatColor.stripColor(s).length() > 64)
        {
            parts.add(s.substring(0, 64));

            String s2 = s.substring(64, s.length());
            if(s2.length() > 64)
                s2 = s2.substring(0, 64);
            parts.add(s2);
        } else {
            parts.add(s);
            parts.add("");
        }

        return parts;

    }



}
