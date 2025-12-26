package model;

/*
 * =====================================================
 *  Class        : Database.java
 *  Name         : Vivi Agustina Suryana
 *  Date         : 27-12-2025
 *  Email        : viviagustina@upi.edu
 *  Description  : This class is responsible for handling
 *                 database connection configuration and
 *                 providing a reusable connection object.
 *  Architecture : MVVM (Model - View - ViewModel)
 *  Saya Vivi Agustina Suryana mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi
 *  Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 * =====================================================
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    /*
     * MySQL database connection URL including:
     * - host      : localhost
     * - port      : 3306
     * - database  : tmd_dpbo
     */
    private static final String URL =
            "jdbc:mysql://localhost:3306/tmd_dpbo?user=root&password=";

    /*
     * Database authentication username.
     */
    private static final String USER = "root";

    /*
     * Database authentication password
     * (leave empty if no password is set).
     */
    private static final String PASS = ""; // isi jika ada password

    /*
     * This method returns an active Connection object
     * that can be used by Repository or ViewModel
     * to execute SQL queries.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
