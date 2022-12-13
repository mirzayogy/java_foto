package frame;

import helpers.Koneksi;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class AnggotaInputFrame extends JFrame{
    private JTextField namaTextField;
    private JTextField alamatTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private JLabel imageLabel;
    private JPanel mainPanel;

    int id;

    public void setId(int id) {
        this.id = id;
    }

    public AnggotaInputFrame()  {
        imageLabel.setText("");
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void isiKomponen() {
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM anggota WHERE id = ?";
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                namaTextField.setText(rs.getString("nama"));
                alamatTextField.setText(rs.getString("alamat"));
                imageLabel.setIcon(new ImageIcon(getBufferedImage(rs.getBlob("foto"))));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public BufferedImage getBufferedImage(Blob imageBlob){
        InputStream binaryStream = null;
        BufferedImage b = null;
        try {
            binaryStream = imageBlob.getBinaryStream();
            b = ImageIO.read(binaryStream);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return b;
    }
}
