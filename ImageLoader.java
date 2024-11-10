import javax.swing.*;
import java.awt.*;

public class ImageLoader {
    public Image grassTile = new ImageIcon("src/Grass.png").getImage();
    public Image wayTile = new ImageIcon("src/Chemin.png").getImage();
    public Image waterTile = new ImageIcon("src/Water.png").getImage();
    public Image houseTile = new ImageIcon("src/output-onlinepngtools.png").getImage();
    public Image treeTile = new ImageIcon("src/Treee.png").getImage();
    public Image herbeTile = new ImageIcon("src/Herbe.png").getImage();
    public Image groundTile = new ImageIcon("src/HouseGround.png").getImage();
    public Image table = new ImageIcon("src/Table.png").getImage();
    public Image carpet = new ImageIcon("src/Carpet.png").getImage();
    public Image tele = new ImageIcon("src/Tele.png").getImage();
    public Image cuisine = new ImageIcon("src/Cuisine.png").getImage();
    public Image frigo = new ImageIcon("src/Frigo.png").getImage();
    public Image fenetre = new ImageIcon("src/Fenetre.png").getImage();
    public Image meuble1 = new ImageIcon("src/Meuble1.png").getImage();
    public Image mur = new ImageIcon("src/mur.png").getImage();

    public Image getTileImage(int tileType) {
        return switch (tileType) {
            case 0 -> grassTile;
            case 1 -> wayTile;
            case 2 -> waterTile;
            case 3 -> houseTile;
            case 4 -> treeTile;
            case 5 -> herbeTile;
            case 6 -> groundTile;
            case 7 -> table;
            case 8 -> carpet;
            case 9 -> tele;
            case 10 -> cuisine;
            case 11 -> frigo;
            case 12 -> fenetre;
            case 13 -> meuble1;
            case 14 -> mur;
            default -> null;
        };
    }
}
