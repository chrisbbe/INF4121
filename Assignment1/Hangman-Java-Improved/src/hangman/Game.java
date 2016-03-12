package hangman;

import java.util.Random;
import java.util.Scanner;

/**
 * Game implements the game control.
 */
class Game {
    private static final String[] wordForGuessing = {"computer", "programmer", "software", "debugger", "compiler",
            "developer", "algorithm", "array", "method", "variable"};

    private static final String[] hangedMan = {
"___________.._______        ",
"| .__________))______|      ",
"| | / /      ||             ",  
"| |/ /       ||             ",  
"| | /        ||.-''.        ",
"| |/         |/ _  Y        ", 
"| |          || `/,|        ", 
"| |          (`_.'          ", 
"| |         .-`--'.         ",  
"| |        /Y . . Y7        ",  
"| |       // |   | z7       ",
"| |      //  | . |  z7      ", 
"| |     ')   |   |   (`     ", 
"| |          ||'||          ", 
"| |          || ||          ", 
"| |          || ||          ", 
"| |          || ||          ", 
"| |         / | | Y         ", 
"----------|_`-' `-' |---|   ", 
"|-|-------Y Y       '-|-|   ", 
"| |        Y Y        | |   ", 
": :         Y Y       : :   ", 
". .          `'       . .   ",
"                            "};

    private final String guessWord;
    private final StringBuffer dashedWord;
    private final FileReadWriter fileReadWriter;

    public Game(boolean autoStart) {
        guessWord = getRandWord();
        dashedWord = getDashedWord(guessWord);
        fileReadWriter = new FileReadWriter();
        if (autoStart) {
            displayMenu();
        }
    }

    /**
     * Picks randomly a word and returns the
     * word.
     *
     * @return word choosed.
     */
    private String getRandWord() {
        Random rand = new Random();
        return wordForGuessing[rand.nextInt(wordForGuessing.length)];
    }

    /**
     * Prints user menu.
     */
    public void displayMenu() {
        System.out.println("Welcome to #Hangman# game. Please, try to guess my secret word.\n"
                + "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
                + "'HELP' to cheat and 'EXIT' to quit the game.");
        findLetterAndPrintIt();
    }

    private void printMan(int mistakes) {
        int i = 0;
        while(i < mistakes*3) {
            System.out.println(hangedMan[i]);
            i++;
        }
    }

    private void printFullMan(int mistakes) {
        int i = 0;
        while(i < mistakes*4) {
            System.out.println(hangedMan[i]);
            i++;
        }
    }

    /**
     * Prints the menu and takes in user-input,
     * evaluates user inputs and allows the user
     * to write their name for the scoreboard.
     * <p>
     * A new game is started when the word is guessed.
     */
    private void findLetterAndPrintIt() {
        boolean isHelpUsed = false;
        String letter;
        StringBuffer dashBuff = new StringBuffer(dashedWord);
        int mistakes = 0;
        int maxMistakes= 6;

        while ((!dashBuff.toString().equals(guessWord)) && (mistakes < maxMistakes)) {
            System.out.println("The secret word is: " + printDashes(dashBuff));
            if (HangmanGame.DEBUG_MODE) {
                System.out.println("DEBUG " + guessWord);
            }
            printMan(mistakes);
            System.out.println("Enter your guess(1 letter allowed): ");
            Scanner input = new Scanner(System.in);
            letter = input.next();

            if (letter.matches("[a-z]")) {
                mistakes += testLetter(dashBuff, letter);
            }
            
            if (!isHelpUsed) {
                 isHelpUsed = menu(letter, dashBuff);
            }
        }

        if ((!isHelpUsed) && (mistakes < maxMistakes)) {
            System.out.println("You won with " + mistakes + " mistake(s).");
            System.out.println("The secret word is: " + printDashes(dashBuff));

            System.out.println("Please enter your name for the top scoreboard:");
            Scanner input = new Scanner(System.in);
            String playerName = input.next();

            fileReadWriter.readFromFile();
            fileReadWriter.myArr.add(new Player(playerName, mistakes));
            fileReadWriter.writeToFile();
            fileReadWriter.printAndSortScoreBoard();
        } else if (mistakes < maxMistakes){
            System.out.println("You won with " + mistakes + " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
            System.out.println("The secret word is: " + printDashes(dashBuff));
        } else {
            printFullMan(mistakes);
            System.out.println("You lost! The man is on the gallow...");
            System.out.println("The secret word was: " + guessWord);
        }

        // restart the game
        new Game(true);
    }

    /**
     * Executes menu command entered by letter.
     *
     * @param letter   entered by user.
     * @param dashBuff Word to guess.
     * @return true if help is used, false otherwise.
     */
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
            
            return true; // cheated
        }
            
        return false; // not cheated
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

    /**
     * Reveals one random char in the secret word to guess,
     * and prints the word.
     *
     * @param dashBuff containing the word to guess.
     */
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

    /**
     * Converts the word to dashed lines.
     *
     * @param word to censor.
     * @return string where characters is dashed.
     */
    private StringBuffer getDashedWord(String word) {
        StringBuffer dashes = new StringBuffer("");
        for (int i = 0; i < word.length(); i++) {
            dashes.append("_");
        }
        return dashes;
    }

    /**
     * Prints the word, where guessed characters are shown,
     * other characters are shown as dashes.
     *
     * @param word to guess.
     * @return string where guessed char are shown, other
     * chars as dashes.
     */
    private String printDashes(StringBuffer word) {
        String toDashes = "";
        for (int i = 0; i < word.length(); i++) {
            toDashes += (" " + word.charAt(i));
        }
        return toDashes;
    }

}
