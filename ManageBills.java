import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

    public class ManageBills extends JFrame implements ActionListener {

    JTable table;
    JButton save, back, exit, refresh, delete;
    JComboBox<String> statusComboBox;
    private DefaultTableModel model;

    // ---- THEME (CONSISTENT APP-WIDE) ----
    private static final Color FRAME_BG    = new Color(245, 247, 250);
    private static final Color HEADER_BG   = new Color(52, 58, 64);
    private static final Color PRIMARY     = new Color(30, 144, 255);
    private static final Color SUCCESS     = new Color(40, 167, 69);
    private static final Color DANGER      = new Color(220, 53, 69);
    private static final Color MUTED       = new Color(160, 160, 160);
    private static final Color BORDER_GRAY = new Color(220, 224, 229);
    private static final Color TEXT_GRAY   = new Color(33, 37, 41);

    public ManageBills() {

        super("Manage Bills");

        model = new DefaultTableModel();

        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FRAME_BG);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY),
                new EmptyBorder(14, 25, 14, 25)
            )
        );

        JLabel title = new JLabel("Manage Bills");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ================= TABLE =================
        model = new DefaultTableModel(new String[]{
                "Bill ID", "Meter Number", "Month", "Year",
                "Units", "Amount", "Status"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(210, 225, 245));
        table.setGridColor(BORDER_GRAY);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 14));
        th.setBackground(new Color(235, 237, 240));
        th.setForeground(TEXT_GRAY);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(Color.WHITE);
        tableWrap.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_GRAY, 1, true),
                new EmptyBorder(15, 15, 15, 15)
            )
        );
        tableWrap.add(sp, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(10, 20, 10, 20));
        center.add(tableWrap, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // ================= FOOTER =================
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(12, 20, 12, 20));
        footer.setBackground(FRAME_BG);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        JLabel statusLbl = new JLabel("Status:");
        statusLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLbl.setForeground(TEXT_GRAY);
        left.add(statusLbl);

        statusComboBox = new JComboBox<>(new String[]{"Pending", "Paid"});
        statusComboBox.setPreferredSize(new Dimension(140, 32));
        statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusComboBox.setBorder(new LineBorder(BORDER_GRAY, 1, true));
        left.add(statusComboBox);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        save    = new FlatRoundedButton("Save", PRIMARY);
        refresh = new FlatRoundedButton("Refresh", SUCCESS);
        delete  = new FlatRoundedButton("Delete", DANGER);
        back    = new FlatRoundedButton("Back", MUTED);
        exit    = new FlatRoundedButton("Exit", MUTED);

        right.add(save);
        right.add(refresh);
        right.add(delete);
        right.add(back);
        right.add(exit);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);

        // ================= LISTENERS =================
        save.addActionListener(this);
        refresh.addActionListener(this);
        delete.addActionListener(this);
        back.addActionListener(this);
        exit.addActionListener(this);

        refreshTable();
        setVisible(true);
    }

    // ================= LOGIC (UNCHANGED) =================
    void refreshTable() {
        model.setRowCount(0);
        try (java.sql.Connection c = Database.getConnection()) {
            if (c == null) throw new Exception("Database connection is null.");

            try (java.sql.PreparedStatement ps = c.prepareStatement("SELECT bill_id, meter_id, month, year, units, amount, status FROM bill");
                 java.sql.ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("bill_id"),
                            rs.getInt("meter_id"),
                            rs.getString("month"),
                            rs.getString("year"),
                            rs.getInt("units"),
                            String.format("%.2f", rs.getDouble("amount")),
                            rs.getString("status")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == save) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                try (java.sql.Connection c = Database.getConnection()) {
                    if (c == null) throw new Exception("Database connection is null.");

                    try (java.sql.PreparedStatement ps = c.prepareStatement("UPDATE bill SET status = ? WHERE bill_id = ?")) {

                        int billId = (int) table.getValueAt(row, 0);
                        String status = (String) statusComboBox.getSelectedItem();

                        ps.setString(1, status);
                        ps.setInt(2, billId);
                        ps.executeUpdate();

                        refreshTable();
                        JOptionPane.showMessageDialog(this, "Bill Status Updated");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating bill status");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a bill first");
            }
        }

        if (ae.getSource() == refresh) refreshTable();

        if (ae.getSource() == delete) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int billId = (int) table.getValueAt(row, 0);
                int c = JOptionPane.showConfirmDialog(
                        this,
                        "Delete this bill?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );
                if (c == JOptionPane.YES_OPTION) {
                    try (java.sql.Connection con = Database.getConnection()) {
                        if (con == null) throw new Exception("Database connection is null.");

                        try (java.sql.PreparedStatement ps = con.prepareStatement("DELETE FROM bill WHERE bill_id = ?")) {

                            ps.setInt(1, billId);
                            ps.executeUpdate();

                            refreshTable();
                            JOptionPane.showMessageDialog(this, "Bill Deleted Successfully");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error deleting bill");
                    }
                }
            }
        }

        if (ae.getSource() == back || ae.getSource() == exit) {
            setVisible(false);
        }
    }


    // ================= BUTTON CLASS =================
    class FlatRoundedButton extends JButton {

        private final Color base;
        private final Color hover;

        public FlatRoundedButton(String text, Color baseColor) {
            super(text);
            this.base = baseColor;
            this.hover = baseColor.darker();

            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(10, 20, 10, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            Color fill = getModel().isRollover() ? hover : base;
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);

            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {}
    }
}