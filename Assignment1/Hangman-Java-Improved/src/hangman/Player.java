package hangman;

import java.io.Serializable;

/**
 * Player represent an player in the game,
 * holding the players name and score.
 */
class Player implements Serializable, Comparable<Player> {
    private final String name;
    private final int scores;

    public Player(String name, int scores) {
        this.name = name;
        this.scores = scores;
    }

    public String getName() {
        return name;
    }

    public int getScores() {
        return scores;
    }

    @Override
    public int compareTo(Player player) {
        if (this.getScores() < player.getScores()) {
            return -1;
        } else if (this.getScores() > player.getScores()) {
            return 1;
        }
        return 0;
    }
}
