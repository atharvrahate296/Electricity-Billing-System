
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;

public class UpdateInformation extends JFrame implements ActionListener {

    JTextField tfaddress, tfcity, tfstate, tfemail, tfphone;
    JButton update, cancel;
    String meter;
    JLabel name;

    public UpdateInformation(String meter) {
        this.meter = meter;
        setBounds(300, 150, 1050, 450);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("UPDATE CUSTOMER INFORMATION");
        heading.setBounds(110, 0, 400, 30);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(heading);

        JLabel lblname = new JLabel("Name");
        lblname.setBounds(30, 70, 100, 20);
        add(lblname);

        name = new JLabel("");
        name.setBounds(230, 70, 200, 20);
        add(name);

        JLabel lblmeternumber = new JLabel("Meter Number");
        lblmeternumber.setBounds(30, 110, 100, 20);
        add(lblmeternumber);

        JLabel meternumber = new JLabel("");
        meternumber.setBounds(230, 110, 200, 20);
        add(meternumber);

        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(30, 150, 100, 20);
        add(lbladdress);

        tfaddress = new JTextField();
        tfaddress.setBounds(230, 150, 200, 20);
        add(tfaddress);

        JLabel lblcity = new JLabel("Registered Mobile Number");
        lblcity.setBounds(30, 190, 150, 20);
        add(lblcity);

        tfcity = new JTextField();
        tfcity.setBounds(230, 190, 200, 20);
        add(tfcity);

        try (java.sql.Connection conn = Database.getConnection()) {
            if (conn == null) throw new Exception("Database connection is null.");

            try (java.sql.PreparedStatement ps = conn.prepareStatement("SELECT name, meter_id, address, rmn FROM user WHERE meter_id = ?")) {

                ps.setString(1, meter);

                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        name.setText(rs.getString("name"));
                        meternumber.setText(rs.getString("meter_id"));
                        tfaddress.setText(rs.getString("address"));
                        tfcity.setText(rs.getString("rmn"));
                    } else {
                        JOptionPane.showMessageDialog(this, "Customer not found.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customer details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        update = new JButton("Update");
        update.setBackground(Color.BLACK);
        update.setForeground(Color.WHITE);
        update.setBounds(70, 360, 100, 25);
        add(update);
        update.addActionListener(this);

        cancel = new JButton("Cancel");
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.setBounds(230, 360, 100, 25);
        add(cancel);
        cancel.addActionListener(this);

        ImageIcon i1 = new ImageIcon("lib/icon/update.jpg");
        if (i1.getImage() != null) {
            Image i2 = i1.getImage().getScaledInstance(400, 300, Image.SCALE_DEFAULT);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel image = new JLabel(i3);
            image.setBounds(550, 50, 400, 300);
            add(image);
        }


        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            String address = tfaddress.getText();
            String rmn = tfcity.getText();

            try (java.sql.Connection c = Database.getConnection()) {
                if (c == null) throw new Exception("Database connection is null.");

                try (java.sql.PreparedStatement ps = c.prepareStatement("UPDATE user SET address = ?, rmn = ? WHERE meter_id = ?")) {
                    ps.setString(1, address);
                    ps.setString(2, rmn);
                    ps.setString(3, meter);
                    ps.executeUpdate();
                }
                JOptionPane.showMessageDialog(null, "User Information Updated Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating user information: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            setVisible(false);
        }
    }

    
}
