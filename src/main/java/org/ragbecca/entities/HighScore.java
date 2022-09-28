package org.ragbecca.entities;

import java.time.LocalDateTime;
import java.util.Date;

public class HighScore {

    private int amountOfMoves;

    private LocalDateTime momentOfAchieving;


    public HighScore(int amountOfMoves, LocalDateTime momentOfAchieving) {
        this.amountOfMoves = amountOfMoves;
        this.momentOfAchieving = momentOfAchieving;
    }

    public int getAmountOfMoves() {
        return amountOfMoves;
    }

    public void setAmountOfMoves(int amountOfMoves) {
        this.amountOfMoves = amountOfMoves;
    }

    public LocalDateTime getMomentOfAchieving() {
        return momentOfAchieving;
    }

    public void setMomentOfAchieving(LocalDateTime momentOfAchieving) {
        this.momentOfAchieving = momentOfAchieving;
    }
}
