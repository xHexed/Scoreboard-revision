package rien.bijl.Scoreboard.r;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import rien.bijl.Scoreboard.r.board.App;
import rien.bijl.Scoreboard.r.util.ConfigControl;

/**
 * Created by Rien on 21-10-2018.
 */
public class Main extends JavaPlugin {

    public static Main instance;
    public static boolean papi = false;
    public static App app;
    public static Scoreboard empty;
    public static boolean longline = false;


    public void onEnable() {
        Main.instance = this;
        ConfigControl.get().createDataFiles();

        Main.empty = getServer().getScoreboardManager().getNewScoreboard();
        Main.longline = ConfigControl.get().gc("settings").getBoolean("settings.longline");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
        {
            Main.papi = true;
        }

        getCommand("sb").setExecutor(new CommandManager());

        new Metrics(this);
        newApp();
    }

    public static void newApp()
    {
        if(ConfigControl.get().gc("settings").getBoolean("settings.safe-mode"))
            (Main.app = new App()).runTaskTimer(Main.instance, 1L, 1L);
//            Main.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, (Main.app = new App()), 1L, 1L);
        else (Main.app = new App()).runTaskTimerAsynchronously(Main.instance, 1L, 1L);
    }

}
