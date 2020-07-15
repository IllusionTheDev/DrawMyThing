package me.imillusion.drawmything;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import me.imillusion.drawmything.data.DrawPlayerManager;
import me.imillusion.drawmything.files.*;
import me.imillusion.drawmything.game.GameCountdown;
import me.imillusion.drawmything.game.GameManager;
import me.imillusion.drawmything.game.data.drawing.tools.PaintingToolManager;
import me.imillusion.drawmything.game.handler.*;
import me.imillusion.drawmything.pregame.ItemEventHandler;
import me.imillusion.drawmything.pregame.JoinHandler;
import me.imillusion.drawmything.pregame.LeaveHandler;
import me.imillusion.drawmything.pregame.WorldActionsHandler;
import me.imillusion.drawmything.utils.EntityHider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class DrawPlugin extends JavaPlugin {

    private EntityHider hider;

    private SettingsFile settings;
    private MessagesFile messages;
    private SoundsFile sounds;
    private TitlesFile titles;
    private WordsFile words;
    private ScoreboardsFile scoreboards;

    private PaintingToolManager toolManager;

    private PlayerHidingHandler hidingHandler;

    private DrawPlayerManager playerManager;

    private GameCountdown gameCountdown;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        hider = new EntityHider(this, EntityHider.Policy.WHITELIST);

        settings = new SettingsFile(this);
        messages = new MessagesFile(this);
        sounds = new SoundsFile(this);
        titles = new TitlesFile(this);
        words = new WordsFile(this);
        scoreboards = new ScoreboardsFile(this);
        MapsFile maps = new MapsFile(this);

        toolManager = new PaintingToolManager(this);
        playerManager = new DrawPlayerManager();

        Bukkit.getPluginManager().registerEvents(hidingHandler = new PlayerHidingHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new DrawingHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new ColorSelectionHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new LeaveHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new GuesserChatHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new ItemEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new DrawerMoveHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new WorldActionsHandler(), this);

        gameManager = new GameManager(this, maps.load());
        gameCountdown = new GameCountdown(this);

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");


    }

    public void sendToLobby(Player player)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(settings.getBungeeLobby());

        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

}
