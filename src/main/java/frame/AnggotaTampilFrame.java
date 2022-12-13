package frame;

import helpers.Koneksi;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class AnggotaTampilFrame extends JFrame {
    private JPanel mainPanel;
    private JTable viewTable;
    private JButton button1;

    public AnggotaTampilFrame() {
        button1.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data dulu");
                return;
            }
            TableModel tm = viewTable.getModel();
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
            AnggotaInputFrame inputFrame = new AnggotaInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        isiTable();
        init();
    }

    private void init() {
        setContentPane(mainPanel);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTable(){
        Connection connection = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM anggota";

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(selectSQL);

            String header[] = {"ID","Nama","Alamat","Foto"};
            DefaultTableModel dtm = new DefaultTableModel(header,0){
                public Class getColumnClass(int column)
                {
                    return getValueAt(0, column).getClass();
                }
            };
            viewTable.setModel(dtm);
            viewTable.setPreferredScrollableViewportSize(viewTable.getPreferredSize());
            viewTable.setRowHeight(100);
            Object[] row =  new Object[4];
            while(rs.next()){
                Icon icon = new ImageIcon(getBufferedImage(rs.getBlob("foto")));
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                row[2] = rs.getString("alamat");
                row[3] = icon;
                dtm.addRow(row);
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
