package viewmodel;

/*
 * =========================================================
 *  Class        : MenuViewModel
 *  Name         : Vivi Agustina Suryana
 *  Date         : 27-12-2025
 *  Email        : viviagustina@upi.edu
 *  Description  : This class acts as a bridge between
 *                 - MenuView (UI)
 *                 - BenefitRepository (data source)
 *  Architecture : MVVM (Model - View - ViewModel)
 *  Role         : ViewModel
 *  Saya Vivi Agustina Suryana mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi
 *  Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 * =========================================================
 */

import model.Benefit;
import model.BenefitRepository;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel {

    /*
     * Stores error message if validation
     * or database operation fails.
     */
    private String error;

    /*
     * List data leaderboard
     */
    private List<Benefit> data;

    /*
     * Handles all database operations
     * related to Benefit.
     */
    private final BenefitRepository repo;

    /*
     * Constructor MenuViewModel.
     * Inisialisasi repository
     */
    public MenuViewModel() {
        repo = new BenefitRepository();

        data = new ArrayList<>();

        try {
            data = repo.getAll();
            System.out.println("JUMLAH DATA DB: " + data.size());
        } catch (Exception e) {
            error = e.getMessage();
            System.out.println("ERROR DB: " + error);
        }
    }

    /*
     * Validation before starting the game.
     */
    public void prosesPlay(String username) throws Exception {

        // Validasi username
        if (username == null || username.trim().isEmpty()) {
            error = "Username tidak boleh kosong!";
            throw new Exception(error);
        }
    }

    public int getSize() {
        return data.size();
    }

    /*
     * @param i index data
     * @return username
     */
    public String getUsername(int i) {
        return data.get(i).getUsername();
    }

    /*
     * @param i index data
     * @return skor
     */
    public int getSkor(int i) {
        return data.get(i).getSkor();
    }

    /*
     * @param i index data
     * @return peluru meleset
     */
    public int getPeluruMeleset(int i) {
        return data.get(i).getPeluruMeleset();
    }

    /*
     * @param i index data
     * @return peluru sisa
     */
    public int getPeluruSisa(int i) {
        return data.get(i).getPeluruSisa();
    }

    /*
     * @return error message
     */
    public String getError() {
        return error;
    }
}
