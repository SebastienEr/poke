import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PokemonGame extends JFrame implements KeyListener {
    private final int TILE_SIZE = 64;
    private final int VIEW_WIDTH = 16;
    private final int VIEW_HEIGHT = 10;

    private Image grassTile;
    private Image wayTile;
    private Image waterTile;
    private Image houseTile;
    private Image treeTile;
    private Image herbeTile;
    private Image groundTile;
    private Image Table;
    private Image Carpet;
    private Image Tele;
    private Image Cuisine;
    private Image Frigo;
    private Image Fenetre;
    private Image Meuble1;
    private Image Mur;

    private int[][] mapData;
    private int[][] overlayData;
    private int[][] houseMapData;
    private int[][] houseOverlayData;
    private boolean inHouse = false;

    private Player player;
    private BufferedImage buffer;
    private GameController gameController;

    public PokemonGame() {
        setTitle("Jeux Pokemon - Caméra centrée");
        setSize(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setFocusable(true);

        // Load images
        grassTile = new ImageIcon("src/Grass.png").getImage();
        wayTile = new ImageIcon("src/Chemin.png").getImage();
        waterTile = new ImageIcon("src/Water.png").getImage();
        houseTile = new ImageIcon("src/output-onlinepngtools.png").getImage();
        treeTile = new ImageIcon("src/Treee.png").getImage();
        herbeTile = new ImageIcon("src/Herbe.png").getImage();
        groundTile = new ImageIcon("src/HouseGround.png").getImage();
        Table = new ImageIcon("src/Table.png").getImage();
        Carpet = new ImageIcon("src/Carpet.png").getImage();
        Tele = new ImageIcon("src/Tele.png").getImage();
        Cuisine = new ImageIcon("src/Cuisine.png").getImage();
        Frigo = new ImageIcon("src/Frigo.png").getImage();
        Fenetre = new ImageIcon("src/Fenetre.png").getImage();
        Meuble1 = new ImageIcon("src/Meuble1.png").getImage();
        Mur = new ImageIcon("src/mur.png").getImage();


        
        

        mapData = loadMap("map.txt");
        overlayData = loadMap("map1.txt");
        houseMapData = loadMap("housemap.txt");
        houseOverlayData = loadMap("housemapoverlay.txt");

        player = new Player(10, 10, TILE_SIZE);
        gameController = new GameController();

        buffer = new BufferedImage(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    }

    private int[][] loadMap(String filename) {
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

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = buffer.createGraphics();

        int offsetX = player.getTileX() - VIEW_WIDTH / 2;
        int offsetY = player.getTileY() - VIEW_HEIGHT / 2;


    if (inHouse) {
    offsetX = Math.max(0, Math.min(player.getTileX() - VIEW_WIDTH / 2, houseMapData[0].length - VIEW_WIDTH));
    offsetY = Math.max(0, Math.min(player.getTileY() - VIEW_HEIGHT / 2, houseMapData.length - VIEW_HEIGHT));
} else {
    offsetX = player.getTileX() - VIEW_WIDTH / 2;
    offsetY = player.getTileY() - VIEW_HEIGHT / 2;
}


        

        g2d.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());

        int[][] currentMapData = inHouse ? houseOverlayData : mapData;
        int[][] currentOverlayData = inHouse ? houseMapData : overlayData;

        for (int row = 0; row < VIEW_HEIGHT; row++) {
            for (int col = 0; col < VIEW_WIDTH; col++) {
                int mapX = col + offsetX;
                int mapY = row + offsetY;

                if (mapY >= 0 && mapY < currentMapData.length && mapX >= 0 && mapX < currentMapData[0].length) {
                    int tileType = currentMapData[mapY][mapX];
                    Image tileImage = switch (tileType) {
                        case 0 -> grassTile;
                        case 1 -> wayTile;
                        case 2 -> waterTile;
                        case 3 -> houseTile;
                        case 4 -> treeTile;
                        case 5 -> herbeTile;
                        case 6 -> groundTile;
                        case 7 -> Table;
                        case 8 -> Carpet;
                        case 9 -> Tele;
                        case 10 -> Cuisine;
                        case 11 -> Frigo;
                        case 12 -> Fenetre;
                        case 13 -> Meuble1;
                        case 14 -> Mur;

                        default -> null;
                    };
                    g2d.drawImage(tileImage, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
                }
            }
        }

        // Draw the overlay map
        for (int row = 0; row < VIEW_HEIGHT; row++) {
            for (int col = 0; col < VIEW_WIDTH; col++) {
                int mapX = col + offsetX;
                int mapY = row + offsetY;

                if (mapY >= 0 && mapY < currentOverlayData.length && mapX >= 0 && mapX < currentOverlayData[0].length) {
                    int tileType = currentOverlayData[mapY][mapX];
                    Image tileImage = switch (tileType) {
                        case 1 -> wayTile;
                        case 2 -> waterTile;
                        case 3 -> houseTile;
                        case 4 -> treeTile;
                        case 5 -> herbeTile;
                        case 6 -> groundTile;
                        case 7 -> Table;
                        case 8 -> Carpet;
                        case 9 -> Tele;
                        case 10 -> Cuisine;
                        case 11 -> Frigo;
                        case 12 -> Fenetre;
                        case 13 -> Meuble1;
                        case 14 -> Mur;
                        default -> null;
                    };

                    if (tileImage != null) {
                    if (tileType == 8) {
                    g2d.drawImage(tileImage, col * TILE_SIZE, row * TILE_SIZE + 13, TILE_SIZE, TILE_SIZE, this);
                        } else if (tileType == 3) {
                     g2d.drawImage(tileImage, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE * 3, TILE_SIZE * 3, this);
                    } else if (tileType == 4) {
                        g2d.drawImage(tileImage, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE * 2, TILE_SIZE * 2, this);
                    } else {
                    g2d.drawImage(tileImage, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
                    
    }
}

                }
            }
        }

        player.draw(g2d, offsetX, offsetY);

        g.drawImage(buffer, 0, 0, null);
        g2d.dispose();
    }




    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> player.moveUp();
            case KeyEvent.VK_DOWN -> player.moveDown(mapData.length);
            case KeyEvent.VK_LEFT -> player.moveLeft();
            case KeyEvent.VK_RIGHT -> player.moveRight(mapData[0].length);
        }

        int playerX = player.getTileX();
        int playerY = player.getTileY();

        System.out.println("Player Coordinates: X = " + playerX + ", Y = " + playerY);

        if (!inHouse && playerX == 13 && playerY == 7) {
            inHouse = true;
            player.setPosition(4, 5, 5, 2); // Téléporte dans la maison
        } else if (inHouse && (houseMapData[playerY][playerX] == 0 || houseMapData[playerY][playerX] == 8)) {  
            inHouse = false;
            player.setPosition(13, 8, 31, 0); // Téléporte en dehors de la maison
          

        }
    
        repaint();
        
        }

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PokemonGame game = new PokemonGame();
            game.setVisible(true);
        });
    }
}
