package org.ragbecca;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ragbecca.entities.GameMap;
import org.ragbecca.entities.HighScore;
import org.ragbecca.util.Colors;
import org.ragbecca.util.LocalDateTimeAdapter;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class FountainLogic {

    private Map<Integer, LocalDateTime> highScore;

    private Map<Integer, Integer> fountain;

    private GameMap gameMap;
    private Map<Integer, Integer> currentLocation;

    private Gson gson;

    private int amountOfMoves;

    private boolean fountainState;

    public void homeScreen() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        highScore = highScoreCheck();


        if (fountain != null) {
            fountain.clear();
        }
        if (currentLocation != null) {
            currentLocation.clear();
        }

        if (highScore.size() == 1) {
            if (highScore.containsKey(0)) {
                System.out.println("No highscore set!");
            } else {
                for (Map.Entry<Integer, LocalDateTime> singleHighScore : highScore.entrySet()) {
                    System.out.println("Highscore is: " + singleHighScore.getKey() + " moves and was achieved on "
                            + singleHighScore.getValue());
                }
            }
        }

        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-");

        System.out.println("Type start to start! Or exit to stop!");

        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNextLine()) {
            if (scanner.nextLine().contains("exit")) {
                return;
            } else {
                createAllLocations();
            }
        }

    }

    public void createAllLocations() {
        String[][] locations = new String[3][3];

        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[0].length; j++) {
                locations[i][j] = "Empty";
            }
        }

        gameMap = new GameMap(locations);

        chooseFountainLocation();
    }

    private void chooseFountainLocation() {

        int row = 0;
        int column = 0;

        do {
            row = ThreadLocalRandom.current().nextInt(0, 4);
            column = ThreadLocalRandom.current().nextInt(0, 4);
        } while (row == 0 && column == 0);

        fountain = new HashMap<>();

        fountain.put(row, column);

        setStartLocation();
    }

    private void setStartLocation() {
        currentLocation = new HashMap<>();

        currentLocation.put(0, 0);

        System.out.println(Colors.ANSI_RED + "You are in the room at (Row=" + currectLocationRow() + ", Column=" + currectLocationColumn() + ").");
        System.out.println(Colors.ANSI_CYAN + "You see light coming from the cavern entrance" + Colors.ANSI_RESET);

        movement();
    }

    private int currectLocationRow() {
        for (Map.Entry<Integer, Integer> currentLocationEntry : currentLocation.entrySet()) {
            return currentLocationEntry.getKey();
        }
        return 0;
    }

    private int currectLocationColumn() {
        for (Map.Entry<Integer, Integer> currentLocationEntry : currentLocation.entrySet()) {
            return currentLocationEntry.getValue();
        }
        return 0;
    }

    private void movement() {
        boolean wonCheck = false;
        do {
            System.out.println("What do you want to do? ");
            Scanner scanner = new Scanner(System.in);
            String checkLine = scanner.nextLine();
            if (checkLine.contains("move east")) {
                int currentRow = currectLocationRow();
                int currentColumn = currectLocationColumn();

                if (currentColumn == gameMap.getMapSize().length) {
                    System.out.println("You can't go east!");
                    continue;
                }
                currentLocation.clear();
                currentLocation.put(currentRow, currentColumn + 1);
                amountOfMoves += 1;
                wonCheck = roomCheck();
            } else if (checkLine.contains("move north")) {
                int currentRow = currectLocationRow();
                int currentColumn = currectLocationColumn();

                if (currentRow == gameMap.getMapSize().length) {
                    System.out.println("You can't go north!");
                    continue;
                }
                currentLocation.clear();
                currentLocation.put(currentRow + 1, currentColumn);
                amountOfMoves += 1;
                wonCheck = roomCheck();
            } else if (checkLine.contains("move south")) {
                int currentRow = currectLocationRow();
                int currentColumn = currectLocationColumn();

                if (currentRow == 0) {
                    System.out.println("You can't go south!");
                    continue;
                }
                currentLocation.clear();
                currentLocation.put(currentRow - 1, currentColumn);
                amountOfMoves += 1;
                wonCheck = roomCheck();
            } else if (checkLine.contains("move west")) {
                int currentRow = currectLocationRow();
                int currentColumn = currectLocationColumn();

                if (currentColumn == 0) {
                    System.out.println("You can't go west!");
                    continue;
                }
                currentLocation.clear();
                currentLocation.put(currentRow, currentColumn - 1);
                amountOfMoves += 1;
                wonCheck = roomCheck();
            } else if (checkLine.contains("enable fountain")) {
                for (Map.Entry<Integer, Integer> fountainLocation : fountain.entrySet()) {
                    int row = fountainLocation.getKey();
                    int column = fountainLocation.getValue();
                    int currentRow = currectLocationRow();
                    int currentColumn = currectLocationColumn();

                    if (row != currentRow && column != currentColumn) {
                        System.out.println("There is no fountain here!");
                        continue;
                    }
                    fountainState = true;
                    amountOfMoves += 1;
                    wonCheck = roomCheck();
                }
            } else {
                System.out.println("That isn't a correct value, try again!");
            }
        } while (!wonCheck);

        homeScreen();
    }

    private boolean roomCheck() {
        for (Map.Entry<Integer, Integer> fountainLocation : fountain.entrySet()) {
            int row = fountainLocation.getKey();
            int column = fountainLocation.getValue();

            System.out.println("---------------------------------------------");
            System.out.println("You are in the room at (Row=" + currectLocationRow() + ", Column=" + currectLocationColumn() + ").");

            if (row == currectLocationRow() && column == currectLocationColumn() && !fountainState) {
                System.out.println("You hear watter dripping in this room. The Fountain of Objects is here!");
                return false;
            } else if (row == currectLocationRow() && column == currectLocationColumn() && fountainState) {
                System.out.println("You hear the rushing waters from the Fountain of Objects. It has been reactivated!");
                return false;
            } else if (currectLocationRow() == 0 && currectLocationColumn() == 0 && fountainState) {
                System.out.println("The Fountain of Objects has been reactivated, and you have escaped with your life!");
                System.out.println("You win!");
                System.out.println("You did it in: " + amountOfMoves + " moves.");
                saveHighScore();
                return true;
            }
            return false;
        }
        return false;

    }

    private void saveHighScore() {
        for (Map.Entry<Integer, LocalDateTime> highScoreValue : highScore.entrySet()) {
            if (highScoreValue.getKey() > amountOfMoves) {
                highScore.clear();
                HighScore newlyGottenHighScore = new HighScore(amountOfMoves, LocalDateTime.now());

                highScore.put(amountOfMoves, LocalDateTime.now());
                System.out.println("New highScore!");

                Path path = Paths.get("highscore.json");

                BufferedWriter writer;
                try {
                    writer = Files.newBufferedWriter(path);
                    writer.write("");
                    writer.flush();

                    FileWriter fileWriter = new FileWriter(path.toFile());

                    gson.toJson(newlyGottenHighScore, fileWriter);
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);

                }
            }
        }
    }

    private Map<Integer, LocalDateTime> highScoreCheck() {

        Path path = Paths.get("highscore.json");
        File jsonFile = new File(path.toUri());

        if (!jsonFile.exists()) {
            HighScore startingHighScore = new HighScore(0, LocalDateTime.now());
            try {
                FileWriter fileWriter = new FileWriter(path.toFile());
                gson.toJson(startingHighScore, fileWriter);
                fileWriter.close();

                Map<Integer, LocalDateTime> mapInitial = new HashMap<>();
                mapInitial.put(0, LocalDateTime.now());

                return mapInitial;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Reader reader = Files.newBufferedReader(path);

            Map<?, ?> mapFromJson = gson.fromJson(reader, Map.class);

            int amountOfMoves = 0;
            LocalDateTime momentOfAchieving = null;

            // print map entries
            for (Map.Entry<?, ?> entry : mapFromJson.entrySet()) {
                if (entry.getKey().equals("amountOfMoves")) {
                    double amountOfMovesValue = (double) entry.getValue();
                    amountOfMoves = (int) amountOfMovesValue;
                } else if (entry.getKey().equals("momentOfAchieving")) {
                   momentOfAchieving = LocalDateTime.parse((String) entry.getValue());
                }
            }

            // close reader
            reader.close();

            Map<Integer, LocalDateTime> mapHighScore = new HashMap<>();

            mapHighScore.put(amountOfMoves, momentOfAchieving);

            return mapHighScore;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
