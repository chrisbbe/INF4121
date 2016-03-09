package hangman;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private static final String[] wordForGuessing = {"computer", "programmer", "software", "debugger", "compiler",
            "developer", "algorithm", "array", "method", "variable"};

    private String guessWord;
    private StringBuffer dashedWord;
    private FileReadWriter fileReadWriter;

    public Game(boolean autoStart) {
        guessWord = getRandWord();
        dashedWord = getDashedWord(guessWord);
        fileReadWriter = new FileReadWriter();
        if (autoStart) {
            displayMenu();
        }
    }

    private String getRandWord() {
        Random rand = new Random();
        return wordForGuessing[rand.nextInt(9)];
    }

    public void displayMenu() {
        System.out.println("Welcome to #Hangman# game. Please, try to guess my secret word.\n"
                + "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
                + "'HELP' to cheat and 'EXIT' to quit the game.");
        findLetterAndPrintIt();
    }

    private void findLetterAndPrintIt() {
        boolean isHelpUsed = false;
        String letter;
        StringBuffer dashBuff = new StringBuffer(dashedWord);
        int mistakes = 0;

        while (!dashBuff.toString().equals(guessWord)) {
            System.out.println("The secret word is: " + printDashes(dashBuff));
            System.out.println("DEBUG " + guessWord);

            System.out.println("Enter your guess(1 letter allowed): ");
            Scanner input = new Scanner(System.in);
            letter = input.next();

            if (letter.matches("[a-z]")) {
                mistakes += testLetter(dashBuff, letter);
            }

            isHelpUsed = menu(letter, dashBuff);
        }

        if (!isHelpUsed) {
            System.out.println("You won with " + mistakes + " mistake(s).");
            System.out.println("The secret word is: " + printDashes(dashBuff));

            System.out.println("Please enter your name for the top scoreboard:");
            Scanner input = new Scanner(System.in);
            String playerName = input.next();

            fileReadWriter.readFromFile();
            fileReadWriter.myArr.add(new Players(playerName, mistakes));
            fileReadWriter.writeToFile();
            fileReadWriter.printAndSortScoreBoard();
        } else {
            System.out.println("You won with " + mistakes + " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
            System.out.println("The secret word is: " + printDashes(dashBuff));
        }

        // restart the game
        new Game(true);
    }

    private boolean menu(String letter, StringBuffer dashBuff) {
        if (letter.equals(Command.restart.toString())) {
            new Game(true);
        } else if (letter.equals(Command.top.toString())) {
            fileReadWriter.readFromFile();
            fileReadWriter.printAndSortScoreBoard();
            new Game(true);
        } else if (letter.equals(Command.exit.toString())) {
            System.exit(1);
        } else if (letter.equals(Command.help.toString())) {
            getAndPrintHelp(dashBuff);
            return true;
        }
        return false;
    }

    /**
     * Test if guessed letter is present in word, and reveals letter
     * in dashBuff if letter is present, return 1 if not present.
     *
     * @param dashBuff to check presence of letter in.
     * @param letter   to look for.
     * @return 1 if not present, 0 if present.
     */
    private int testLetter(StringBuffer dashBuff, String letter) {
        int counter = 0;
        for (int i = 0; i < guessWord.length(); i++) {
            String currentLetter = Character.toString(guessWord.charAt(i));
            if (letter.equals(currentLetter)) {
                ++counter;
                dashBuff.setCharAt(i, letter.charAt(0));
            }
        }

        if (counter == 0) {
            System.out.printf("Sorry! There are no unrevealed letters \'%s\'. \n", letter);
            return 1;
        } else {
            System.out.printf("Good job! You revealed %d letter(s).\n", counter);
        }
        return 0;
    }

    private void getAndPrintHelp(StringBuffer dashBuff) {
        int i = 0, j = 0;
        while (j < 1) {
            if (dashBuff.charAt(i) == '_') {
                dashBuff.setCharAt(i, guessWord.charAt(i));
                ++j;
            }
            ++i;
        }
        System.out.println("The secret word is: " + printDashes(dashBuff));
    }

    private StringBuffer getDashedWord(String word) {
        StringBuffer dashes = new StringBuffer("");
        for (int i = 0; i < word.length(); i++) {
            dashes.append("_");
        }
        return dashes;
    }

    private String printDashes(StringBuffer word) {
        String toDashes = "";
        for (int i = 0; i < word.length(); i++) {
            toDashes += (" " + word.charAt(i));
        }
        return toDashes;
    }

}
