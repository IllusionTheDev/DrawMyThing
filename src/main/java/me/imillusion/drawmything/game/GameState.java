package me.imillusion.drawmything.game;

public enum GameState {
    PREGAME, COUNTDOWN, IN_GAME, FINISHED;

    public boolean canJoin() {
        return this == PREGAME || this == COUNTDOWN;
    }
}
