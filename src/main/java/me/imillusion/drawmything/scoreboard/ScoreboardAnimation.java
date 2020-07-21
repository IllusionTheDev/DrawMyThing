package me.imillusion.drawmything.scoreboard;

import lombok.Getter;

import java.util.List;

public class ScoreboardAnimation {

    private int currentPosition;
    private List<String> rawText;

    @Getter
    private String currentText;

    public ScoreboardAnimation(List<String> rawText) {
        this.rawText = rawText;
        currentText = rawText.get(currentPosition++);
    }

    public void dispose()
    {
        currentPosition = 0;
        rawText = null;
        currentText = null;
    }

    protected ScoreboardAnimation clone()
    {
        return new ScoreboardAnimation(rawText);
    }

    void tick()
    {
        currentText = rawText.get(currentPosition++);
        if (currentPosition >= rawText.size())
            currentPosition = 0;
    }

}
