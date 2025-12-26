package model;

/*
 * =====================================================
 *  Class        : Obstacle.java
 *  Name         : Vivi Agustina Suryana
 *  Date         : 27-12-2025
 *  Email        : viviagustina@upi.edu
 *  Description  : This class represents an obstacle
 *                 entity used in the game as a barrier
 *                 or protection.
 *
 *  Architecture : MVVM (Model - View - ViewModel)
 *  Role         : Model (Game Object / Entity)
 *  Used In      : GameView, GameViewModel
 *  Saya Vivi Agustina Suryana mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi
 *  Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 * =====================================================
 */

public class Obstacle {

    /*
     * X and Y position of the obstacle in the game world.
     */
    private int x;
    private int y;

    /*
     * Size of the obstacle (square shape).
     */
    private int size;

    /*
     * Constructor Obstacle.
     * Creates a new obstacle at a given position
     * with a default size.
     */
    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 60; // default obstacle size
    }

    /*
     * Checks collision with another object
     * using Axis-Aligned Bounding Box (AABB).
     * @param ox
     * @param oy
     * @param otherSize
     * @return true
     */
    public boolean collides(int ox, int oy, int otherSize) {
        return ox < x + size &&
                ox + otherSize > x &&
                oy < y + size &&
                oy + otherSize > y;
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }
}
