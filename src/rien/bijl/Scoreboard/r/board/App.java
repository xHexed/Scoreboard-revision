package rien.bijl.Scoreboard.r.board;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rien.bijl.Scoreboard.r.Main;
import rien.bijl.Scoreboard.r.board.events.EDeintergrate;
import rien.bijl.Scoreboard.r.board.events.EIntergrate;
import rien.bijl.Scoreboard.r.util.ConfigControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rien on 21-10-2018.
 */
public class App extends BukkitRunnable {

    private Row title;
    private ArrayList<Row> rows = new ArrayList<Row>();
    private ArrayList<Player> children = new ArrayList<Player>();
    public ArrayList<ScoreboardHolder> holders = new ArrayList<ScoreboardHolder>();
    public static boolean longline = false;
    public String board;
    public boolean isdefault = false;

    public App(String board)
    {
        // conf
        App.longline = ConfigControl.get().gc("settings").getBoolean("settings.longline");
        board = board;

        //Events
        Main.instance.getServer().getPluginManager().registerEvents(new EIntergrate(this), Main.instance);
        Main.instance.getServer().getPluginManager().registerEvents(new EDeintergrate(this), Main.instance);

        // Setup title row
        List<String> lines = ConfigControl.get().gc("settings").getConfigurationSection(board+".title").getStringList("liner");
        int interval = ConfigControl.get().gc("settings").getInt(board+".title.interval");
        title = new Row((ArrayList<String>) lines, interval);

        for(int i = 1; i<200; i++)
        {
            ConfigurationSection section = ConfigControl.get().gc("settings").getConfigurationSection(board+".rows." + i);
            if(null != section)
            {
                Row row = new Row((ArrayList<String>)section.getStringList("liner"), section.getInt("interval"));
                rows.add(row);
            }
        }

        // Register already joined players
        if(board == "board") for(Player player : Main.instance.getServer().getOnlinePlayers()) new ScoreboardHolder(this, player);

    }

    public ArrayList<Row> getRows()
    {
        return this.rows;
    }

    public Row getTitle()
    {
        return this.title;
    }

    public void registerHolder(ScoreboardHolder holder)
    {
        holders.add(holder);
    }
    public void unregisterHolder(ScoreboardHolder holder)
    {
        holders.remove(holder);
    }
    public void unregisterHolder(Player player)
    {
        for(ScoreboardHolder holder : holders)
            if(holder.player == player)
            {
                holders.remove(holder);
                break;
            }
    }

    @Override
    public void run() {
        // Update rows
        title.update();
        for(Row row : this.rows)
            row.update();


        // Update scoreboards
        for(ScoreboardHolder holder : this.holders)
            holder.update();
    }
}
