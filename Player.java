import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private double x;  
    private double y;
    private final int TILE_SIZE;
    private final double MOVE_DISTANCE;
    private BufferedImage spriteSheet;
    private BufferedImage[][] sprites;  

    private int direction = 2;  
    private int animationFrame = 0; 
    private int animationCounter = 0;  

    public Player(int startX, int startY, int tileSize) {
        this.x = startX * tileSize;
        this.y = startY * tileSize;
        this.TILE_SIZE = tileSize;
        this.MOVE_DISTANCE = tileSize / 4.0;

        try {
            spriteSheet = ImageIO.read(getClass().getResource("src/Hero-M-Walk - Copie.png"));
            loadSprites();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSprites() {
        int spriteWidth = spriteSheet.getWidth() / 4;  
        int spriteHeight = spriteSheet.getHeight() / 4;  
        sprites = new BufferedImage[4][4];  

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                sprites[row][col] = spriteSheet.getSubimage(
                    col * spriteWidth,
                    row * spriteHeight,
                    spriteWidth,
                    spriteHeight
                );
            }
        }
    }

    public int getTileX() {
        return (int) (x / TILE_SIZE);
    }
    
    public int getTileY() {
        return (int) (y / TILE_SIZE);
    }

     public void setPosition(int tileX, int tileY, double offsetX, double offsetY) {
        this.x = tileX * TILE_SIZE + offsetX;
        this.y = tileY * TILE_SIZE + offsetY;
    }

    public boolean isOnSpecificTile(int[][] map, int tileType) {
        int tileX = getTileX();
        int tileY = getTileY();
        return map[tileY][tileX] == tileType;
    }

    public void moveUp() {
        y = Math.max(0, y - MOVE_DISTANCE);
        direction = 3;  
        animate();
    }

    public void moveDown(int mapHeight) {
        y = Math.min((mapHeight - 1) * TILE_SIZE, y + MOVE_DISTANCE);
        direction = 0;  
        animate();
    }

    public void moveLeft() {
        x = Math.max(0, x - MOVE_DISTANCE);
        direction = 1;  
        animate();
    }

    public void moveRight(int mapWidth) {
        x = Math.min((mapWidth - 1) * TILE_SIZE, x + MOVE_DISTANCE);
        direction = 2;  
        animate();
    }

    // Animate the player character
    private void animate() {
        animationCounter++;
        if (animationCounter >= 1) {  
            animationFrame = (animationFrame + 1) % 4;
            animationCounter = 0;
        }
    }

    // Draw the player with a custom size
    public void draw(Graphics2D g, int offsetX, int offsetY) {
        int screenX = (int) x - offsetX * TILE_SIZE;
        int screenY = (int) y - offsetY * TILE_SIZE;

        int playerWidth = TILE_SIZE;
        int playerHeight = TILE_SIZE;

        int centerX = screenX + (TILE_SIZE - playerWidth) / 2;
        int centerY = screenY + (TILE_SIZE - playerHeight) / 2;

        g.drawImage(sprites[direction][animationFrame], centerX, centerY, playerWidth, playerHeight, null);
    }
}
