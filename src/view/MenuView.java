package view;

/*
 * =====================================================
 *  Class        : MenuView.java
 *  Name         : Vivi Agustina Suryana
 *  Date         : 27-12-2025
 *  Email        : viviagustina@upi.edu
 *  Description  : This class represents the main menu
 *                 view of the game, displaying:
 *                 - Game title
 *                 - Score table (history)
 *                 - Username input
 *                 - Play & Exit buttons
 *
 *  Architecture : MVVM (Model - View - ViewModel)
 *  Role         : View
 *  Saya Vivi Agustina Suryana mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi
 *  Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 * =====================================================
 */

import viewmodel.MenuViewModel;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

public class MenuView {

    /*
     * ViewModel acts as a bridge between
     * the View and the data layer.
     */
    private final MenuViewModel menuViewModel;

    /*
     * JTable used to display player history.
     */
    private JTable table;

    /*
     * Constructor MenuView.
     */
    public MenuView() {
        menuViewModel = new MenuViewModel();
    }

    /*
     * Main method to render the menu UI.
     */
    public void tampil() {
        JFrame frame = new JFrame("Hide and Seek The Challenge");

        // ================= BACKGROUND =================
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());

        // ================= TITLE =================
        JLabel title = new JLabel("HIDE AND SEEK : THE CHALLENGE", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // ================= TABLE =================
        table = new JTable();
        loadTable();

        table.setEnabled(false);
        table.setShowGrid(false);
        table.setRowHeight(28);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(new Color(0, 0, 0, 0));

        // ===== TABLE HEADER STYLE =====
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(20, 20, 20, 200));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 32));
        header.setReorderingAllowed(false);

        /*
         * Custom cell renderer
         * Alternating row color (zebra table).
         */
        table.setDefaultRenderer(Object.class, (tbl, value, isSelected, hasFocus, row, col) -> {
            JLabel cell = new JLabel(value.toString());
            cell.setOpaque(true);
            cell.setForeground(Color.WHITE);
            cell.setFont(new Font("Consolas", Font.PLAIN, 13));
            cell.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

            if (row % 2 == 0) {
                cell.setBackground(new Color(0, 0, 0, 130));
            } else {
                cell.setBackground(new Color(0, 0, 0, 170));
            }
            return cell;
        });

        // ===== SCROLL PANE =====
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(460, 230));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(
                new Color(255, 255, 255, 70)
        ));

        JPanel tableWrapper = new JPanel();
        tableWrapper.setOpaque(false);
        tableWrapper.add(scrollPane);

        mainPanel.add(tableWrapper, BorderLayout.CENTER);

        // ================= BOTTOM PANEL =================
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2, 12, 12));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 35, 150));

        JLabel lblUsername = new JLabel("USERNAME");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 13));

        JTextField txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnPlay = new JButton("PLAY");
        btnPlay.setFont(new Font("Arial", Font.BOLD, 14));
        btnPlay.setBackground(new Color(80, 200, 120));
        btnPlay.setForeground(Color.WHITE);
        btnPlay.setFocusPainted(false);

        JButton btnExit = new JButton("EXIT");
        btnExit.setFont(new Font("Arial", Font.BOLD, 14));
        btnExit.setBackground(new Color(200, 80, 80));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);

        /*
         * Action button PLAY:
         */
        btnPlay.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            try {
                menuViewModel.prosesPlay(username);
                frame.dispose();
                new GameView(username);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, menuViewModel.getError());
            }
        });

        // Action button EXIT
        btnExit.addActionListener(e -> System.exit(0));

        bottomPanel.add(lblUsername);
        bottomPanel.add(txtUsername);
        bottomPanel.add(btnPlay);
        bottomPanel.add(btnExit);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ================= FRAME SETUP =================
        frame.add(mainPanel);
        frame.setSize(900, 520);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /*
     * Loads data from ViewModel into JTable.
     */
    private void loadTable() {
        String[] kolom = {
                "Username", "Skor", "Peluru Meleset", "Peluru Sisa"
        };

        String[][] isi = new String[menuViewModel.getSize()][4];

        for (int i = 0; i < menuViewModel.getSize(); i++) {
            isi[i][0] = menuViewModel.getUsername(i);
            isi[i][1] = String.valueOf(menuViewModel.getSkor(i));
            isi[i][2] = String.valueOf(menuViewModel.getPeluruMeleset(i));
            isi[i][3] = String.valueOf(menuViewModel.getPeluruSisa(i));
        }

        table.setModel(new DefaultTableModel(isi, kolom));
    }

    /*
     * Custom panel to draw background image.
     */
    class BackgroundPanel extends JPanel {

        private final Image bg;

        public BackgroundPanel() {
            bg = new ImageIcon(
                    getClass().getResource("/assets/images/background_menu.png")
            ).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
