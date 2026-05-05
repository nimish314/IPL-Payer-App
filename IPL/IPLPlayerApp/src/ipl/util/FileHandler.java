package ipl.util;

import ipl.model.AllRounder;
import ipl.model.Batsman;
import ipl.model.Bowler;
import ipl.model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Static utility class for persisting Player objects to a plain-text file.
 * Demonstrates File Handling using BufferedReader / BufferedWriter and static methods.
 */
public class FileHandler {

    public static final String FILE_NAME = "players.txt";

    // Prevent instantiation – this is a utility class.
    private FileHandler() { }

    /** Append one player to the file (creates the file if missing). */
    public static void savePlayer(Player p) throws PlayerException {
        if (p == null) {
            throw new PlayerException("Cannot save a null player.");
        }
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FILE_NAME, true))) {
            bw.write(p.toFileString());
            bw.newLine();
        } catch (IOException e) {
            throw new PlayerException("Failed to save player: " + e.getMessage(), e);
        }
    }

    /** Re-write the entire file from a list (used for delete / update). */
    public static void saveAll(List<Player> players) throws PlayerException {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FILE_NAME, false))) {
            for (Player p : players) {
                bw.write(p.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new PlayerException("Failed to save players list: " + e.getMessage(), e);
        }
    }

    /** Load all players, reconstructing the correct subclass from the role tag. */
    public static List<Player> loadPlayers() {
        List<Player> players = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return players;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    Player p = parseLine(line);
                    if (p != null) players.add(p);
                } catch (Exception ex) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return players;
    }

    /** Dispatch a line to the correct fromString() factory based on prefix. */
    private static Player parseLine(String line) {
        if (line.startsWith("BAT|"))  return Batsman.fromString(line);
        if (line.startsWith("BOWL|")) return Bowler.fromString(line);
        if (line.startsWith("AR|"))   return AllRounder.fromString(line);
        return null;
    }
}
