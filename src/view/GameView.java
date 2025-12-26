package view;

/*
 * =========================================================
 *  Class        : GameView
 *  Name         : Vivi Agustina Suryana
 *  Date         : 27-12-2025
 *  Email        : viviagustina@upi.edu
 *  Description  : This class handles
 *                 - Rendering game graphics (player, alien, obstacles)
 *                 - User input (keyboard)
 *                 - Game loop & animation
 *                 - Displaying HUD (score, ammo, username)
 *  Architecture : MVVM (Model - View - ViewModel)
 *  Role         : View
 *  Saya Vivi Agustina Suryana mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi
 *  Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 * =========================================================
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import java.awt.Image;

import view.MenuView;
import viewmodel.GameViewModel;
import model.Obstacle;

public class GameView extends JFrame {

    /*
     * Game logic handler.
     */
    private GameViewModel viewModel;

    private GamePanel gamePanel;

    private Timer timer;

    // ================= INPUT FLAG =================
    /*
     * Used for smooth movement (key hold).
     */
    private boolean leftPressed, rightPressed, upPressed, downPressed;
    private boolean moving = false;

    // ================= DIRECTION =================
    /*
     * flip sprite player
     */
    private enum Direction { LEFT, RIGHT }
    private Direction direction = Direction.LEFT;

    // ================= PLAYER SPRITE =================
    private BufferedImage[] idleFrames;
    private BufferedImage[] walkFrames;
    private BufferedImage[] deathFrames;

    private int frameIndex = 0;
    private int frameTick = 0;

    // ================= OBSTACLE =================
    private BufferedImage batuImage;

    // ================= ALIEN SPRITE =================
    private BufferedImage[] alienIdleFrames;
    private BufferedImage[] alienWalkFrames;
    private BufferedImage[] alienDeathFrames;

    private int alienFrameIndex = 0;
    private int alienFrameTick = 0;

    // ================= SIZE CONFIG =================
    /*
     * - HITBOX_SIZE     : collision
     * - DRAW_SIZE       : render sprite
     */
    private static final int HITBOX_SIZE = 32;
    private static final int PLAYER_DRAW_SIZE = 128;
    private static final int ALIEN_DRAW_SIZE = 128;
    private static final int BATU_DRAW_SIZE = 98;

    // ================= BACKGROUND =================
    private Image backgroundGame;

    /*
     * Constructor GameView.
     *
     * @param username
     */
    public GameView(String username) {
        viewModel = new GameViewModel(username);

        // Load semua asset
        loadPlayerSprites();
        loadAlienSprites();
        loadObstacleImage();
        loadBackground();

        setTitle("Hide And Seek The Challenge");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new GamePanel();
        add(gamePanel);

        // ================= GAME LOOP =================
        timer = new Timer(30, e -> {

            // ===== PLAYER MOVEMENT =====
            if (leftPressed) {
                viewModel.moveLeft();
                direction = Direction.LEFT;
            }
            if (rightPressed) {
                viewModel.moveRight();
                direction = Direction.RIGHT;
            }
            if (upPressed) viewModel.moveUp();
            if (downPressed) viewModel.moveDown();

            moving = leftPressed || rightPressed || upPressed || downPressed;

            // ===== GAME LOGIC =====
            viewModel.moveAlien();
            viewModel.shootAlienBullet();
            viewModel.moveAlienBullet();
            viewModel.movePlayerBullet();

            updateAnimation();

            // ===== GAME OVER CHECK =====
            if (viewModel.checkAlienHitPlayer() || viewModel.isGameOver()) {
                timer.stop();
                viewModel.saveResultToDatabase();
                JOptionPane.showMessageDialog(this, "GAME OVER");
                dispose();
                new MenuView().tampil();
            }

            repaint();
        });
        timer.start();

        // ================= KEYBOARD INPUT =================
        addKeyListener(new KeyAdapter() {

            /*
             * Event when button is pressed
             */
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> leftPressed = true;
                    case KeyEvent.VK_RIGHT -> rightPressed = true;
                    case KeyEvent.VK_UP -> upPressed = true;
                    case KeyEvent.VK_DOWN -> downPressed = true;

                    // Shoot the player's bullets
                    case KeyEvent.VK_Z -> viewModel.shootPlayerBullet();

                    // Back to menu
                    case KeyEvent.VK_SPACE -> {
                        timer.stop();
                        dispose();
                        new MenuView().tampil();
                    }
                }
            }

            /*
             * Event when the button is released
             */
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> leftPressed = false;
                    case KeyEvent.VK_RIGHT -> rightPressed = false;
                    case KeyEvent.VK_UP -> upPressed = false;
                    case KeyEvent.VK_DOWN -> downPressed = false;
                }
            }
        });

        setFocusable(true);
        setVisible(true);
    }

    // ================= LOAD PLAYER SPRITE =================
    /*
     * Load sprite player:
     * idle, walk, and death.
     */
    private void loadPlayerSprites() {
        try {
            idleFrames = new BufferedImage[6];
            for (int i = 0; i < idleFrames.length; i++) {
                idleFrames[i] = ImageIO.read(
                        getClass().getResource("/assets/images/player/idle_" + i + ".png")
                );
            }

            walkFrames = new BufferedImage[8];
            for (int i = 0; i < walkFrames.length; i++) {
                walkFrames[i] = ImageIO.read(
                        getClass().getResource("/assets/images/player/walk_" + i + ".png")
                );
            }

            deathFrames = new BufferedImage[10];
            for (int i = 0; i < deathFrames.length; i++) {
                deathFrames[i] = ImageIO.read(
                        getClass().getResource("/assets/images/player/death_" + i + ".png")
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= ANIMATION UPDATE =================
    private void updateAnimation() {
        frameTick++;
        if (frameTick % 6 == 0) frameIndex++;

        alienFrameTick++;
        if (alienFrameTick % 6 == 0) alienFrameIndex++;
    }

    // ================= LOAD OBSTACLE =================
    private void loadObstacleImage() {
        try {
            batuImage = ImageIO.read(
                    getClass().getResource("/assets/images/obstacle/batu.png")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD ALIEN SPRITE =================
    private void loadAlienSprites() {
        try {
            alienIdleFrames = new BufferedImage[6];
            for (int i = 0; i < alienIdleFrames.length; i++) {
                alienIdleFrames[i] = ImageIO.read(
                        getClass().getResource("/assets/images/alien/idle_" + i + ".png")
                );
            }

            alienWalkFrames = new BufferedImage[8];
            for (int i = 0; i < alienWalkFrames.length; i++) {
                alienWalkFrames[i] = ImageIO.read(
                        getClass().getResource("/assets/images/alien/walk_" + i + ".png")
                );
            }

            alienDeathFrames = new BufferedImage[10];
            for (int i = 0; i < alienDeathFrames.length; i++) {
                alienDeathFrames[i] = ImageIO.read(
                        getClass().getResource("/assets/images/alien/death_" + i + ".png")
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD BACKGROUND =================
    private void loadBackground() {
        backgroundGame = new ImageIcon(
                getClass().getResource("/assets/images/background_game.png")
        ).getImage();
    }

    // ================= GAME PANEL =================
    /*
     * Panel yang bertugas menggambar
     * seluruh elemen game.
     */
    class GamePanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // ===== BACKGROUND =====
            g2.drawImage(backgroundGame, 0, 0, getWidth(), getHeight(), null);

            g2.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
            );

            // ===== HUD =====
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRoundRect(20, 20, 260, 100, 15, 15);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Verdana", Font.BOLD, 16));
            g2.drawString("USERNAME : " + viewModel.getUsername(), 35, 50);
            g2.drawString("SKOR     : " + viewModel.getSkor(), 35, 75);
            g2.drawString("PELURU   : " + viewModel.getPeluru(), 35, 100);

            // ===== PLAYER =====
            BufferedImage frame;
            if (viewModel.isGameOver()) {
                frame = deathFrames[Math.min(frameIndex, deathFrames.length - 1)];
            } else if (moving) {
                frame = walkFrames[frameIndex % walkFrames.length];
            } else {
                frame = idleFrames[frameIndex % idleFrames.length];
            }

            int drawX = viewModel.getPlayerX() - (PLAYER_DRAW_SIZE - HITBOX_SIZE) / 2;
            int drawY = viewModel.getPlayerY() - (PLAYER_DRAW_SIZE - HITBOX_SIZE) / 2;

            if (direction == Direction.RIGHT) {
                g2.drawImage(frame, drawX, drawY, PLAYER_DRAW_SIZE, PLAYER_DRAW_SIZE, null);
            } else {
                g2.drawImage(frame, drawX + PLAYER_DRAW_SIZE, drawY,
                        -PLAYER_DRAW_SIZE, PLAYER_DRAW_SIZE, null);
            }

            // ===== ALIEN =====
            BufferedImage alienFrame = alienIdleFrames[
                    alienFrameIndex % alienIdleFrames.length
                    ];

            int alienX = viewModel.getAlienX();
            int alienY = viewModel.getAlienY();
            boolean alienFaceRight = alienX < viewModel.getPlayerX();

            if (alienFaceRight) {
                g2.drawImage(alienFrame, alienX, alienY,
                        ALIEN_DRAW_SIZE, ALIEN_DRAW_SIZE, null);
            } else {
                g2.drawImage(alienFrame, alienX + ALIEN_DRAW_SIZE, alienY,
                        -ALIEN_DRAW_SIZE, ALIEN_DRAW_SIZE, null);
            }

            // ===== BULLET =====
            if (viewModel.isAlienBulletActive()) {
                g2.setColor(Color.RED);
                g2.fillRect(viewModel.getAlienBulletX(),
                        viewModel.getAlienBulletY(), 8, 12);
            }

            if (viewModel.isPlayerBulletActive()) {
                g2.setColor(Color.YELLOW);
                g2.fillRect(viewModel.getPlayerBulletX(),
                        viewModel.getPlayerBulletY(), 8, 12);
            }

            // ===== OBSTACLE =====
            for (Obstacle o : viewModel.getObstacles()) {
                g2.drawImage(batuImage,
                        o.getX(), o.getY(),
                        BATU_DRAW_SIZE, BATU_DRAW_SIZE, null);
            }
        }
    }
}
