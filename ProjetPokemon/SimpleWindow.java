

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleWindow {
    private static JFrame frame;
    private static Pokemon playerPokemon = PokemonStats.getPlayerPokemon();
    private static Pokemon currentOpponent = PokemonStats.getRandomPokemon();
    private static JProgressBar playerHpBar;
    private static JProgressBar opponentHpBar;
    private static JLabel playerImageLabel = new JLabel(new ImageIcon(SimpleWindow.class.getResource("/images/" + playerPokemon.getName() + "Back.png")));
    private static JLabel opponentImageLabel;
    private static JLabel turnLabel = new JLabel("", JLabel.CENTER);
    private static boolean isPlayerTurn = true;
    private static JLabel playerXpLabel = new JLabel();
    private static JLabel opponentXpLabel = new JLabel();

    public static void createAndShowGUI() {
        frame = new JFrame("Pokemon Battle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 520);

        initializeHpBars();
        initializeMenuBar();
        JPanel panel = createMainPanel();

        frame.add(panel);
        frame.setVisible(true);

        updateTurn();
    }

    private static void initializeHpBars() {
        playerHpBar = new JProgressBar(0, playerPokemon.getMaxHp());
        opponentHpBar = new JProgressBar(0, currentOpponent.getMaxHp());

        playerHpBar.setValue(playerPokemon.getHp());
        playerHpBar.setString(playerPokemon.getHp() + "/" + playerPokemon.getMaxHp());
        playerHpBar.setStringPainted(true);
        opponentHpBar.setValue(currentOpponent.getHp());
        opponentHpBar.setString(currentOpponent.getHp() + "/" + currentOpponent.getMaxHp());
        opponentHpBar.setStringPainted(true);
    }

    private static void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
    }

    private static JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("ProjetPokemon/images/Terrain.png");
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
        panel.add(playerImageLabel, gbc);

        gbc.gridx = 1;
        opponentImageLabel = new JLabel(new ImageIcon(SimpleWindow.class.getResource("/images/" + currentOpponent.getName() + ".png")));
        panel.add(opponentImageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(playerHpBar, gbc);

        gbc.gridx = 1;
        panel.add(opponentHpBar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton attackButton = createAttackButton();
        panel.add(attackButton, gbc);

        gbc.gridy = 5;
        JButton usePotionButton = createPotionButton();
        panel.add(usePotionButton, gbc);

        updateXpLabels();

        gbc.gridy = 6;
        panel.add(playerXpLabel, gbc);
        gbc.gridx = 1;
        panel.add(opponentXpLabel, gbc);

        return panel;
    }

    private static JButton createAttackButton() {
        JButton attackButton = new JButton("Attack");
        attackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isPlayerTurn && currentOpponent.isAlive()) {
                    currentOpponent.takeDamage(playerPokemon.getAttack());
                    opponentHpBar.setValue(currentOpponent.getHp());
                    opponentHpBar.setString(currentOpponent.getHp() + "/" + currentOpponent.getMaxHp());
                    checkGameOver();
                    isPlayerTurn = false;
                    updateTurn();
                }
            }
        });
        return attackButton;
    }

    private static JButton createPotionButton() {
        JButton usePotionButton = new JButton("Use Potion");
        usePotionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isPlayerTurn && playerPokemon.isAlive()) {
                    playerPokemon.heal(20);
                    playerHpBar.setValue(playerPokemon.getHp());
                    playerHpBar.setString(playerPokemon.getHp() + "/" + playerPokemon.getMaxHp());
                    isPlayerTurn = false;
                    updateTurn();
                } else if (!isPlayerTurn && currentOpponent.isAlive()) {
                    currentOpponent.heal(20);
                    opponentHpBar.setValue(currentOpponent.getHp());
                    opponentHpBar.setString(currentOpponent.getHp() + "/" + currentOpponent.getMaxHp());
                    isPlayerTurn = true;
                    updateTurn();
                }
            }
        });
        return usePotionButton;
    }

    private static void updateXpLabels() {
        playerXpLabel.setText("XP: " + playerPokemon.getXp() + " | Level: " + playerPokemon.getLevel());
        opponentXpLabel.setText("XP: " + currentOpponent.getXp() + " | Level: " + currentOpponent.getLevel());
    }

    private static void updateTurn() {
        if (!playerPokemon.isAlive()) {
            turnLabel.setText(playerPokemon.getName() + " is K.O!");
        } else if (!currentOpponent.isAlive()) {
            turnLabel.setText(currentOpponent.getName() + " is K.O!");
        } else {
            String turnText = isPlayerTurn ? "It's " + playerPokemon.getName() + "'s turn!" : "It's " + currentOpponent.getName() + "'s turn!";
            turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
            displayTurnText(turnText, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isPlayerTurn && currentOpponent.isAlive()) {
                        Timer timer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                playerPokemon.takeDamage(currentOpponent.getAttack());
                                playerHpBar.setValue(playerPokemon.getHp());
                                playerHpBar.setString(playerPokemon.getHp() + "/" + playerPokemon.getMaxHp());
                                checkGameOver();
                                isPlayerTurn = true;
                                updateTurn();
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            });
        }
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
        if (!playerPokemon.isAlive()) {
            playerHpBar.setString("K.O");
            currentOpponent.gainXp(50);
        } else if (!currentOpponent.isAlive()) {
            opponentHpBar.setString("K.O");
            playerPokemon.gainXp(5000);
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