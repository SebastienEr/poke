

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GameController {
    private int[][] worldMapData;
    private int[][] worldOverlayData;
    private int[][] houseMapData;
    private int[][] houseOverlayData;
    private int[][] currentMapData;
    private int[][] currentOverlayData;
    private boolean inHouse = false;
    private int originalX, originalY; // Player's original position

    public GameController() {
        // Load all maps
        this.worldMapData = loadMap("map.txt");
        this.worldOverlayData = loadMap("map1.txt");
        this.houseMapData = loadMap("housemap.txt");
        this.houseOverlayData = loadMap("housemapoverlay.txt");

        this.currentMapData = worldMapData;
        this.currentOverlayData = worldOverlayData;
    }

    public int[][] getCurrentMapData() {
        return currentMapData;
    }

    public int[][] getCurrentOverlayData() {
        return currentOverlayData;
    }

    public int[][] loadMap(String filename) {
        ArrayList<int[]> mapRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                int[] row = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Integer.parseInt(tokens[i]);
                }
                mapRows.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[][] mapArray = new int[mapRows.size()][];
        for (int i = 0; i < mapRows.size(); i++) {
            mapArray[i] = mapRows.get(i);
        }
        return mapArray;
    }

    public void updatePlayerPosition(Player player) {
        if (!inHouse && currentOverlayData[player.getTileY()][player.getTileX()] == 3) {
            enterHouse(player);
        } else if (inHouse && player.getTileX() == 1 && player.getTileY() == 1) { // Example exit position
            exitHouse(player);
        }
    }

    private void enterHouse(Player player) {
        originalX = player.getTileX();
        originalY = player.getTileY();
        currentMapData = houseMapData;
        currentOverlayData = houseOverlayData;
        player.setPosition(2, 2, 0.0, 0.0); // Set starting position inside the house with zero velocity
        inHouse = true;
    }

    private void exitHouse(Player player) {
        currentMapData = worldMapData;
        currentOverlayData = worldOverlayData;
        player.setPosition(originalX, originalY, 0.0, 0.0); // Return player to original position with zero velocity
        inHouse = false;
    }
}