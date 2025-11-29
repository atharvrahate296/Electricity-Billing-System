import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CustomerDetails extends JFrame implements ActionListener {

    JTable table;
    JButton delete, back, exit, save, refresh;
    private DefaultTableModel model;
    public CustomerDetails() {
        super("Customer Details");

        model = new DefaultTableModel();

        setSize(1200, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        // ---------- HEADER ----------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Customer Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(40, 40, 40));

        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ---------- TABLE ----------
        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Meter Number not editable
            }
        };

        model.addColumn("Meter Number");
        model.addColumn("Name");
        model.addColumn("Username");
        model.addColumn("Address");
        model.addColumn("Registered Mobile Number");

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(220, 235, 255));
        table.setGridColor(new Color(220, 220, 220));

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 14));
        th.setBackground(new Color(235, 239, 245));
        th.setForeground(Color.DARK_GRAY);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new EmptyBorder(10, 20, 10, 20));

        add(sp, BorderLayout.CENTER);

        // ---------- ACTION BAR ----------
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setBorder(new EmptyBorder(10, 20, 15, 20));
        actionBar.setBackground(new Color(245, 247, 250));

        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftActions.setOpaque(false);

        save = createButton("Save", new Color(34, 139, 34));
        refresh = createButton("Refresh", new Color(52, 120, 220));
        delete = createButton("Delete", new Color(200, 60, 60));

        leftActions.add(save);
        leftActions.add(refresh);
        leftActions.add(delete);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightActions.setOpaque(false);

        back = createButton("Back", new Color(130, 130, 130));
        exit = createButton("Exit", new Color(130, 130, 130));

        rightActions.add(back);
        rightActions.add(exit);

        actionBar.add(leftActions, BorderLayout.WEST);
        actionBar.add(rightActions, BorderLayout.EAST);

        add(actionBar, BorderLayout.SOUTH);

        // ---------- Button Actions ----------
        save.addActionListener(this);
        refresh.addActionListener(this);
        delete.addActionListener(this);
        back.addActionListener(this);
        exit.addActionListener(this);

        refreshTable();
        setVisible(true);
    }

    // ---------- UI HELPERS ----------
private JButton createButton(String text, Color bg) {
    JButton b = new JButton(text) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            // Button background
            if (getModel().isPressed()) {
                g2.setColor(bg.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(bg.brighter());
            } else {
                g2.setColor(bg);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.dispose();

            super.paintComponent(g);
        }
    };

    b.setFont(new Font("Segoe UI", Font.BOLD, 14));
    b.setForeground(Color.WHITE);
    b.setContentAreaFilled(false);
    b.setBorderPainted(false);
    b.setFocusPainted(false);
    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    b.setBorder(new EmptyBorder(10, 22, 10, 22));
    b.setOpaque(false);

    return b;
}


    // ---------- DATA ----------
    void refreshTable() {
        model.setRowCount(0);
        try (java.sql.Connection c = Database.getConnection()) {
            if (c == null) throw new Exception("Database connection is null.");

            try (java.sql.PreparedStatement ps = c.prepareStatement("SELECT meter_id, name, username, address, rmn FROM user");
                 java.sql.ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("meter_id"),
                            rs.getString("name"),
                            rs.getString("username"),
                            rs.getString("address"),
                            rs.getString("rmn")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- FUNCTIONALITY (UNCHANGED) ----------
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == save) {
            try (java.sql.Connection c = Database.getConnection()) {
                if (c == null) throw new Exception("Database connection is null.");

                try (java.sql.PreparedStatement ps = c.prepareStatement("UPDATE user SET name = ?, username = ?, address = ?, rmn = ? WHERE meter_id = ?")) {

                    for (int i = 0; i < model.getRowCount(); i++) {
                        int meterNumber = (int) model.getValueAt(i, 0);
                        String name = (String) model.getValueAt(i, 1);
                        String username = (String) model.getValueAt(i, 2);
                        String address = (String) model.getValueAt(i, 3);
                        String rmn = (String) model.getValueAt(i, 4);

                        ps.setString(1, name);
                        ps.setString(2, username);
                        ps.setString(3, address);
                        ps.setString(4, rmn);
                        ps.setInt(5, meterNumber);
                        ps.addBatch(); // Add to batch for efficiency
                    }
                    ps.executeBatch(); // Execute all updates
                    JOptionPane.showMessageDialog(this, "Customer Details Updated Successfully");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating customer details");
            }

        } else if (ae.getSource() == refresh) {
            refreshTable();

        } else if (ae.getSource() == delete) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                try (java.sql.Connection c = Database.getConnection()) {
                    if (c == null) throw new Exception("Database connection is null.");

                    try (java.sql.PreparedStatement ps = c.prepareStatement("DELETE FROM user WHERE meter_id = ?")) {

                        int meterNumber = (int) table.getValueAt(selectedRow, 0);
                        ps.setInt(1, meterNumber);
                        ps.executeUpdate();

                        refreshTable();
                        JOptionPane.showMessageDialog(this, "Customer Deleted Successfully");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error deleting customer");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer to delete");
            }

        } else if (ae.getSource() == back || ae.getSource() == exit) {
            setVisible(false);
        }
    }
}

