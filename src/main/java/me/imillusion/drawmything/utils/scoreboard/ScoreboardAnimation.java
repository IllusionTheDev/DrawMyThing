package me.imillusion.drawmything.utils.scoreboard;

import lombok.Getter;

import java.util.List;

public class ScoreboardAnimation {

    private int currentPosition;
    private List<String> rawText;

    @Getter
    private String currentText;

    public ScoreboardAnimation(List<String> rawText) {
        this.rawText = rawText;
        tick();
    }

    public void dispose()
    {
        currentPosition = 0;
        rawText = null;
        currentText = null;
    }

    void tick()
    {
        currentText = rawText.get(currentPosition++);
        if(currentPosition >= rawText.size())
            currentPosition = 0;
    }

}
