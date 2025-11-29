
import java.awt.*;
import javax.swing.*;

public class ViewInformation extends JFrame {

    public ViewInformation(String meter) {
        setBounds(350, 150, 850, 650);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("VIEW CUSTOMER INFORMATION");
        heading.setBounds(250, 0, 500, 40);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(heading);

        JLabel lblname = new JLabel("Name");
        lblname.setBounds(70, 80, 100, 20);
        add(lblname);

        JLabel name = new JLabel("");
        name.setBounds(250, 80, 100, 20);
        add(name);

        JLabel lblmeternumber = new JLabel("Meter Number");
        lblmeternumber.setBounds(70, 140, 100, 20);
        add(lblmeternumber);

        JLabel meternumber = new JLabel("");
        meternumber.setBounds(250, 140, 100, 20);
        add(meternumber);

        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(70, 200, 100, 20);
        add(lbladdress);

        JLabel address = new JLabel("");
        address.setBounds(250, 200, 100, 20);
        add(address);

        JLabel lblcity = new JLabel("Registered Mobile Number");
        lblcity.setBounds(70, 260, 150, 20);
        add(lblcity);

        JLabel rmn = new JLabel("");
        rmn.setBounds(250, 260, 100, 20);
        add(rmn);

        try (java.sql.Connection conn = Database.getConnection()) {
            if (conn == null) throw new Exception("Database connection is null.");

            try (java.sql.PreparedStatement ps = conn.prepareStatement("SELECT name, meter_id, address, rmn FROM user WHERE meter_id = ?")) {

                ps.setInt(1, Integer.parseInt(meter));

                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        name.setText(rs.getString("name"));
                        meternumber.setText(rs.getString("meter_id"));
                        address.setText(rs.getString("address"));
                        rmn.setText(rs.getString("rmn"));
                    } else {
                        JOptionPane.showMessageDialog(this, "Customer not found.");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customer details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        ImageIcon i1 = new ImageIcon("lib/icon/viewcustomer.jpg");
        if (i1.getImage() != null) {
            Image i2 = i1.getImage().getScaledInstance(600, 300, Image.SCALE_DEFAULT);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel image = new JLabel(i3);
            image.setBounds(20, 350, 600, 300);
            add(image);
        }

        setVisible(true);
    }

    
}
