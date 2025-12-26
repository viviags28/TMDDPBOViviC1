package model;

/*
 * =====================================================
 *  Class        : BenefitRepository.java
 *  Name         : Vivi Agustina Suryana
 *  Date         : 27-12-2025
 *  Email        : viviagustina@upi.edu
 *  Description  : This class handles database operations
 *                 such as retrieving and saving game result
 *                 data (CRUD-like operations).
 *  Architecture : MVVM (Model - View - ViewModel)
 *  Role         : Model (Repository / Data Access Layer)
 *  Table Used   : tbenefit
 *  Saya Vivi Agustina Suryana mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi
 *  Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 * =====================================================
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BenefitRepository {

    /*
     * Retrieves all records from the tbenefit table
     * and maps them into a list of Benefit objects.
     * @return List<Benefit>
     */
    public List<Benefit> getAll() {
        List<Benefit> list = new ArrayList<>();

        /*
         * Using try-with-resources to automatically
         * close database connection.
         */
        try (Connection c = Database.getConnection()) {

            String sql = "SELECT * FROM tbenefit";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            /*
             * Iterates through query results and maps
             * each row into a Benefit object.
             */
            while (rs.next()) {
                list.add(new Benefit(
                        rs.getString("username"),
                        rs.getInt("skor"),
                        rs.getInt("peluru_meleset"),
                        rs.getInt("peluru_sisa")
                ));
            }

        } catch (Exception e) {
            /*
             * Handles database-related exceptions.
             */
            e.printStackTrace();
        }

        return list;
    }

    /*
     * Saves a new benefit record if the username
     * does not exist, or updates the existing one
     * if it already exists.
     */
    public void saveOrUpdate(Benefit b) {

        try (Connection c = Database.getConnection()) {

            /*
             * Checks if the username already exists
             * in the database.
             */
            String check = "SELECT id FROM tbenefit WHERE username=?";
            PreparedStatement psCheck = c.prepareStatement(check);
            psCheck.setString(1, b.getUsername());
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                /*
                 * If record exists:
                 * - score is accumulated
                 * - missed bullets are accumulated
                 * - remaining bullets are updated
                 */
                String update = """
                    UPDATE tbenefit
                    SET skor = skor + ?,
                        peluru_meleset = peluru_meleset + ?,
                        peluru_sisa = ?
                    WHERE username = ?
                """;

                PreparedStatement ps = c.prepareStatement(update);
                ps.setInt(1, b.getSkor());
                ps.setInt(2, b.getPeluruMeleset());
                ps.setInt(3, b.getPeluruSisa());
                ps.setString(4, b.getUsername());
                ps.executeUpdate();

            } else {
                /*
                 * If record does not exist:
                 * inserts a new benefit record.
                 */
                String insert = """
                    INSERT INTO tbenefit
                    (username, skor, peluru_meleset, peluru_sisa)
                    VALUES (?, ?, ?, ?)
                """;

                PreparedStatement ps = c.prepareStatement(insert);
                ps.setString(1, b.getUsername());
                ps.setInt(2, b.getSkor());
                ps.setInt(3, b.getPeluruMeleset());
                ps.setInt(4, b.getPeluruSisa());
                ps.executeUpdate();
            }

        } catch (Exception e) {
            /*
             * Handles SQL or database connection errors.
             */
            e.printStackTrace();
        }
    }
}
