package org.ragbecca.entities;

public class GameMap {

    private String[][] mapSize;

    public GameMap(String[][] mapSize) {
        this.mapSize = mapSize;
    }

    public String[][] getMapSize() {
        return mapSize;
    }

    public void setMapSize(String[][] mapSize) {
        this.mapSize = mapSize;
    }
}
