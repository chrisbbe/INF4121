package hangman;

import java.io.*;
import java.util.ArrayList;

public class FileReadWriter {
    public static final String PERSISTENT_FILE = "players.ser";
    public ArrayList<Players> myArr = new ArrayList<>();

    public void writeToFile() {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(PERSISTENT_FILE));
            output.writeObject(this.myArr);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void printAndSortScoreBoard() {
        Players temp;
        int n = myArr.size();
        for (int pass = 1; pass < n; pass++) {

            for (int i = 0; i < n - pass; i++) {
                if (myArr.get(i).getScores() > myArr.get(i + 1).getScores()) {
                    temp = myArr.get(i);
                    myArr.set(i, myArr.get(i + 1));
                    myArr.set(i + 1, temp);
                }
            }
        }

        System.out.println("Scoreboard:");
        int place = 1;
        for (Players p : myArr) {
            System.out.printf("%d. %s ----> %d\n", place++, p.getName(), p.getScores());
        }
    }

}
