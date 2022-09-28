package org.ragbecca.entities;

public class GameMap {

    private Object[][] mapSize;

    public GameMap(Object[][] mapSize) {
        this.mapSize = mapSize;
    }

    public Object[][] getMapSize() {
        return mapSize;
    }

    public void setMapSize(Object[][] mapSize) {
        this.mapSize = mapSize;
    }
}
