package hangman;

/**
 * The HangmanGame implements the traditional
 * pen and pencil game where the player tries
 * to guess the word picked by the computer.
 */
public class HangmanGame {
    public static final boolean DEBUG_MODE = false;

    public static void main(String[] args) {
        Game myGame = new Game(false);
        myGame.displayMenu();
    }

}
