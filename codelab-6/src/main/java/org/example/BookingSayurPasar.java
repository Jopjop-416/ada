package org.example;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.*;
import java.io.*;
import java.util.List;

public class BookingSayurPasar {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Booking Sayur Pasar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null); // Posisikan di tengah layar
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 5, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNama = new JLabel("Nama:");
        JTextField tfNama = new JTextField(15);
        JLabel lblAlamat = new JLabel("Alamat:");
        JTextField tfAlamat = new JTextField(15);
        JLabel lblPesanan = new JLabel("Pesanan:");
        JTextField tfPesanan = new JTextField(15);
        JLabel lblJumlah = new JLabel("Jumlah (kg):");
        JTextField tfJumlah = new JTextField(15);

        JButton btnBooking = new JButton("Tambah Pesanan");
        btnBooking.setBackground(new Color(0, 153, 255));
        btnBooking.setForeground(Color.WHITE);

        JButton btnUpdate = new JButton("Update Pesanan");
        btnUpdate.setBackground(new Color(50, 97, 182));
        btnUpdate.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("Delete Pesanan");
        btnDelete.setBackground(new Color(229, 4, 58));
        btnDelete.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(lblNama, gbc);
        gbc.gridx = 1;
        inputPanel.add(tfNama, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(lblAlamat, gbc);
        gbc.gridx = 1;
        inputPanel.add(tfAlamat, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(lblPesanan, gbc);
        gbc.gridx = 1;
        inputPanel.add(tfPesanan, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(lblJumlah, gbc);
        gbc.gridx = 1;
        inputPanel.add(tfJumlah, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        inputPanel.add(btnBooking, gbc);
        gbc.gridy = 5;
        inputPanel.add(btnUpdate, gbc);
        gbc.gridy = 6;
        inputPanel.add(btnDelete, gbc);

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Nama", "Alamat", "Pesanan", "Jumlah (kg)"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        btnBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Validasi input form
                    if (tfNama.getText().isEmpty() || tfAlamat.getText().isEmpty() || tfPesanan.getText().isEmpty() || tfJumlah.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // Menyimpan data pesanan
                        String nama = tfNama.getText();
                        String alamat = tfAlamat.getText();
                        String pesanan = tfPesanan.getText();
                        double jumlah = Double.parseDouble(tfJumlah.getText());

                        // Menambahkan baris data ke tabel
                        tableModel.addRow(new Object[]{nama, alamat, pesanan, jumlah});

                        // Menyimpan data pesanan ke file menggunakan Java API
                        savePesananToFile(nama, alamat, pesanan, jumlah);

                        // Reset form input
                        tfNama.setText("");
                        tfAlamat.setText("");
                        tfPesanan.setText("");
                        tfJumlah.setText("");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Jumlah harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String namaInput = JOptionPane.showInputDialog(frame, "Masukkan Nama Pesanan untuk update:");
                String alamatInput = JOptionPane.showInputDialog(frame, "Masukkan Alamat Pesanan untuk update:");
                String pesananInput = JOptionPane.showInputDialog(frame, "Masukkan Nama Pesanan untuk update:");

                try {
                    boolean found = false;
                    List<String> lines = Files.readAllLines(Paths.get("pesanan.txt"));
                    for (int i = 0; i < lines.size(); i++) {
                        String line = lines.get(i);
                        if (line.contains("Nama: " + namaInput) && line.contains("Alamat: " + alamatInput) && line.contains("Pesanan: " + pesananInput)) {
                            found = true;
                            String newNama = JOptionPane.showInputDialog(frame, "Masukkan Nama Baru:");
                            String newAlamat = JOptionPane.showInputDialog(frame, "Masukkan Alamat Baru:");
                            String newPesanan = JOptionPane.showInputDialog(frame, "Masukkan Nama Pesanan Baru:");
                            double newJumlah = Double.parseDouble(JOptionPane.showInputDialog(frame, "Masukkan Jumlah (kg):"));

                            lines.set(i, "Nama: " + newNama + ", Alamat: " + newAlamat + ", Pesanan: " + newPesanan + ", Jumlah: " + newJumlah + " kg");

                            Files.write(Paths.get("pesanan.txt"), lines);

                            updateTableWithFileData(tableModel);

                            JOptionPane.showMessageDialog(frame, "Pesanan berhasil diperbarui.");
                            break;
                        }
                    }

                    if (!found) {
                        throw new Exception("Pesanan tidak ditemukan.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String namaInput = JOptionPane.showInputDialog(frame, "Masukkan Nama Pesanan untuk dihapus:");
                String alamatInput = JOptionPane.showInputDialog(frame, "Masukkan Alamat Pesanan untuk dihapus:");
                String pesananInput = JOptionPane.showInputDialog(frame, "Masukkan Nama Pesanan untuk dihapus:");

                try {
                    boolean found = false;
                    List<String> lines = Files.readAllLines(Paths.get("pesanan.txt"));
                    for (int i = 0; i < lines.size(); i++) {
                        String line = lines.get(i);
                        if (line.contains("Nama: " + namaInput) && line.contains("Alamat: " + alamatInput) && line.contains("Pesanan: " + pesananInput)) {
                            found = true;

                            lines.remove(i);

                            Files.write(Paths.get("pesanan.txt"), lines);

                            updateTableWithFileData(tableModel);

                            JOptionPane.showMessageDialog(frame, "Pesanan berhasil dihapus.");
                            break;
                        }
                    }

                    if (!found) {
                        throw new Exception("Pesanan tidak ditemukan.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setVisible(true);

        loadPesananFromFile(tableModel);
    }

    private static void savePesananToFile(String nama, String alamat, String pesanan, double jumlah) {
        try {
            Path filePath = Paths.get("pesanan.txt");

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            String pesananData = "Nama: " + nama + ", Alamat: " + alamat + ", Pesanan: " + pesanan + ", Jumlah: " + jumlah + " kg\n";
            Files.write(filePath, pesananData.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan pesanan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void loadPesananFromFile(DefaultTableModel tableModel) {
        try {
            Path filePath = Paths.get("pesanan.txt");
            if (Files.exists(filePath)) {
                List<String> lines = Files.readAllLines(filePath);
                for (String line : lines) {
                    String[] data = line.split(", ");
                    String nama = data[0].split(": ")[1];
                    String alamat = data[1].split(": ")[1];
                    String pesanan = data[2].split(": ")[1];
                    double jumlah = Double.parseDouble(data[3].split(": ")[1].replace(" kg", ""));
                    tableModel.addRow(new Object[]{nama, alamat, pesanan, jumlah});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat memuat pesanan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateTableWithFileData(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        loadPesananFromFile(tableModel);
    }
}
