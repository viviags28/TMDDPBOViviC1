package viewmodel;

/*
 * =========================================================
 *  Class        : GameViewModel
 *  Name         : Vivi Agustina Suryana
 *  Date         : 27-12-2025
 *  Email        : viviagustina@upi.edu
 *  Description  : This class handles all core game logic
 *                 - Player movement
 *                 - Alien behavior
 *                 - Bullet mechanics
 *                 - Collision detection
 *                 - Scoring system
 *  Architecture : MVVM (Model - View - ViewModel)
 *  Role         : ViewModel
 *  Saya Vivi Agustina Suryana mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi
 *  Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 * =========================================================
 */

import model.Benefit;
import model.BenefitRepository;
import model.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class GameViewModel {

    /*
     * Direction enum for facing orientation.
     */
    public enum Direction {
        LEFT, RIGHT
    }

    private Direction playerDirection = Direction.RIGHT;
    private Direction alienDirection = Direction.LEFT;

    /*
     * Player data and game statistics.
     */
    private String username;
    private int skor;
    private int peluru;
    private int peluruMeleset;

    // ================= PLAYER =================

    /*
     * hitbox-based
     */
    private int playerX;
    private int playerY;

    private final int PLAYER_SIZE = 128;
    private final int PLAYER_HITBOX = 32;
    private final int SPEED = 8;

    // ================= ALIEN =================

    /*
     * Alien position and motion parameters
     */
    private int alienX;
    private int alienY;

    private final int ALIEN_SIZE = 128;
    private final int ALIEN_HITBOX = 32;
    private final int ALIEN_SPEED = 3;

    /*
     * Game screen size
     */
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;

    // ================= OBSTACLE =================

    private List<Obstacle> obstacles = new ArrayList<>();
    private final int OBSTACLE_COUNT = 4;

    // ================= BULLET ALIEN =================

    private int alienBulletX, alienBulletY;
    private int alienBulletDX, alienBulletDY;
    private boolean alienBulletActive = false;

    private final int ALIEN_BULLET_SPEED = 6;
    private int alienShootCooldown = 0;

    /*
     * Initial delay before alien starts shooting.
     */
    private int alienStartDelay = 60; // ~1 detik

    // ================= BULLET PLAYER =================
    private int playerBulletX, playerBulletY;
    private int playerBulletDX, playerBulletDY;
    private boolean playerBulletActive = false;

    private final int PLAYER_BULLET_SPEED = 10;
    private boolean gameOver = false;
    private BenefitRepository benefitRepository;

    /*
     * Constructor GameViewModel.
     */
    public GameViewModel(String username) {
        this.username = username;

        skor = 0;
        peluru = 0;          // starting from 0
        peluruMeleset = 0;

        benefitRepository = new BenefitRepository();

        // The player's initial position is in the center of the screen
        playerX = SCREEN_WIDTH / 2;
        playerY = SCREEN_HEIGHT / 2;

        respawnAlien();
        generateObstacles();
    }

    // ================= OBSTACLE =================

    /*
     * Generate obstacles randomly
     */
    private void generateObstacles() {
        obstacles.clear();
        for (int i = 0; i < OBSTACLE_COUNT; i++) {
            int x = 50 + (int) (Math.random() * (SCREEN_WIDTH - 150));
            int y = 100 + (int) (Math.random() * (SCREEN_HEIGHT - 200));
            obstacles.add(new Obstacle(x, y));
        }
    }

    /*
     * Checking for collisions with obstacles
     */
    private boolean hitObstacle(int x, int y) {
        for (Obstacle o : obstacles) {
            if (o.collides(x, y, PLAYER_HITBOX)) return true;
        }
        return false;
    }

    // ================= PLAYER MOVE =================
    public void moveLeft() {
        int nx = playerX - SPEED;
        if (!hitObstacle(nx, playerY)) {
            playerX = Math.max(0, nx);
            playerDirection = Direction.LEFT;
        }
    }

    public void moveRight() {
        int nx = playerX + SPEED;
        if (!hitObstacle(nx, playerY)) {
            playerX = Math.min(SCREEN_WIDTH - PLAYER_HITBOX, nx);
            playerDirection = Direction.RIGHT;
        }
    }

    public void moveUp() {
        int ny = playerY - SPEED;
        if (!hitObstacle(playerX, ny)) {
            playerY = Math.max(0, ny);
        }
    }

    public void moveDown() {
        int ny = playerY + SPEED;
        if (!hitObstacle(playerX, ny)) {
            playerY = Math.min(SCREEN_HEIGHT - PLAYER_HITBOX, ny);
        }
    }

    // ================= ALIEN =================

    /*
     * Alien movement vertically upwards
     */
    public void moveAlien() {
        alienY -= ALIEN_SPEED;
        alienDirection = (playerX < alienX) ? Direction.LEFT : Direction.RIGHT;

        if (alienY < -ALIEN_SIZE) respawnAlien();
    }

    private void respawnAlien() {
        alienX = (int) (Math.random() * (SCREEN_WIDTH - ALIEN_SIZE));
        alienY = SCREEN_HEIGHT;
    }

    // ================= ALIEN BULLET =================

    public void shootAlienBullet() {
        if (gameOver) return;

        // Delay awal sebelum alien aktif
        if (alienStartDelay > 0) {
            alienStartDelay--;
            return;
        }

        if (alienShootCooldown > 0) {
            alienShootCooldown--;
            return;
        }

        if (!alienBulletActive) {
            alienBulletX = (alienDirection == Direction.RIGHT)
                    ? alienX + ALIEN_SIZE - 20
                    : alienX + 20;

            alienBulletY = alienY + ALIEN_SIZE / 2;

            int dx = playerX - alienBulletX;
            int dy = playerY - alienBulletY;
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len == 0) return;

            alienBulletDX = (int) (ALIEN_BULLET_SPEED * dx / len);
            alienBulletDY = (int) (ALIEN_BULLET_SPEED * dy / len);

            alienBulletActive = true;
            alienShootCooldown = 50;
        }
    }

    public void moveAlienBullet() {
        if (!alienBulletActive) return;

        alienBulletX += alienBulletDX;
        alienBulletY += alienBulletDY;

        for (Obstacle o : obstacles) {
            if (o.collides(alienBulletX, alienBulletY, 8)) {
                alienBulletActive = false;
                peluru++;
                peluruMeleset++;
                return;
            }
        }

        if (checkBulletHitPlayer()) {
            gameOver = true;
            alienBulletActive = false;
        }

        if (alienBulletX < 0 || alienBulletX > SCREEN_WIDTH
                || alienBulletY < 0 || alienBulletY > SCREEN_HEIGHT) {
            alienBulletActive = false;
            peluru++;
            peluruMeleset++;
        }
    }

    // ================= PLAYER BULLET =================

    public void shootPlayerBullet() {
        if (playerBulletActive || peluru <= 0 || gameOver) return;

        playerBulletX = (playerDirection == Direction.RIGHT)
                ? playerX + PLAYER_SIZE - 20
                : playerX + 20;

        playerBulletY = playerY + PLAYER_SIZE / 2;

        int dx = alienX - playerBulletX;
        int dy = alienY - playerBulletY;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return;

        playerBulletDX = (int) (PLAYER_BULLET_SPEED * dx / len);
        playerBulletDY = (int) (PLAYER_BULLET_SPEED * dy / len);

        playerBulletActive = true;
        peluru--;
    }

    public void movePlayerBullet() {
        if (!playerBulletActive) return;

        playerBulletX += playerBulletDX;
        playerBulletY += playerBulletDY;

        for (Obstacle o : obstacles) {
            if (o.collides(playerBulletX, playerBulletY, 8)) {
                playerBulletActive = false;
                return;
            }
        }

        if (checkBulletHitAlien()) {
            skor += 10;
            playerBulletActive = false;
            respawnAlien();
        }

        if (playerBulletX < 0 || playerBulletX > SCREEN_WIDTH
                || playerBulletY < 0 || playerBulletY > SCREEN_HEIGHT) {
            playerBulletActive = false;
        }
    }

    // ================= COLLISION =================

    private boolean checkBulletHitPlayer() {
        return alienBulletX < playerX + PLAYER_HITBOX &&
                alienBulletX + 6 > playerX &&
                alienBulletY < playerY + PLAYER_HITBOX &&
                alienBulletY + 10 > playerY;
    }

    private boolean checkBulletHitAlien() {
        return playerBulletX < alienX + ALIEN_HITBOX &&
                playerBulletX + 6 > alienX &&
                playerBulletY < alienY + ALIEN_HITBOX &&
                playerBulletY + 10 > alienY;
    }

    public boolean checkAlienHitPlayer() {
        return alienX < playerX + PLAYER_HITBOX &&
                alienX + ALIEN_HITBOX > playerX &&
                alienY < playerY + PLAYER_HITBOX &&
                alienY + ALIEN_HITBOX > playerY;
    }

    /*
     * Save game results to database
     */
    public void saveResultToDatabase() {
        benefitRepository.saveOrUpdate(
                new Benefit(username, skor, peluruMeleset, peluru)
        );
    }

    // ================= GETTER =================

    public boolean isGameOver() { return gameOver; }
    public String getUsername() { return username; }
    public int getSkor() { return skor; }
    public int getPeluru() { return peluru; }

    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }

    public int getAlienX() { return alienX; }
    public int getAlienY() { return alienY; }

    public boolean isAlienBulletActive() { return alienBulletActive; }
    public int getAlienBulletX() { return alienBulletX; }
    public int getAlienBulletY() { return alienBulletY; }

    public boolean isPlayerBulletActive() { return playerBulletActive; }
    public int getPlayerBulletX() { return playerBulletX; }
    public int getPlayerBulletY() { return playerBulletY; }

    public List<Obstacle> getObstacles() { return obstacles; }
}
