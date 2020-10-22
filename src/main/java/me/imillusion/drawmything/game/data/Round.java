package me.imillusion.drawmything.game.data;

import lombok.AccessLevel;
import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.game.GameState;
import me.imillusion.drawmything.game.arena.Arena;
import me.imillusion.drawmything.utils.ActionBarUtil;
import me.imillusion.drawmything.utils.SimplePlaceholder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Round {

    @Getter(AccessLevel.NONE)
    private Random random = new Random();
    @Getter(AccessLevel.NONE)
    private final DrawPlugin main;

    private DrawPlayer drawer;
    private Set<UUID> drawnPlayers = new HashSet<>();
    private List<UUID> toDraw;

    private Set<UUID> guessedPlayers = new HashSet<>();

    private int roundNum = 0;

    private String word;
    private String obfuscated;

    private Arena arena;

    private boolean intermission;
    private int intermissionTime;
    private int drawingTime;


    public Round(DrawPlugin main, Arena arena, List<UUID> toDraw) {
        this.main = main;
        this.arena = arena;
        this.toDraw = toDraw;
    }

    private void deobfuscateWord()
    {
        StringBuilder builder = new StringBuilder(obfuscated);

        for (int i = 0; i < word.length(); i++)
            if (random.nextInt(100) < 27)
                builder.setCharAt(i, word.charAt(i));

        obfuscated = builder.toString();
    }

    /**
     * Picks someone to draw that hasn't drawn during the round
     *
     * @return - The new Drawer
     * @see DrawPlayer
     */
    public DrawPlayer pickDrawer()
    {
        arena.getCanvas().clear();
        guessedPlayers.clear();

        if (drawer != null) {
            drawnPlayers.add(drawer.getUuid());
            drawer.getPlayer().setWalkSpeed(0.2f);
            for (ItemStack item : drawer.getPlayer().getInventory().getContents())
                if (item != null && main.getToolManager().getToolByItem(item) != null)
                    drawer.getPlayer().getInventory().remove(item);

            drawer.getPlayer().getActivePotionEffects().forEach(effect -> drawer.getPlayer().removePotionEffect(effect.getType()));
        }

        UUID uuid = toDraw.size() == 1 ? toDraw.remove(0) : toDraw.remove(random.nextInt(toDraw.size()));

        drawer = main.getPlayerManager().get(uuid);

        drawer.getPlayer().teleport(arena.getMap().getDrawLocation());

        drawer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10000, 250, false, false), true);
        drawer.getPlayer().setWalkSpeed(0);
        arena.sendScoreboard();
        return drawer;
    }

    /**
     * Picks a word
     *
     * @return - The word picked
     */
    public String pickWord()
    {
        List<String> words = main.getWords().getWords();

        word = words.get(random.nextInt(words.size()));

        obfuscated = word.replaceAll("\\w", "_");
        return word;
    }

    /**
     * Resets the round.
     */
    public void reset()
    {
        toDraw.removeIf(drawnPlayers::contains);

        if (drawer != null && drawer.getPlayer() != null) {
            toDraw.add(drawer.getUuid());
            drawer.getPlayer().teleport(arena.getMap().getSpawnLocation());
            drawer.getPlayer().getActivePotionEffects().forEach(effect -> drawer.getPlayer().removePotionEffect(effect.getType()));
            drawer.getPlayer().setWalkSpeed(0.2f);
        }

        toDraw.addAll(drawnPlayers);
        drawnPlayers.clear();
        guessedPlayers.clear();
        drawer = null;
        obfuscated = "";
        word = "";
        roundNum++;

        intermission = true;
        intermissionTime = 0;

        ActionBarUtil.sendActionbarMessage(" ", arena.getPlayers().toArray(new Player[]{}));

    }

    /**
     * Adds a player to the current round
     *
     * @param uuid - The UUID of the player
     */
    public void addPlayer(UUID uuid)
    {
        toDraw.add(uuid);
    }

    public void addPoints(UUID uuid)
    {
        int points = (int) (((double) drawingTime / main.getSettings().getDrawingTime()) * 200) - (guessedPlayers.size() * 20);

        arena.setPoints(uuid, arena.getPoints(uuid) + points);
    }

    /**
     * Removes a player from the current round
     *
     * @param uuid - The UUID of the player
     */
    public void removePlayer(UUID uuid)
    {
        toDraw.remove(uuid);
        drawnPlayers.remove(uuid);
        guessedPlayers.remove(uuid);

        if (arena.getGame().getGameState() != GameState.IN_GAME)
            return;

        if (arena.getUUIDs().size() == 1) {
            arena.getGame().end();
            return;
        }

        if (drawer != null && drawer.getUuid().equals(uuid) && !toDraw.isEmpty()) {
            drawer = null;
            pickDrawer();
        }


        if (guessedPlayers.size() == arena.getUUIDs().size() - 1)
            handleSwitch();
    }

    public void tick()
    {
        if (roundNum > 3)
            return;

        if (arena.getUUIDs().size() == 1) {
            arena.getGame().end();
            return;
        }

        if (intermission) {
            if (intermissionTime++ == 5)
                startRound();
            return;
        }

        if (guessedPlayers.size() >= (arena.getPlayers().size() - 1) || --drawingTime <= 0) {
            handleSwitch();
            return;
        }

        if (drawingTime % (main.getSettings().getDrawingTime() / 2) == 0)
            deobfuscateWord();

        Player[] nonGuessed = arena.getPlayers().stream()
                .filter(player -> !guessedPlayers.contains(player.getUniqueId())).collect(Collectors.toList()).toArray(new Player[]{});

        ActionBarUtil.sendActionbarMessage("&8Round #" + roundNum + " | &6" + obfuscated + " &8| &a" + (drawingTime / 60) + ":" + (drawingTime % 60 < 10 ? "0" : "") + (drawingTime % 60), nonGuessed);
        ActionBarUtil.sendActionbarMessage("&8Round #" + roundNum + " | &6" + word + " &8| &a" + (drawingTime / 60) + ":" + (drawingTime % 60 < 10 ? "0" : "") + (drawingTime % 60), drawer.getPlayer());

    }

    private void startRound()
    {
        intermission = false;
        intermissionTime = 0;
        drawingTime = main.getSettings().getDrawingTime();
        guessedPlayers.clear();

        pickWord();
        pickDrawer();

        main.getTitles().playTitle("drawer-word-chosen", new SimplePlaceholder[]{new SimplePlaceholder("%word%", word)}, drawer.getPlayer());
        drawer.getPlayer().sendMessage(
                ChatColor.translateAlternateColorCodes('&',
                        main.getMessages().getMessage("word-chosen")
                                .replace("%prefix%", main.getMessages().getPrefix())
                                .replace("%word%", word)));
    }

    private void handleSwitch()
    {
        if (toDraw.isEmpty()) {
            reset();
            if (roundNum > main.getSettings().getRounds())
                arena.getGame().end();
            return;
        }

        intermission = true;
        intermissionTime = 0;

        if (drawer.getPlayer() != null) {
            drawnPlayers.add(drawer.getUuid());

            for (ItemStack item : drawer.getPlayer().getInventory().getContents())
                if (item != null && main.getToolManager().getToolByItem(item) != null)
                    drawer.getPlayer().getInventory().remove(item);

            drawer.getPlayer().teleport(arena.getMap().getSpawnLocation());
            drawer.getPlayer().getActivePotionEffects().forEach(effect -> drawer.getPlayer().removePotionEffect(effect.getType()));
            drawer.getPlayer().setWalkSpeed(0.2f);
        }

        guessedPlayers.clear();

        drawer = null;

        obfuscated = "";

        ActionBarUtil.sendActionbarMessage(" ", arena.getPlayers().toArray(new Player[]{}));

        String msg = ChatColor.translateAlternateColorCodes('&', main.getMessages().getMessage("round-end")
                .replace("%prefix%", main.getMessages().getPrefix())
                .replace("%word%", word));

        word = "";

        arena.getPlayers().forEach(player -> player.sendMessage(msg));

        if (guessedPlayers.size() == (arena.getUUIDs().size() - 1))
            arena.getPlayers().forEach(player -> main.getMessages().sendMessage(player, "round-end-everyone-guess"));
    }
}
