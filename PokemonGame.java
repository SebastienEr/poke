import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PokemonGame extends JFrame implements KeyListener {
    private final int TILE_SIZE = 64;
    private final int VIEW_WIDTH = 14;
    private final int VIEW_HEIGHT = 12;

    private final Map<Integer, Image> tileImages = new HashMap<>();
    private int[][] mapData;
    private int[][] overlayData;
    private int[][] houseMapData;
    private int[][] houseOverlayData;
    private boolean inHouse = false;

    private Player player;
    private BufferedImage buffer;

    // Variables pour gérer les touches enfoncées
    private boolean upPressed, downPressed, leftPressed, rightPressed, sprintPressed;

    private Timer gameTimer;

 

    public PokemonGame() {
        setTitle("Jeux Pokemon - Caméra centrée");
        setSize(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setFocusable(true);

        // Charger les images
        loadImages();

        // Charger les données de la carte
        mapData = loadMap("map.txt");
        overlayData = loadMap("map1.txt");
        houseMapData = loadMap("housemap.txt");
        houseOverlayData = loadMap("housemapoverlay.txt");

        player = new Player(10, 10, TILE_SIZE);
        buffer = new BufferedImage(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE, BufferedImage.TYPE_INT_ARGB);

        // Démarrer la boucle de jeu
        gameTimer = new Timer(16, e -> {
            gameUpdate();
            repaint();
        });
        gameTimer.start();
    }

    private void loadImages() {
        tileImages.put(0, new ImageIcon("src/Grass.png").getImage());
        tileImages.put(1, new ImageIcon("src/Chemin.png").getImage());
        tileImages.put(2, new ImageIcon("src/Water.png").getImage());
        tileImages.put(3, new ImageIcon("src/output-onlinepngtools.png").getImage());
        tileImages.put(4, new ImageIcon("src/Treee.png").getImage());
        tileImages.put(5, new ImageIcon("src/Herbe.png").getImage());
        tileImages.put(6, new ImageIcon("src/HouseGround.png").getImage());
        tileImages.put(7, new ImageIcon("src/Table.png").getImage());
        tileImages.put(8, new ImageIcon("src/Carpet.png").getImage());
        tileImages.put(9, new ImageIcon("src/Tele.png").getImage());
        tileImages.put(10, new ImageIcon("src/Cuisine.png").getImage());
        tileImages.put(11, new ImageIcon("src/Frigo.png").getImage());
        tileImages.put(12, new ImageIcon("src/Fenetre.png").getImage());
        tileImages.put(13, new ImageIcon("src/Meuble1.png").getImage());
        tileImages.put(14, new ImageIcon("src/mur.png").getImage());
        tileImages.put(15, new ImageIcon("src/Treee.png").getImage());
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
        return mapRows.toArray(new int[0][]);
    }

    private void gameUpdate() {
        boolean moving = false;

        if (upPressed) {
            player.moveUp();
            moving = true;
        }
        if (downPressed) {
            player.moveDown(inHouse ? houseMapData.length : mapData.length);
            moving = true;
        }
        if (leftPressed) {
            player.moveLeft();
            moving = true;
        }
        if (rightPressed) {
            player.moveRight(inHouse ? houseMapData[0].length : mapData[0].length);
            moving = true;
        }
        

        if (moving) {
            if (sprintPressed) {
                player.setRunning(true);
                player.setSpeed(TILE_SIZE / 9.0); // Vitesse de course
            } else {
                player.setRunning(false);
                player.setSpeed(TILE_SIZE / 18.0); // Vitesse de marche
            }
        } else {
            player.setRunning(false);
        }

        // Gestion des transitions de carte
        int playerX = player.getTileX();
        int playerY = player.getTileY();
        
        if (System.currentTimeMillis() % 2000 < 16) {
            System.out.println("Player Coordinates: X = " + playerX + ", Y = " + playerY);
        }
       

        if (!inHouse && playerX == 13 && playerY == 7) {
            inHouse = true;
            player.setPosition(4, 6, 5, 3); // Téléporte dans la maison
        } else if (inHouse && (houseMapData[playerY][playerX] == 0 || houseMapData[playerY][playerX] == 8)) {
            inHouse = false;
            player.setPosition(13, 8, 31, 0); // Téléporte en dehors de la maison
        }
    }
    

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = buffer.createGraphics();
        int offsetX = Math.max(-3, Math.min(player.getTileX() - VIEW_WIDTH / 2,
                (inHouse ? houseMapData[0].length : mapData[0].length) - VIEW_WIDTH));
        int offsetY = Math.max(-3, Math.min(player.getTileY() - VIEW_HEIGHT / 2,
                (inHouse ? houseMapData.length : mapData.length) - VIEW_HEIGHT));
                
                

        g2d.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());

        

        int[][] currentMapData = inHouse ? houseOverlayData : mapData;
        int[][] currentOverlayData = inHouse ? houseMapData : overlayData;

        drawMap(g2d, currentMapData, offsetX, offsetY);
        drawMap(g2d, currentOverlayData, offsetX, offsetY);

        player.draw(g2d, offsetX, offsetY);
        g.drawImage(buffer, 0, 0, null);
        g2d.dispose();
    }

    private void drawMap(Graphics2D g2d, int[][] mapData, int offsetX, int offsetY) {
        // Votre méthode existante, inchangée
        for (int row = 0; row < VIEW_HEIGHT; row++) {
            for (int col = 0; col < VIEW_WIDTH; col++) {
                int mapX = col + offsetX;
                int mapY = row + offsetY;

                if (mapY >= 0 && mapY < mapData.length && mapX >= 0 && mapX < mapData[0].length) {
                    int tileType = mapData[mapY][mapX];

                    // Vérifier si c'est une partie d'une grande tuile qui ne doit pas être dessinée
                    if (tileType == 3 || tileType == 4 || tileType == 15) {
                        if (!isTopLeftOfLargeTile(mapData, mapX, mapY, tileType)) {
                            continue; // Ne pas dessiner cette tuile
                        }
                    }

                    Image tileImage = tileImages.get(tileType);

                    if (tileImage != null) {
                        int x = col * TILE_SIZE;
                        int y = row * TILE_SIZE;

                        // Ajuster la taille des tuiles spéciales
                        if (tileType == 8) {
                            g2d.drawImage(tileImage, x, y + 16, TILE_SIZE, TILE_SIZE, this);
                        } else if (tileType == 3 || tileType == 4 || tileType == 15)  {
                            Dimension size = getTileSize(tileType);
                            g2d.drawImage(tileImage, x, y, TILE_SIZE * size.width, TILE_SIZE * size.height, this);
                        } else {
                            g2d.drawImage(tileImage, x, y, TILE_SIZE, TILE_SIZE, this);
                        }
                    }
                }
            }
        }
    }

    private Dimension getTileSize(int tileType) {
        // Votre méthode existante, inchangée
        switch (tileType) {
            case 3: // Maison
                return new Dimension(3, 3);
            case 4: // Arbre
                return new Dimension(2, 2);
            case 15: // Maison
                return new Dimension(2, 2);
            default:
                return new Dimension(1, 1);
        }
    }

    private boolean isTopLeftOfLargeTile(int[][] mapData, int mapX, int mapY, int tileType) {
        // Votre méthode existante, inchangée
        Dimension size = getTileSize(tileType);
        int tileWidth = size.width;
        int tileHeight = size.height;

        // Vérifier les tuiles à gauche
        for (int i = 1; i <= tileWidth - 1; i++) {
            if (mapX - i >= 0 && mapData[mapY][mapX - i] == tileType) {
                return false;
            }
        }

        // Vérifier les tuiles au-dessus
        for (int i = 1; i <= tileHeight - 1; i++) {
            if (mapY - i >= 0 && mapData[mapY - i][mapX] == tileType) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Mise à jour des touches enfoncées
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
            case KeyEvent.VK_CONTROL:
                sprintPressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Mise à jour des touches relâchées
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
            case KeyEvent.VK_CONTROL:
                sprintPressed = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Non utilisé
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PokemonGame game = new PokemonGame();
            game.setVisible(true);
        });
    }
}
