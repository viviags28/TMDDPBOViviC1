package model;

/*
 * =====================================================
 *  Class        : Benefit.java
 *  Name         : Vivi Agustina Suryana
 *  Date         : 27-12-2025
 *  Email        : viviagustina@upi.edu
 *  Description  : This class acts as a data container
 *                 to store game results such as score,
 *                 missed bullets, and remaining bullets.
 *  Architecture : MVVM (Model - View - ViewModel)
 *  Saya Vivi Agustina Suryana mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi
 *  Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 * =====================================================
 */

public class Benefit {

    /*
     * Stores the player's username.
     */
    private final String username;

    /*
     * Player's score achieved in the game.
     */
    private final int skor;

    /*
     * Number of missed alien bullets.
     */
    private final int peluruMeleset;

    /*
     * Remaining bullets owned by the player.
     */
    private final int peluruSisa;

    /*
     * Constructor Benefit.
     * Initializes a Benefit object with game result data.
     * @param username
     * @param skor
     * @param peluruMeleset
     * @param peluruSisa
     */
    public Benefit(String username, int skor, int peluruMeleset, int peluruSisa) {
        this.username = username;
        this.skor = skor;
        this.peluruMeleset = peluruMeleset;
        this.peluruSisa = peluruSisa;
    }

    public String getUsername() {
        return username;
    }

    public int getSkor() {
        return skor;
    }

    public int getPeluruMeleset() {
        return peluruMeleset;
    }


    public int getPeluruSisa() {
        return peluruSisa;
    }
}
