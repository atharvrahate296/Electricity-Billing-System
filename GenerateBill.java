import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class GenerateBill extends JFrame implements ActionListener {

    JTextField tfunits;
    JLabel lblname, labeladdress, finalAmount;

    JButton generate, cancel, calculate, exit;

    JComboBox<String> meternumber, cmonth, cyear, cstatus;

    // ---- THEME (SAME AS LOGIN & MAINAPP) ----
    private static final Color FRAME_BG = new Color(245, 247, 250);
    private static final Color PRIMARY_COLOR = new Color(30, 144, 255); // softer blue
    private static final Color HEADER_BG = new Color(52, 58, 64);       // dark gray
    private static final Color MUTED_BTN = new Color(160, 160, 160);   // soft gray
    private static final Color CALCULATE_COLOR = new Color(72, 201, 176); // teal

    private static final Color TEXT_GRAY = new Color(52, 58, 64);
    private static final Color BORDER_GRAY = new Color(220, 224, 229);

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public GenerateBill() {

        setTitle("Generate Bill");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FRAME_BG);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setPreferredSize(new Dimension(900, 50)); // Add this line — adjust 80 to desired height
        header.setBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR)
        );

        JLabel heading = new JLabel("       Generate Electricity Bill");
        heading.setFont(TITLE_FONT);
        heading.setForeground(Color.WHITE);


        header.add(heading, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ================= CARD =================
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(760, 520));
        card.setOpaque(true);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_GRAY, 1, true),
                        new EmptyBorder(30, 35, 30, 35)
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridy = 0;

        // ================= FORM =================
        meternumber = new JComboBox<>();
        try (java.sql.Connection conn = Database.getConnection()) {
            if (conn == null) throw new Exception("Database connection is null.");

            try (java.sql.PreparedStatement ps = conn.prepareStatement("SELECT meter_id FROM user");
                java.sql.ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    meternumber.addItem(rs.getString("meter_id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately, e.g., show an error dialog
        }

        addRow(card, gbc, "Meter Number", meternumber);

        lblname = valueLabel();
        labeladdress = valueLabel();
        addRow(card, gbc, "Name", lblname);
        addRow(card, gbc, "Address", labeladdress);

        meternumber.addActionListener(e -> {
            try (java.sql.Connection listenerConn = Database.getConnection()) {
                if (listenerConn == null) throw new Exception("Database connection is null.");

                Object selectedItem = meternumber.getSelectedItem();
                if (selectedItem == null) {
                    lblname.setText("No user selected");
                    labeladdress.setText("-");
                    return; // Exit if no item is selected
                }
                
                try (java.sql.PreparedStatement ps = listenerConn.prepareStatement("SELECT name, address FROM user WHERE meter_id = ?")) {

                    ps.setString(1, selectedItem.toString());
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            lblname.setText(rs.getString("name"));
                            labeladdress.setText(rs.getString("address"));
                        } else {
                            lblname.setText("User not found");
                            labeladdress.setText("-");
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                lblname.setText("Error");
                labeladdress.setText("Error");
            }
        });

        tfunits = textField();
        calculate = createRoundedButton("Calculate",CALCULATE_COLOR);
        calculate.addActionListener(this);
        addUnitsRow(card, gbc, tfunits, calculate);

        cmonth = new JComboBox<>(new String[]{
                "January","February","March","April","May","June",
                "July","August","September","October","November","December"
        });
        addRow(card, gbc, "Month", cmonth);

        cyear = new JComboBox<>();
        for (int i = 2020; i <= 2025; i++) cyear.addItem(String.valueOf(i));
        addRow(card, gbc, "Year", cyear);

        finalAmount = valueLabel();
        addRow(card, gbc, "Final Amount", finalAmount);

        cstatus = new JComboBox<>(new String[]{"Pending", "Paid"});
        addRow(card, gbc, "Status", cstatus);

        // ================= BUTTONS =================
        gbc.gridx = 0;
        gbc.gridwidth = 3;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);

        cancel = createRoundedButton("Cancel", new Color(150, 150, 150));
        generate = createRoundedButton("Generate Bill", PRIMARY_COLOR);

        cancel.addActionListener(this);
        generate.addActionListener(this);

        btnPanel.add(cancel);
        btnPanel.add(generate);
        card.add(btnPanel, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(card);

        add(center, BorderLayout.CENTER);

        setVisible(true);
    }

    // ================= ACTIONS =================
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == calculate) {
            try {
                int units = Integer.parseInt(tfunits.getText());
                double amount = calculateBill(units);
                finalAmount.setText(String.format("%.2f", amount));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Enter valid units");
            }
        }

        if (ae.getSource() == generate) {
            try {
                Object selectedMeter = meternumber.getSelectedItem();
                if (selectedMeter == null) {
                    JOptionPane.showMessageDialog(this, "Please select a Meter Number.");
                    return;
                }
                int meter = Integer.parseInt(selectedMeter.toString());
                int units = Integer.parseInt(tfunits.getText());
                double amount = Double.parseDouble(finalAmount.getText());
                String month = cmonth.getSelectedItem().toString();
                String year = cyear.getSelectedItem().toString();
                String status = cstatus.getSelectedItem().toString();

                try (java.sql.Connection generateConn = Database.getConnection()) {
                    if (generateConn == null) throw new Exception("Database connection is null.");

                    try (java.sql.PreparedStatement ps = generateConn.prepareStatement("INSERT INTO bill (meter_id, month, year, units, amount, status) VALUES (?, ?, ?, ?, ?, ?)")) {

                        ps.setInt(1, meter);
                        ps.setString(2, month);
                        ps.setString(3, year);
                        ps.setInt(4, units);
                        ps.setDouble(5, amount);
                        ps.setString(6, status);
                        ps.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(this, "Bill Generated Successfully");
                    setVisible(false);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error generating bill: " + e.getMessage());
                    e.printStackTrace(); // For debugging
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for units.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (ae.getSource() == cancel || ae.getSource() == exit) {
            setVisible(false);
        }
    }

    // ================= HELPERS =================

    private static void addRow(JPanel panel, GridBagConstraints gbc, String label, Component comp) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JLabel l = new JLabel(label);
        l.setFont(LABEL_FONT);
        l.setForeground(TEXT_GRAY);
        panel.add(l, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(comp, gbc);
        gbc.gridy++;
    }

    private static void addUnitsRow(JPanel panel, GridBagConstraints gbc,
                                    JTextField unitsField, JButton calcBtn) {

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JLabel l = new JLabel("Units Consumed");
        l.setFont(LABEL_FONT);
        l.setForeground(TEXT_GRAY);
        panel.add(l, gbc);

        gbc.gridx = 1;
        panel.add(unitsField, gbc);

        gbc.gridx = 2;
        panel.add(calcBtn, gbc);

        gbc.gridy++;
    }

    private static JTextField textField() {
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(240, 34));
        t.setFont(FIELD_FONT);
        t.setBorder(new CompoundBorder(
                new LineBorder(BORDER_GRAY, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return t;
    }

    private static JLabel valueLabel() {
        JLabel l = new JLabel("-");
        l.setFont(FIELD_FONT);
        l.setOpaque(true);
        l.setBackground(FRAME_BG);
        l.setBorder(new EmptyBorder(6, 10, 6, 10));
        return l;
    }

    private static JButton createRoundedButton(String text, Color color) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                super.paintComponent(g);
                g2.dispose();
            }
            public void paintBorder(Graphics g) {}
        };

        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 26, 10, 26));
        return b;
    }

    public static double calculateBill(int units) {
        if (units <= 100) return units * 4.0;
        if (units <= 200) return 100 * 4 + (units - 100) * 6;
        return 100 * 4 + 100 * 6 + (units - 200) * 8;
    }
}
