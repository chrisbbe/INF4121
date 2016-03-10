package hangman;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * FileReadWriter implements methods to write and read
 * the Hangman scoreboard from/to a file.
 */
class FileReadWriter {
    private static final String PERSISTENT_FILE = "players.ser";
    public ArrayList<Player> myArr = new ArrayList<>();

    /**
     * Writes the ArrayList containing the players as a byte
     * stream to file PERSISTENT_FILE
     */
    public void writeToFile() {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(PERSISTENT_FILE));
            output.writeObject(this.myArr);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the byte stream in PERSISTENT_FILE and restores
     * the ArrayList containing players.
     */
    public void readFromFile() {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(PERSISTENT_FILE));
            this.myArr = (ArrayList) input.readObject();
            input.close();
        } catch (FileNotFoundException ignored) {

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sorts the scoreboard and prints the
     * result in ascending order.
     */
    public void printAndSortScoreBoard() {
        Collections.sort(myArr);

        System.out.println("Scoreboard:");
        int place = 1;
        for (Player p : myArr) {
            System.out.printf("%d. %s ----> %d\n", place++, p.getName(), p.getScores());
        }
    }

}
