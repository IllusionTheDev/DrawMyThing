package me.imillusion.drawmything;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import lombok.Getter;
import me.imillusion.drawmything.cleanup.CleanupAction;
import me.imillusion.drawmything.cleanup.ServerType;
import me.imillusion.drawmything.data.DrawPlayerManager;
import me.imillusion.drawmything.files.*;
import me.imillusion.drawmything.game.GameCountdown;
import me.imillusion.drawmything.game.GameManager;
import me.imillusion.drawmything.game.arena.ArenaMap;
import me.imillusion.drawmything.game.data.drawing.tools.PaintingToolManager;
import me.imillusion.drawmything.game.handler.*;
import me.imillusion.drawmything.placeholders.PAPIHook;
import me.imillusion.drawmything.pregame.ItemEventHandler;
import me.imillusion.drawmything.pregame.JoinHandler;
import me.imillusion.drawmything.pregame.LeaveHandler;
import me.imillusion.drawmything.pregame.WorldActionsHandler;
import me.imillusion.drawmything.utils.EntityHider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Getter
public class DrawPlugin extends JavaPlugin {

    private EntityHider hider;

    private SettingsFile settings;
    private MessagesFile messages;
    private SoundsFile sounds;
    private TitlesFile titles;


    private WordsFile words;
    private ScoreboardsFile scoreboards;
    private ItemFile items;

    private PaintingToolManager toolManager;

    private PlayerHidingHandler hidingHandler;

    private DrawPlayerManager playerManager;

    private GameCountdown gameCountdown;
    private GameManager gameManager;

    private CleanupAction action;

    public static boolean hookPlaceholders() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public void onEnable() {

        if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            Bukkit.getLogger().warning("This plugin is missing dependencies (ProtocolLib)");
            Bukkit.getLogger().warning("Please install missing dependencies and restart the server");
            Bukkit.getLogger().warning("for the plugin to work.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (getDataFolder().listFiles().length == 0)
            Bukkit.getLogger().warning("Running first-time setup");

        hider = new EntityHider(this, EntityHider.Policy.WHITELIST);
        playerManager = new DrawPlayerManager();

        setupFiles();

        MapsFile maps = new MapsFile(this);
        toolManager = new PaintingToolManager(this);

        setupListeners();

        List<ArenaMap> loadedMaps = maps.load();

        if (loadedMaps.isEmpty()) {
            Bukkit.getLogger().warning("The maps.yml file hasn't been set up yet.");
            Bukkit.getLogger().warning("Please insert maps into maps.yml (plugins -> DrawMyThing -> maps.yml");
            Bukkit.getLogger().warning("and restart the server once you are done.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        gameManager = new GameManager(this, loadedMaps);
        gameCountdown = new GameCountdown(this);

        if (hookPlaceholders())
            registerExpansion(new PAPIHook(this));

        setupCleanup();
    }

    @Override
    public void onDisable() {
        scoreboards.dispose();
    }

    private void setupFiles() {
        settings = new SettingsFile(this);
        messages = new MessagesFile(this);
        sounds = new SoundsFile(this);
        titles = new TitlesFile(this);
        words = new WordsFile(this);
        scoreboards = new ScoreboardsFile(this);
        items = new ItemFile(this);
    }

    private void setupListeners() {
        Bukkit.getPluginManager().registerEvents(hidingHandler = new PlayerHidingHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new DrawingHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new ColorSelectionHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new LeaveHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new GuesserChatHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new ItemEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new DrawerMoveHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new WorldActionsHandler(), this);
    }

    private void setupCleanup()
    {
        Optional<CleanupAction> action = Enums.getIfPresent(CleanupAction.class, settings.getConfiguration().getString("action"));

        if (!action.isPresent())
            Bukkit.getLogger().warning("Invalid cleanup action, defaulting to KICK");

        this.action = action.or(CleanupAction.KICK);

        ServerType type = ServerType.valueOf(settings.getConfiguration().getString("server-type", "SPIGOT"));

        if (type == ServerType.BUNGEE)
            Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void registerExpansion(Object expansion) {
        try {
            expansion.getClass().getDeclaredMethod("register").invoke(expansion);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            try {
                Class.forName("PlaceholderAPI").getDeclaredMethod("registerExpansion", expansion.getClass()).invoke(null, expansion);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }
}
