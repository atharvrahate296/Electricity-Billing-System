import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class PayBill extends JFrame implements ActionListener {

    // UI Components
    JLabel meternumberLabel, nameLabel;
    JButton pay, back, downloadReceipt;
    JTable billTable;
    DefaultTableModel tableModel;
    JLabel noBillMessage;

    String meter;
    // Store last paid bill details for receipt generation
    private int lastPaidBillId = -1;
    private String lastPaidMonth = "";
    private String lastPaidYear = "";

    // THEME
    private static final Color FRAME_BG  = new Color(245, 247, 250);
    private static final Color HEADER_BG = new Color(52, 58, 64);
    private static final Color PRIMARY   = new Color(30, 144, 255);
    private static final Color SUCCESS   = new Color(33, 150, 83);
    private static final Color MUTED     = new Color(108, 117, 125);
    private static final Color BORDER    = new Color(220, 224, 229);
    private static final Color TEXT      = new Color(33, 37, 41);

    private static final Font TITLE = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font LABEL = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FIELD = new Font("Segoe UI", Font.PLAIN, 14);

    public PayBill(String meter) {
        this.meter = meter;

        setTitle("Pay Bill");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FRAME_BG);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY),
                new EmptyBorder(14, 25, 14, 25)
        ));

        JLabel heading = new JLabel("Electricity Bill Payment");
        heading.setFont(TITLE);
        heading.setForeground(Color.WHITE);

        header.add(heading, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ================= CARD =================
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(780, 430));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // ------------ LEFT (USER INFO) -------------
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(260, 380));

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.insets = new Insets(8, 8, 8, 8);
        gbcLeft.anchor = GridBagConstraints.WEST;
        gbcLeft.fill = GridBagConstraints.HORIZONTAL;
        gbcLeft.weightx = 1;
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;

        meternumberLabel = valueLabel();
        nameLabel         = valueLabel();

        addRow(leftPanel, gbcLeft, "Meter Number", meternumberLabel);
        addRow(leftPanel, gbcLeft, "Name",           nameLabel);

        gbcLeft.gridx = 0;
        gbcLeft.gridwidth = 2;
        gbcLeft.gridy++;
        noBillMessage = new JLabel("", SwingConstants.LEFT);
        noBillMessage.setFont(FIELD);
        noBillMessage.setForeground(SUCCESS);
        leftPanel.add(noBillMessage, gbcLeft);

        card.add(leftPanel, BorderLayout.WEST);

        // ------------ CENTER (TABLE) -------------
        String[] columns = {"Month", "Year", "Units", "Amount", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        billTable = new JTable(tableModel);
        billTable.setFont(FIELD);
        billTable.setRowHeight(26);
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        billTable.getTableHeader().setFont(LABEL);
        billTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(billTable);
        scroll.setBorder(new LineBorder(BORDER));

        card.add(scroll, BorderLayout.CENTER);

        // ------------ IMAGE -------------
        ImageIcon icon = new ImageIcon("lib/icon/bill.png");
        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            imgLabel.setBorder(new EmptyBorder(10, 20, 10, 20));
            card.add(imgLabel, BorderLayout.EAST);
        }

        // ------------ BUTTONS -------------
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        buttons.setOpaque(false);

        back = createButton("Back", MUTED);
        pay  = createButton("Pay Now", SUCCESS);
        downloadReceipt = createButton("Download Receipt", PRIMARY);
        downloadReceipt.setEnabled(false);

        back.addActionListener(this);
        pay.addActionListener(this);
        downloadReceipt.addActionListener(this);

        buttons.add(back);
        buttons.add(downloadReceipt);
        buttons.add(pay);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(buttons, BorderLayout.EAST);

        card.add(bottom, BorderLayout.SOUTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(card);
        add(center, BorderLayout.CENTER);

        loadUserDetails();
        loadBill();

        setVisible(true);
    }

    private void loadUserDetails() {
        try (Connection c = Database.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement("SELECT name FROM user WHERE meter_id = ?")) {
                ps.setString(1, meter);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        meternumberLabel.setText(meter);
                        nameLabel.setText(rs.getString("name"));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadBill() {
        tableModel.setRowCount(0);
        noBillMessage.setText("");
        pay.setEnabled(false);
        boolean foundBills = false;

        try (Connection c = Database.getConnection()) {
            String sql = "SELECT month, year, units, amount, status FROM bill WHERE meter_id = ? AND status = 'Pending'";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, meter);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        foundBills = true;
                        tableModel.addRow(new Object[] {
                                rs.getString("month"),
                                rs.getString("year"),
                                rs.getInt("units"),
                                rs.getDouble("amount"),
                                rs.getString("status")
                        });
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!foundBills) {
            noBillMessage.setText("No pending bills found.");
        } else {
            pay.setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            setVisible(false);
        } else if (e.getSource() == downloadReceipt) {
            downloadReceiptAction();
        } else if (e.getSource() == pay) {
            int selectedRow = billTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a bill to pay.");
                return;
            }

            new PaymentScreen(
                    this,
                    meter,
                    nameLabel.getText(),
                    tableModel.getValueAt(selectedRow, 0).toString(),
                    tableModel.getValueAt(selectedRow, 1).toString(),
                    tableModel.getValueAt(selectedRow, 2).toString(),
                    tableModel.getValueAt(selectedRow, 3).toString()
            );
        }
    }

    private void downloadReceiptAction() {
        if (lastPaidBillId == -1) {
            JOptionPane.showMessageDialog(this, "No paid bill available. Please pay a bill first.");
            return;
        }

        try (Connection c = Database.getConnection()) {
            // Fetch bill details
            String billQuery = "SELECT b.bill_id, u.meter_id, u.name, u.address, u.rmn, b.month, b.year, b.units, b.amount FROM bill b JOIN user u ON b.meter_id = u.meter_id WHERE b.bill_id = ?";
            try (PreparedStatement ps = c.prepareStatement(billQuery)) {
                ps.setInt(1, lastPaidBillId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        BillReceipt receipt = new BillReceipt(
                                rs.getInt("bill_id"),
                                rs.getInt("meter_id"),
                                rs.getString("name"),
                                rs.getString("address"),
                                rs.getString("rmn"),
                                rs.getString("month"),
                                rs.getString("year"),
                                rs.getInt("units"),
                                rs.getDouble("amount")
                        );

                        // Create file chooser
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Save Receipt As");
                        fileChooser.setSelectedFile(new java.io.File("Receipt_" + receipt.getTransactionId() + ".txt"));
                        
                        // Add file filters
                        javax.swing.filechooser.FileNameExtensionFilter txtFilter = 
                            new javax.swing.filechooser.FileNameExtensionFilter("Text Files (*.txt)", "txt");
                        javax.swing.filechooser.FileNameExtensionFilter csvFilter = 
                            new javax.swing.filechooser.FileNameExtensionFilter("CSV Files (*.csv)", "csv");
                        
                        fileChooser.addChoosableFileFilter(txtFilter);
                        fileChooser.addChoosableFileFilter(csvFilter);
                        fileChooser.setFileFilter(txtFilter);

                        int userSelection = fileChooser.showSaveDialog(this);

                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            java.io.File fileToSave = fileChooser.getSelectedFile();
                            
                            boolean success = false;
                            if (fileChooser.getFileFilter() == csvFilter) {
                                success = receipt.exportAsCSV(fileToSave);
                            } else {
                                success = receipt.exportToFile(fileToSave);
                            }

                            if (success) {
                                JOptionPane.showMessageDialog(this, 
                                    "Receipt downloaded successfully!\nFile saved: " + fileToSave.getAbsolutePath());
                            } else {
                                JOptionPane.showMessageDialog(this, 
                                    "Error saving receipt. Please try again.", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error fetching bill details: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private static void addRow(JPanel p, GridBagConstraints gbc, String text, JLabel value) {
        gbc.gridx = 0;
        JLabel l = new JLabel(text);
        l.setFont(LABEL);
        p.add(l, gbc);
        gbc.gridx = 1;
        p.add(value, gbc);
        gbc.gridy++;
    }

    private static JLabel valueLabel() {
        JLabel l = new JLabel("-");
        l.setFont(FIELD);
        l.setOpaque(true);
        l.setBackground(FRAME_BG);
        l.setBorder(new EmptyBorder(6, 10, 6, 10));
        l.setPreferredSize(new Dimension(220, 32));
        return l;
    }

    private static JButton createButton(String text, Color bg) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }
            protected void paintBorder(Graphics g) {}
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 26, 10, 26));
        return b;
    }

    // ================= INNER CLASS: PAYMENT SCREEN =================

    class PaymentScreen extends JDialog implements ActionListener {

        JPasswordField passwordField;
        JButton confirmPay;
        String meterId, userName, month, year, units, amount;

        PaymentScreen(Frame owner, String meterId, String userName, String month, String year, String units, String amount) {
            super(owner, "Confirm Payment", true);
            this.meterId = meterId; this.userName = userName;
            this.month = month; this.year = year;
            this.units = units; this.amount = amount;

            setSize(450, 500);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout());
            getContentPane().setBackground(FRAME_BG);

            JPanel form = new JPanel(new GridBagLayout());
            form.setOpaque(false);
            form.setBorder(new EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;

            addRow(form, gbc, "Meter Number", createValueLabel(meterId));
            addRow(form, gbc, "Name", createValueLabel(userName));
            addRow(form, gbc, "Month", createValueLabel(month));
            addRow(form, gbc, "Year", createValueLabel(year));
            addRow(form, gbc, "Amount", createValueLabel(amount));

            gbc.gridx = 0; gbc.gridy++;
            JLabel passL = new JLabel("Enter Password"); passL.setFont(LABEL);
            form.add(passL, gbc);

            passwordField = new JPasswordField();
            passwordField.setFont(FIELD);
            gbc.gridx = 1;
            form.add(passwordField, gbc);

            add(form, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            confirmPay = createButton("Pay", SUCCESS);
            confirmPay.addActionListener(this);
            bottom.add(confirmPay);
            add(bottom, BorderLayout.SOUTH);

            setVisible(true);
        }

        private JLabel createValueLabel(String text) {
            JLabel l = new JLabel(text);
            l.setFont(FIELD);
            l.setOpaque(true);
            l.setBackground(Color.WHITE);
            l.setBorder(new EmptyBorder(5, 8, 5, 8));
            return l;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredPassword = new String(passwordField.getPassword());

            if (enteredPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter password.");
                return;
            }

            try (Connection c = Database.getConnection()) {
                // Fetch the hashed password from database
                String userQuery = "SELECT password FROM user WHERE meter_id = ?";
                try (PreparedStatement ps = c.prepareStatement(userQuery)) {
                    ps.setString(1, meterId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String storedHash = rs.getString("password");

                            // --- SECURE VERIFICATION USING BCRYPT ---
                            if (PasswordHasher.check(enteredPassword, storedHash)) {
                                // Update bill
                                String updateSql = "UPDATE bill SET status = 'Paid' WHERE meter_id = ? AND month = ? AND year = ?";
                                try (PreparedStatement psUpdate = c.prepareStatement(updateSql)) {
                                    psUpdate.setString(1, meterId);
                                    psUpdate.setString(2, month);
                                    psUpdate.setString(3, year);
                                    psUpdate.executeUpdate();
                                }
                                
                                // Store payment details for receipt generation
                                PayBill.this.lastPaidMonth = month;
                                PayBill.this.lastPaidYear = year;
                                
                                // Get bill ID for receipt
                                String billQuery = "SELECT bill_id FROM bill WHERE meter_id = ? AND month = ? AND year = ?";
                                try (PreparedStatement psBillId = c.prepareStatement(billQuery)) {
                                    psBillId.setString(1, meterId);
                                    psBillId.setString(2, month);
                                    psBillId.setString(3, year);
                                    try (ResultSet rsBillId = psBillId.executeQuery()) {
                                        if (rsBillId.next()) {
                                            PayBill.this.lastPaidBillId = rsBillId.getInt("bill_id");
                                        }
                                    }
                                }
                                
                                JOptionPane.showMessageDialog(this, "Payment Successful!\nYou can now download the receipt.");
                                PayBill.this.loadBill();
                                PayBill.this.downloadReceipt.setEnabled(true);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(this, "Incorrect password.");
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}