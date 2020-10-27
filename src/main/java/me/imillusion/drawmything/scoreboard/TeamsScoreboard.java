package me.imillusion.drawmything.scoreboard;

import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.events.ScoreboardDisplayEvent;
import me.imillusion.drawmything.utils.ColorConverter;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

import static org.bukkit.Bukkit.getScoreboardManager;
import static org.bukkit.scoreboard.DisplaySlot.SIDEBAR;

/**
 * A 32 character no flicker scoreboard implementation, fast and lightweight.
 */
public class TeamsScoreboard {

    public static final int MAX_LINES = 15;
    private static final String[] BLANKS = new String[MAX_LINES];

    static {
        for (int i = 0; i < MAX_LINES; i++)
            BLANKS[i] = String.valueOf(new char[]{ChatColor.COLOR_CHAR, (char) ('r' + i)});
    }

    private DrawPlugin main;

    private final Objective objective;
    @Getter
    private final org.bukkit.scoreboard.Scoreboard board;
    private final Team[] teams = new Team[MAX_LINES];

    /**
     * Construct a new {@link TeamsScoreboard} wrapping the main {@link org.bukkit.scoreboard.Scoreboard}.
     */
    public TeamsScoreboard(DrawPlugin main) {
        this(main, getScoreboardManager().getNewScoreboard());
    }

    /**
     * Construct a new {@link TeamsScoreboard} wrapping the given {@link org.bukkit.scoreboard.Scoreboard}.
     *
     * @param board - The {@link org.bukkit.scoreboard.Scoreboard} to wrap.
     */
    public TeamsScoreboard(DrawPlugin main, Scoreboard board) {
        this.board = board;
        objective = board.registerNewObjective("test", "dummy");
        objective.setDisplaySlot(SIDEBAR);
        for (int i = 0; i < MAX_LINES; i++)
            (teams[i] = board.registerNewTeam(BLANKS[i])).addEntry(BLANKS[i]);

        this.main = main;
    }

    /**
     * Sets the title to the given value.
     *
     * @param title - The new title.
     */
    public void title(String title) {
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
    }

    /**
     * Sets the line at a given index to the given text and score.
     *
     * @param index - The index of the line.
     * @param text  - The text. ~32 characters max.
     * @return - The index of the line.
     */
    public int line(int index, Object text, int score) {
        final String colored = ChatColor.translateAlternateColorCodes('&', text.toString());

        objective.getScore(BLANKS[index]).setScore(score);

        if (colored.length() <= 16) {
            teams[index].setPrefix(colored);
            teams[index].setSuffix("");
            return index;
        }

        String prefix = colored.substring(0, 16);
        String suffix = ColorConverter.get().getLastColors(prefix) + colored.substring(16);

        if (prefix.charAt(prefix.length() - 1) == ChatColor.COLOR_CHAR)
            prefix = prefix.substring(0, prefix.length() - 1);

        if (prefix.length() > 16) {
            Bukkit.getLogger().warning("prefix line with index " + index + " (score: " + score + ") is above 16 characters");
            Bukkit.getLogger().warning("line: \"" + prefix + "\"");
            return index;
        }

        if (suffix.length() > 16) {
            Bukkit.getLogger().warning("suffix line with index " + index + " (score: " + score + ") is above 16 characters");
            Bukkit.getLogger().warning("line: \"" + prefix + "\"");
            return index;
        }

        teams[index].setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));
        teams[index].setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));

        return index;
    }

    /**
     * Sets the line at a given index to the given text with a score of 1.
     *
     * @param index - The index of the line.
     * @param text  - The text. ~32 characters max.
     * @return - The index of the line.
     */
    public int line(int index, String text) {
        return line(index, text, 1);
    }


    /**
     * Sets all lines in order from MAX_LINES to 1
     *
     * @param lines - The text to write
     */
    public void write(List<String> lines) {
        if (lines.size() < MAX_LINES)
            for (int i = 0; i < (MAX_LINES - lines.size()); i++) {
                if (!board.getTeam(BLANKS[i]).getPrefix().isEmpty() || !board.getTeam(BLANKS[i]).getSuffix().isEmpty())
                    line(i, "", i);
                board.resetScores(BLANKS[i]);
            }


        for (int i = MAX_LINES; i > (MAX_LINES - lines.size()); i--) {
            line(i - 1, lines.get(MAX_LINES - i), i - 1);
        }

        DrawPlayer target = main.getPlayerManager().getAllPlayers().stream().filter(player -> player.getScoreboard() == this).findFirst().orElse(null);

        Validate.notNull(target, "TeamsScoreboard not assigned to DrawPlayer");

        new ScoreboardDisplayEvent(this, lines, target.getUuid());
    }

    /**
     * Removes the line at the given index.
     *
     * @param index - The index of the line.
     * @return - {@code true} if the line was previously set.
     */
    public boolean remove(int index) {
        if (index >= MAX_LINES) return false;
        board.resetScores(BLANKS[index]);
        return true;
    }
}
