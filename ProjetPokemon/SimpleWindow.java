import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleWindow {
    private static JFrame frame;
    private static Pokemon Salameche = PokemonStats.SALAMECHE;
    private static Pokemon Bulbizarre = PokemonStats.BULBIZARRE;
    private static JProgressBar SalamecheHpBar;
    private static JProgressBar BulbizarreHpBar;
    private static JLabel SalamecheImageLabel = new JLabel(new ImageIcon("C:/Users/basti/Documents/Epitech working/ProjetPokemon/images/SalamecheBack.png"));
    private static JLabel BulbizarreImageLabel = new JLabel(new ImageIcon("C:/Users/basti/Documents/Epitech working/ProjetPokemon/images/Bulbizarre.png"));
    private static JLabel turnLabel = new JLabel("", JLabel.CENTER);
    private static boolean isSalamecheTurn = true;
    private static JLabel SalamecheXpLabel = new JLabel();
    private static JLabel BulbizarreXpLabel = new JLabel();

    public static void createAndShowGUI() {
        frame = new JFrame("Pokemon Battle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 520);

        
        SalamecheHpBar = new JProgressBar(0, Salameche.getMaxHp());
        BulbizarreHpBar = new JProgressBar(0, Bulbizarre.getMaxHp());

        
        SalamecheHpBar.setValue(Salameche.getHp());
        SalamecheHpBar.setString(Salameche.getHp() + "/" + Salameche.getMaxHp());
        SalamecheHpBar.setStringPainted(true);
        BulbizarreHpBar.setValue(Bulbizarre.getHp());
        BulbizarreHpBar.setString(Bulbizarre.getHp() + "/" + Bulbizarre.getMaxHp());
        BulbizarreHpBar.setStringPainted(true);

        
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

     
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("C:/Users/basti/Documents/Epitech working/ProjetPokemon/images/Terrain.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Pokemon Battle!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        turnLabel.setFont(new Font("Arial", Font.PLAIN, 18)); 
        panel.add(turnLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        panel.add(SalamecheImageLabel, gbc);

        gbc.gridx = 1;
        panel.add(BulbizarreImageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(SalamecheHpBar, gbc);

        gbc.gridx = 1;
        panel.add(BulbizarreHpBar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton attackSalamecheButton = new JButton("Feu");
        attackSalamecheButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isSalamecheTurn && Bulbizarre.isAlive()) {
                    Bulbizarre.takeDamage(Salameche.getAttack());
                    BulbizarreHpBar.setValue(Bulbizarre.getHp());
                    BulbizarreHpBar.setString(Bulbizarre.getHp() + "/" + Bulbizarre.getMaxHp());
                    checkGameOver();
                    isSalamecheTurn = false;
                    updateTurn();
                }
            }
        });
        panel.add(attackSalamecheButton, gbc);

        gbc.gridy = 5;
        JButton usePotionButton = new JButton("Use Potion");
        usePotionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isSalamecheTurn && Salameche.isAlive()) {
                    Salameche.heal(20); 
                    SalamecheHpBar.setValue(Salameche.getHp());
                    SalamecheHpBar.setString(Salameche.getHp() + "/" + Salameche.getMaxHp());
                    isSalamecheTurn = false;
                    updateTurn();
                } else if (!isSalamecheTurn && Bulbizarre.isAlive()) {
                    Bulbizarre.heal(20);
                    BulbizarreHpBar.setValue(Bulbizarre.getHp());
                    BulbizarreHpBar.setString(Bulbizarre.getHp() + "/" + Bulbizarre.getMaxHp());
                    isSalamecheTurn = true;
                    updateTurn();
                }
            }
        });
        panel.add(usePotionButton, gbc);

        updateXpLabels();

        gbc.gridy = 6;
        panel.add(SalamecheXpLabel, gbc);
        gbc.gridx = 1;
        panel.add(BulbizarreXpLabel, gbc);

        frame.add(panel);
        frame.setVisible(true);

        updateTurn();
    }

    private static void updateXpLabels() {
        SalamecheXpLabel.setText("XP: " + Salameche.getXp() + " | Level: " + Salameche.getLevel());
        BulbizarreXpLabel.setText("XP: " + Bulbizarre.getXp() + " | Level: " + Bulbizarre.getLevel());
    }

    private static void updateTurn() {
        JMenuBar menuBar = frame.getJMenuBar();

        String turnText = isSalamecheTurn ? "C'est au tour de Salameche !" : "C'est au tour de Bulbizarre !";
        turnLabel.setForeground(isSalamecheTurn ? Color.RED : Color.GREEN);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        displayTurnText(turnText, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSalamecheTurn && Bulbizarre.isAlive()) {
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Salameche.takeDamage(Bulbizarre.getAttack());
                            SalamecheHpBar.setValue(Salameche.getHp());
                            SalamecheHpBar.setString(Salameche.getHp() + "/" + Salameche.getMaxHp());
                            checkGameOver();
                            isSalamecheTurn = true;
                            updateTurn();
                        }
                    });
                    timer.setRepeats(false); 
                    timer.start();
                }
            }
        });
    }

    private static void displayTurnText(String text, ActionListener onComplete) {
        turnLabel.setText("");
        Timer timer = new Timer(50, new ActionListener() { 
            int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < text.length()) {
                    turnLabel.setText(turnLabel.getText() + text.charAt(index));
                    index++;
                } else {
                    ((Timer) e.getSource()).stop();
                    onComplete.actionPerformed(e);
                }
            }
        });
        timer.start();
    }

    private static void checkGameOver() {
        if (!Salameche.isAlive()) {
            SalamecheHpBar.setString("Salameche is defeated!");
            Bulbizarre.gainXp(50);
        } else if (!Bulbizarre.isAlive()) {
            BulbizarreHpBar.setString("Bulbizarre is defeated!");
            Salameche.gainXp(50);
        }
        updateXpLabels();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}