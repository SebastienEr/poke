import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private double x;  
    private double y;
    private final int TILE_SIZE;
    private double moveDistance;
    private BufferedImage[][][] sprites; // [0]: marche, [1]: course

    private int direction = 2;  
    private int animationFrame = 0; 
    private int animationCounter = 0;  
    private boolean running = false;

    public Player(int startX, int startY, int tileSize) {
        this.x = startX * tileSize;
        this.y = startY * tileSize;
        this.TILE_SIZE = tileSize;
        this.moveDistance = tileSize / 18.0; // Vitesse de marche par défaut

        try {
            BufferedImage walkSpriteSheet = ImageIO.read(getClass().getResource("src\\Hero-M-Walk.png"));
            BufferedImage runSpriteSheet = ImageIO.read(getClass().getResource("src\\Hero-M-Run.png"));
            loadSprites(walkSpriteSheet, runSpriteSheet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Correction : la méthode loadSprites doit accepter deux arguments
    private void loadSprites(BufferedImage walkSpriteSheet, BufferedImage runSpriteSheet) {
        int spriteWidth = walkSpriteSheet.getWidth() / 4;  
        int spriteHeight = walkSpriteSheet.getHeight() / 4;  
        sprites = new BufferedImage[2][4][4];  // [0]: marche, [1]: course

        // Charger les sprites de marche
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                sprites[0][row][col] = walkSpriteSheet.getSubimage(
                    col * spriteWidth,
                    row * spriteHeight,
                    spriteWidth,
                    spriteHeight
                );
            }
        }

        // Charger les sprites de course
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                sprites[1][row][col] = runSpriteSheet.getSubimage(
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

    public void setSpeed(double speed) {
        this.moveDistance = speed;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void moveUp() {
        y = Math.max(0, y - moveDistance);
        direction = 3;  
        animate();
    }

    public void moveDown(int mapHeight) {
        y = Math.min((mapHeight - 1) * TILE_SIZE, y + moveDistance);
        direction = 0;  
        animate();
    }

    public void moveLeft() {
        x = Math.max(0, x - moveDistance);
        direction = 1;  
        animate();
    }

    public void moveRight(int mapWidth) {
        x = Math.min((mapWidth - 1) * TILE_SIZE, x + moveDistance);
        direction = 2;  
        animate();
    }


    

    private void animate() {
        animationCounter++;
        int animationSpeed = running ? 5 : 10; // Ajuster ces valeurs selon vos préférences

        if (animationCounter >= animationSpeed) {
            animationFrame = (animationFrame + 1) % 4;
            animationCounter = 0;
        }
    }

    public void draw(Graphics2D g, int offsetX, int offsetY) {
        int screenX = (int) x - offsetX * TILE_SIZE;
        int screenY = (int) y - offsetY * TILE_SIZE;

        int playerWidth = TILE_SIZE;
        int playerHeight = TILE_SIZE;

        int centerX = screenX + (TILE_SIZE - playerWidth) / 2;
        int centerY = screenY + (TILE_SIZE - playerHeight) / 2;

        int spriteIndex = running ? 1 : 0; // 0 pour marche, 1 pour course

        // Correction : s'assurer que l'image passée est un BufferedImage, pas un tableau
        g.drawImage(sprites[spriteIndex][direction][animationFrame], centerX, centerY, playerWidth, playerHeight, null);
    }
}
