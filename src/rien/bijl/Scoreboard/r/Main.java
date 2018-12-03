package rien.bijl.Scoreboard.r;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import rien.bijl.Scoreboard.r.board.App;
import rien.bijl.Scoreboard.r.triggers.onCombat;
import rien.bijl.Scoreboard.r.util.ConfigControl;

import java.util.HashMap;

/**
 * Created by Rien on 21-10-2018.
 */
public class Main extends JavaPlugin {

    public static Main instance;
    public static boolean papi = false;
    public static Scoreboard empty;

    public static HashMap<String, App> apps = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        ConfigControl.get().createDataFiles();

        empty = getServer().getScoreboardManager().getNewScoreboard();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            papi = true;
        else
            papi = false;

        getCommand("sb").setExecutor(new CommandManager());

        // Events
//        this.getServer().getPluginManager().registerEvents(new onCombat(), this);

        new Metrics(this);

        loadBoards();
    }

    public static void loadBoards()
    {
        newApp("board", true); // Default board

        for(String s : ConfigControl.get().gc("settings").getStringList("enabled-boards"))
        {
            Main.instance.getLogger().info("Attempting to start app-creator for: " + s);
            if(ConfigControl.get().gc("settings").isConfigurationSection(s)) newApp(s, false);
            else Main.instance.getLogger().severe("Tried enabling board '" + s + "', but it does not exist!");
        }
    }

    public static void disolveBoards()
    {
        for(App app : apps.values())
            app.cancel();
        apps.clear();
    }

    public static void newApp(String board, boolean isdefault)
    {
        App app = new App(board);
        if(ConfigControl.get().gc("settings").getBoolean("settings.safe-mode"))
            app.runTaskTimer(Main.instance, 1L, 1L);
        else app.runTaskTimerAsynchronously(Main.instance, 1L, 1L);
        apps.put(board, app);
        Main.instance.getLogger().info("Loaded app handler for board: " + board);
        app.isdefault = isdefault;
    }

}
