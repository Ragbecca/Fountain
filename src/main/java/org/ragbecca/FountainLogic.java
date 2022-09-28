package org.ragbecca;

import org.ragbecca.entities.CurrentLocation;
import org.ragbecca.entities.Fountain;
import org.ragbecca.entities.GameMap;
import org.ragbecca.util.Colors;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class FountainLogic {

    private Fountain fountain;
    private GameMap gameMap;
    private CurrentLocation currentLocation;
    private boolean fountainState;

    public void homeScreen() {
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-");
        System.out.println("Type start to start! Or exit to stop!");
        fountainState = false;
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            if (scanner.nextLine().contains("exit")) {
            } else {
                createAllLocations();
            }
        }

    }

    private void createAllLocations() {
        Object[][] locations = new Object[3][3];

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

        fountain = new Fountain(row, column);

        setStartLocation();
    }

    private void setStartLocation() {
        currentLocation = new CurrentLocation(0, 0);

        System.out.println(Colors.ANSI_RED + "You are in the room at (Row=" + currentLocation.currentRow() + ", Column=" + currentLocation.currentColumn() + ").");
        System.out.println(Colors.ANSI_CYAN + "You see light coming from the cavern entrance" + Colors.ANSI_RESET);

        movement();
    }

    private void movement() {
        boolean wonCheck = false;
        do {
            System.out.println("What do you want to do? ");
            Scanner scanner = new Scanner(System.in);
            String checkLine = scanner.nextLine();
            if (movement(checkLine)) {
                wonCheck = roomCheck();
            } else if (checkLine.contains("enable fountain")) {
                int row = fountain.fountainRow();
                int column = fountain.fountainColumn();
                int currentRow = currentLocation.currentRow();
                int currentColumn = currentLocation.currentColumn();

                if (row != currentRow && column != currentColumn) {
                    System.out.println("There is no fountain here!");
                    continue;
                }
                fountainState = true;
                wonCheck = roomCheck();
            } else {
                System.out.println("That isn't a correct value, try again!");
            }
        } while (!wonCheck);
        homeScreen();
    }

    public boolean movement(String direction) {
        switch (direction) {
            case "move west":
                if (checkIfMaxWayThatWay("west", 0, -1)) {
                    return true;
                }
                break;
            case "move east":
                if (checkIfMaxWayThatWay("east", 0, 1)) {
                    return true;
                }
                break;
            case "move north":
                if (checkIfMaxWayThatWay("north", 1, 0)) {
                    return true;
                }
                break;
            case "move south":
                if (checkIfMaxWayThatWay("south", -1, 0)) {
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    public boolean checkIfMaxWayThatWay(String direction, int directionRow, int directionColumn) {
        if ((currentLocation.currentRow() + directionRow) <= gameMap.getMapSize().length &&
                (currentLocation.currentColumn() + directionColumn) <= gameMap.getMapSize().length &&
                (currentLocation.currentColumn() + directionColumn) >= 0 &&
                (currentLocation.currentRow() + directionRow) >= 0) {
            currentLocation = new CurrentLocation(currentLocation.currentRow() + directionRow, currentLocation.currentColumn() + directionColumn);
            return true;
        } else {
            System.out.println("You can't go " + direction + "!");
            return false;
        }
    }

    private boolean roomCheck() {
        int row = fountain.fountainRow();
        int column = fountain.fountainColumn();

        System.out.println("---------------------------------------------");
        System.out.println(Colors.ANSI_RED + "You are in the room at (Row=" + currentLocation.currentRow() + ", Column=" + currentLocation.currentColumn() + ")." + Colors.ANSI_RESET);

        if (row == currentLocation.currentRow() && column == currentLocation.currentColumn() && !fountainState) {
            System.out.println("You hear watter dripping in this room. The Fountain of Objects is here!");
            return false;
        } else if (row == currentLocation.currentRow() && column == currentLocation.currentColumn() && fountainState) {
            System.out.println("You hear the rushing waters from the Fountain of Objects. It has been reactivated!");
            return false;
        } else if (currentLocation.currentRow() == 0 && currentLocation.currentColumn() == 0 && fountainState) {
            System.out.println("The Fountain of Objects has been reactivated, and you have escaped with your life!");
            System.out.println("You win!");
            return true;
        }
        return false;
    }
}
